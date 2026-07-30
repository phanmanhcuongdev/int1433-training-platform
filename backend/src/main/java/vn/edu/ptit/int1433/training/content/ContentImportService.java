package vn.edu.ptit.int1433.training.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentImportService {
    private static final Set<String> REQUIRED = Set.of(
        "id", "title", "status", "track", "technology", "difficulty", "level", "source_label",
        "statement", "processing_requirement", "evaluation_mode", "grader_key", "tags",
        "common_failures", "estimated_time"
    );

    private final ContentImportProperties properties;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    public ContentImportService(ContentImportProperties properties, ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public ContentImportResult run(boolean dryRun, boolean allowDelete) {
        List<String> errors = new ArrayList<>();
        List<ContentExerciseRecord> records;
        try {
            records = loadRecords();
        } catch (RuntimeException exception) {
            String message = exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
            return new ContentImportResult(0, 0, 0, 0, List.of(), Map.of(), List.of(message), dryRun);
        }

        validate(records, errors);
        if (!errors.isEmpty()) {
            return new ContentImportResult(0, 0, 0, 0, List.of(), Map.of(), errors, dryRun);
        }

        Map<String, Map<String, Object>> dbRecords = currentDbRecords();
        Map<String, ContentExerciseRecord> contentById = new LinkedHashMap<>();
        for (ContentExerciseRecord record : records) {
            contentById.put(record.id(), record);
        }

        List<String> databaseOnly = dbRecords.keySet().stream()
            .filter(id -> !contentById.containsKey(id))
            .toList();
        if (!databaseOnly.isEmpty() && !allowDelete) {
            errors.add("Database-only exercise records require --allow-delete: " + databaseOnly);
        }

        int created = 0;
        int updated = 0;
        int unchanged = 0;
        Map<String, List<String>> changedFields = new LinkedHashMap<>();
        for (ContentExerciseRecord record : records) {
            Map<String, Object> current = dbRecords.get(record.id());
            if (current == null) {
                created += 1;
                changedFields.put(record.id(), List.of("new"));
            } else {
                List<String> changed = changedFields(record, current);
                if (changed.isEmpty()) {
                    unchanged += 1;
                } else {
                    updated += 1;
                    changedFields.put(record.id(), changed);
                }
            }
        }

        if (!errors.isEmpty() || dryRun) {
            return new ContentImportResult(created, updated, unchanged, 0, databaseOnly, changedFields, errors, dryRun);
        }

        for (ContentExerciseRecord record : records) {
            upsert(record);
            replaceChildren(record);
        }
        int deleted = 0;
        if (allowDelete) {
            for (String id : databaseOnly) {
                jdbcTemplate.update("delete from exercises where id = ?", id);
                deleted += 1;
            }
        }
        return new ContentImportResult(created, updated, unchanged, deleted, databaseOnly, changedFields, errors, false);
    }

    private List<ContentExerciseRecord> loadRecords() {
        Path root = Path.of(properties.root()).toAbsolutePath().normalize();
        Path exercises = root.resolve("exercises").normalize();
        if (!exercises.startsWith(root) || !Files.isDirectory(exercises)) {
            throw new IllegalArgumentException("Invalid content root: " + root);
        }
        try (Stream<Path> stream = Files.walk(exercises)) {
            List<Path> files = stream
                .filter(path -> path.toString().endsWith(".json"))
                .sorted(Comparator.comparing(Path::toString))
                .toList();
            List<ContentExerciseRecord> records = new ArrayList<>();
            for (Path file : files) {
                if (!file.toAbsolutePath().normalize().startsWith(root)) {
                    throw new IllegalArgumentException("Path traversal rejected: " + file);
                }
                JsonNode json = objectMapper.readTree(file.toFile());
                records.add(toRecord(json));
            }
            return records;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to read content JSON: " + exception.getMessage(), exception);
        }
    }

    private ContentExerciseRecord toRecord(JsonNode json) {
        for (String field : REQUIRED) {
            if (!json.hasNonNull(field)) {
                throw new IllegalArgumentException("Missing required content field: " + field);
            }
        }
        String mode = text(json, "evaluation_mode");
        JsonNode timeout = json.get("timeout");
        JsonNode judge = json.get("judge");
        Integer timeLimit = judge != null && judge.has("time_limit_ms") ? judge.get("time_limit_ms").asInt() : null;
        Integer memory = judge != null && judge.has("memory_limit_mb") ? judge.get("memory_limit_mb").asInt() : null;
        Integer ttl = timeout != null && timeout.has("session_ttl_seconds") ? timeout.get("session_ttl_seconds").asInt() : null;
        if ("JAVA_CODE".equals(mode) && judge != null && judge.has("run_ms")) {
            timeLimit = judge.get("run_ms").asInt();
        }
        return new ContentExerciseRecord(
            text(json, "id"), text(json, "title"), text(json, "summary"), text(json, "status"),
            text(json, "track"), text(json, "technology"), text(json, "protocol"), text(json, "transport"),
            text(json, "stream_type"), text(json, "difficulty"), text(json, "level"), text(json, "source_label"),
            text(json, "statement"), text(json, "processing_requirement"), text(json, "request_format"),
            text(json, "response_format"), text(json, "submission_format"), minutes(text(json, "estimated_time")),
            json.path("display_order").asInt(9999), json.get("server_contract"), timeout,
            mode, text(json, "grader_key"), timeLimit, memory, ttl,
            judge != null && judge.has("max_attempts") ? judge.get("max_attempts").asInt() : null,
            text(json, "starter_asset_path"), languagePolicy(json), json.get("verdict_definitions"),
            json.get("constraints"), json.get("examples"), text(json, "evidence_disclaimer"),
            stringList(json.get("tags")), stringList(json.get("common_failures")), stringList(json.get("hints")),
            stringList(json.get("learning_objectives")), stringList(json.get("prerequisites")),
            stringList(json.get("source_claim_ids")), stringList(json.get("source_files"))
        );
    }

    private void validate(List<ContentExerciseRecord> records, List<String> errors) {
        if (records.size() != 10) {
            errors.add("Expected exactly 10 content exercises, found " + records.size());
        }
        Set<String> seen = new java.util.HashSet<>();
        for (ContentExerciseRecord record : records) {
            if (!seen.add(record.id())) {
                errors.add("Duplicate exercise ID: " + record.id());
            }
            if (record.graderKey() == null || record.graderKey().isBlank()) {
                errors.add(record.id() + " missing grader_key");
            }
            if (List.of("OBSERVED", "STRONG_PATTERN").contains(record.sourceLabel())) {
                if (record.sourceClaimIds().isEmpty() || record.sourceFiles().isEmpty()) {
                    errors.add(record.id() + " missing traceability");
                }
            }
            if ("NETWORK_CHALLENGE".equals(record.evaluationMode()) && (record.networkSessionTtlSeconds() == null || record.starterAssetPath() == null)) {
                errors.add(record.id() + " missing network challenge config");
            }
            if ("JAVA_CODE".equals(record.evaluationMode()) && record.timeLimitMs() == null) {
                errors.add(record.id() + " missing Java runner config");
            }
        }
    }

    private Map<String, Map<String, Object>> currentDbRecords() {
        Map<String, Map<String, Object>> records = new LinkedHashMap<>();
        jdbcTemplate.queryForList("""
            select id, title, summary, status, statement, processing_requirement, request_format,
                   response_format, submission_format, server_contract::text as server_contract,
                   timeout_config::text as timeout_config, evaluation_mode, grader_key,
                   source_label, starter_asset_path
            from exercises order by id
            """).forEach(row -> records.put(String.valueOf(row.get("id")), row));
        return records;
    }

    private List<String> changedFields(ContentExerciseRecord record, Map<String, Object> current) {
        List<String> changed = new ArrayList<>();
        compare(changed, "title", record.title(), current.get("title"));
        compare(changed, "summary", record.summary(), current.get("summary"));
        compare(changed, "status", record.status(), current.get("status"));
        compare(changed, "statement", record.statement(), current.get("statement"));
        compare(changed, "processing_requirement", record.processingRequirement(), current.get("processing_requirement"));
        compare(changed, "request_format", record.requestFormat(), current.get("request_format"));
        compare(changed, "response_format", record.responseFormat(), current.get("response_format"));
        compare(changed, "submission_format", record.submissionFormat(), current.get("submission_format"));
        compareJson(changed, "server_contract", record.serverContract(), current.get("server_contract"));
        compareJson(changed, "timeout_config", record.timeoutConfig(), current.get("timeout_config"));
        compare(changed, "evaluation_mode", record.evaluationMode(), current.get("evaluation_mode"));
        compare(changed, "grader_key", record.graderKey(), current.get("grader_key"));
        compare(changed, "source_label", record.sourceLabel(), current.get("source_label"));
        compare(changed, "starter_asset_path", record.starterAssetPath(), current.get("starter_asset_path"));
        return changed;
    }

    private void compare(List<String> changed, String field, Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual)) {
            changed.add(field);
        }
    }

    private void compareJson(List<String> changed, String field, JsonNode expected, Object actual) {
        String actualText = actual == null ? null : String.valueOf(actual);
        if (expected == null || expected.isNull() || expected.isMissingNode()) {
            if (actualText != null) {
                changed.add(field);
            }
            return;
        }
        try {
            if (actualText == null || !expected.equals(objectMapper.readTree(actualText))) {
                changed.add(field);
            }
        } catch (Exception exception) {
            changed.add(field);
        }
    }

    private void upsert(ContentExerciseRecord record) {
        jdbcTemplate.update("""
            insert into exercises (
                id, title, summary, status, track, technology, protocol, transport, stream_type,
                difficulty, level, source_label, statement, processing_requirement, request_format,
                response_format, submission_format, estimated_time_minutes, display_order,
                server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
                memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
                language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
            ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), cast(? as jsonb), ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), cast(? as jsonb), cast(? as jsonb), cast(? as jsonb), ?)
            on conflict (id) do update set
                title = excluded.title, summary = excluded.summary, status = excluded.status, track = excluded.track,
                technology = excluded.technology, protocol = excluded.protocol, transport = excluded.transport,
                stream_type = excluded.stream_type, difficulty = excluded.difficulty, level = excluded.level,
                source_label = excluded.source_label, statement = excluded.statement,
                processing_requirement = excluded.processing_requirement, request_format = excluded.request_format,
                response_format = excluded.response_format, submission_format = excluded.submission_format,
                estimated_time_minutes = excluded.estimated_time_minutes, display_order = excluded.display_order,
                server_contract = excluded.server_contract, timeout_config = excluded.timeout_config,
                evaluation_mode = excluded.evaluation_mode, grader_key = excluded.grader_key,
                time_limit_ms = excluded.time_limit_ms, memory_limit_mb = excluded.memory_limit_mb,
                network_session_ttl_seconds = excluded.network_session_ttl_seconds, max_attempts = excluded.max_attempts,
                starter_asset_path = excluded.starter_asset_path, language_policy = excluded.language_policy,
                verdict_policy = excluded.verdict_policy, constraints_json = excluded.constraints_json,
                examples_json = excluded.examples_json, evidence_disclaimer = excluded.evidence_disclaimer,
                updated_at = now()
            """,
            record.id(), record.title(), record.summary(), record.status(), record.track(), record.technology(),
            record.protocol(), record.transport(), record.streamType(), record.difficulty(), record.level(),
            record.sourceLabel(), record.statement(), record.processingRequirement(), record.requestFormat(),
            record.responseFormat(), record.submissionFormat(), record.estimatedTimeMinutes(), record.displayOrder(),
            json(record.serverContract()), json(record.timeoutConfig()), record.evaluationMode(), record.graderKey(),
            record.timeLimitMs(), record.memoryLimitMb(), record.networkSessionTtlSeconds(), record.maxAttempts(),
            record.starterAssetPath(), json(record.languagePolicy()), json(record.verdictPolicy()),
            json(record.constraints()), json(record.examples()), record.evidenceDisclaimer()
        );
    }

    private void replaceChildren(ContentExerciseRecord record) {
        for (String table : List.of("exercise_tags", "exercise_common_failures", "exercise_hints", "exercise_learning_objectives", "exercise_prerequisites", "exercise_sources")) {
            jdbcTemplate.update("delete from " + table + " where exercise_id = ?", record.id());
        }
        for (String tag : record.tags()) jdbcTemplate.update("insert into exercise_tags (exercise_id, tag) values (?, ?)", record.id(), tag);
        for (int i = 0; i < record.commonFailures().size(); i += 1) jdbcTemplate.update("insert into exercise_common_failures (exercise_id, failure_code, display_order) values (?, ?, ?)", record.id(), record.commonFailures().get(i), i + 1);
        for (int i = 0; i < record.hints().size(); i += 1) jdbcTemplate.update("insert into exercise_hints (exercise_id, content, display_order) values (?, ?, ?)", record.id(), record.hints().get(i), i + 1);
        for (int i = 0; i < record.learningObjectives().size(); i += 1) jdbcTemplate.update("insert into exercise_learning_objectives (exercise_id, objective, display_order) values (?, ?, ?)", record.id(), record.learningObjectives().get(i), i + 1);
        for (int i = 0; i < record.prerequisites().size(); i += 1) jdbcTemplate.update("insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values (?, ?, ?)", record.id(), record.prerequisites().get(i), i + 1);
        int sources = Math.max(record.sourceClaimIds().size(), record.sourceFiles().size());
        for (int i = 0; i < sources; i += 1) {
            jdbcTemplate.update("insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values (?, ?, ?, ?)",
                record.id(), at(record.sourceClaimIds(), i), at(record.sourceFiles(), i), record.evidenceDisclaimer());
        }
    }

    private String at(List<String> values, int index) {
        return index < values.size() ? values.get(index) : null;
    }

    private String json(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) return null;
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid JSONB field", exception);
        }
    }

    private JsonNode languagePolicy(JsonNode json) {
        JsonNode policy = json.path("judge").path("language_policy");
        if (!policy.isMissingNode()) return policy;
        return objectMapper.valueToTree(List.of("JAVA"));
    }

    private String text(JsonNode json, String field) {
        JsonNode value = json.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    private List<String> stringList(JsonNode node) {
        if (node == null || !node.isArray()) return List.of();
        List<String> values = new ArrayList<>();
        node.forEach(item -> values.add(item.asText()));
        return values;
    }

    private int minutes(String value) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^(?:(\\d+)h)?(?:(\\d+)m)?$").matcher(value);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid estimated_time: " + value);
        return Integer.parseInt(matcher.group(1) == null ? "0" : matcher.group(1)) * 60
            + Integer.parseInt(matcher.group(2) == null ? "0" : matcher.group(2));
    }
}
