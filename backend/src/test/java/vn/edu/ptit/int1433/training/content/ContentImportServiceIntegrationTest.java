package vn.edu.ptit.int1433.training.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.edu.ptit.int1433.training.AbstractPostgresIntegrationTest;

class ContentImportServiceIntegrationTest extends AbstractPostgresIntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @TempDir
    Path tempDir;

    @Test
    void dryRunDoesNotMutateDatabase() throws Exception {
        Path root = copyContent();
        String before = title("fnd-character-flush-001");

        ContentImportResult result = service(root).run(true, false);

        assertThat(result.success()).isTrue();
        assertThat(result.unchanged()).isEqualTo(10);
        assertThat(title("fnd-character-flush-001")).isEqualTo(before);
    }

    @Test
    void changedExerciseUpdatesAndCanBeRestored() throws Exception {
        Path root = copyContent();
        Path file = root.resolve("exercises/foundation/fnd-character-flush-001.json");
        ObjectNode json = (ObjectNode) objectMapper.readTree(file.toFile());
        json.put("title", "Character Stream và flush() - test import");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), json);

        ContentImportResult changed = service(root).run(false, false);
        assertThat(changed.success()).isTrue();
        assertThat(title("fnd-character-flush-001")).isEqualTo("Character Stream và flush() - test import");

        ContentImportResult restored = service(Path.of("../content")).run(false, false);
        assertThat(restored.success()).isTrue();
        assertThat(title("fnd-character-flush-001")).isEqualTo("Character Stream và flush()");
    }

    @Test
    void missingGraderKeyAbortsImport() throws Exception {
        Path root = copyContent();
        Path file = root.resolve("exercises/foundation/fnd-character-flush-001.json");
        ObjectNode json = (ObjectNode) objectMapper.readTree(file.toFile());
        json.remove("grader_key");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), json);

        ContentImportResult result = service(root).run(false, false);

        assertThat(result.success()).isFalse();
        assertThat(result.errors()).anySatisfy(error -> assertThat(error).contains("grader_key"));
    }

    @Test
    void databaseOnlyRecordsAreRefusedByDefault() throws Exception {
        Path root = copyContent();
        Files.delete(root.resolve("exercises/foundation/fnd-character-flush-001.json"));

        ContentImportResult result = service(root).run(true, false);

        assertThat(result.success()).isFalse();
        assertThat(result.errors()).anySatisfy(error -> assertThat(error).contains("Expected exactly 10"));
    }

    private ContentImportService service(Path root) {
        return new ContentImportService(new ContentImportProperties(root.toString()), objectMapper, jdbcTemplate);
    }

    private String title(String id) {
        return jdbcTemplate.queryForObject("select title from exercises where id = ?", String.class, id);
    }

    private Path copyContent() throws Exception {
        Path source = Path.of("../content").toAbsolutePath().normalize();
        Path target = tempDir.resolve("content");
        try (var stream = Files.walk(source)) {
            for (Path path : stream.sorted(Comparator.comparing(Path::toString)).toList()) {
                Path relative = source.relativize(path);
                Path destination = target.resolve(relative);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(destination);
                } else {
                    Files.copy(path, destination);
                }
            }
        }
        return target;
    }
}
