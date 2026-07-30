package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    private String id;

    private String title;
    private String summary;

    @Enumerated(EnumType.STRING)
    private ExerciseStatus status;

    @Enumerated(EnumType.STRING)
    private Track track;

    private String technology;
    private String protocol;
    private String transport;

    @Column(name = "stream_type")
    private String streamType;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private ExerciseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_label")
    private SourceLabel sourceLabel;

    @Column(columnDefinition = "text")
    private String statement;

    @Column(name = "processing_requirement", columnDefinition = "text")
    private String processingRequirement;

    @Column(name = "request_format", columnDefinition = "text")
    private String requestFormat;

    @Column(name = "response_format", columnDefinition = "text")
    private String responseFormat;

    @Column(name = "submission_format", columnDefinition = "text")
    private String submissionFormat;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "display_order")
    private Integer displayOrder;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "server_contract", columnDefinition = "jsonb")
    private Map<String, Object> serverContract;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "timeout_config", columnDefinition = "jsonb")
    private Map<String, Object> timeoutConfig;

    @Column(name = "evaluation_mode")
    private String evaluationMode;

    @Column(name = "grader_key")
    private String graderKey;

    @Column(name = "time_limit_ms")
    private Integer timeLimitMs;

    @Column(name = "memory_limit_mb")
    private Integer memoryLimitMb;

    @Column(name = "network_session_ttl_seconds")
    private Integer networkSessionTtlSeconds;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "starter_asset_path")
    private String starterAssetPath;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "language_policy", columnDefinition = "jsonb")
    private List<String> languagePolicy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "verdict_policy", columnDefinition = "jsonb")
    private Map<String, Object> verdictPolicy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "constraints_json", columnDefinition = "jsonb")
    private List<Object> constraints;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "examples_json", columnDefinition = "jsonb")
    private List<Object> examples;

    @Column(name = "evidence_disclaimer", columnDefinition = "text")
    private String evidenceDisclaimer;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "exercise_tags", joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "tag")
    private Set<String> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseCommonFailure> commonFailures = List.of();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseHint> hints = List.of();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLearningObjective> learningObjectives = List.of();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisePrerequisite> prerequisites = List.of();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSource> sources = List.of();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public ExerciseStatus getStatus() { return status; }
    public Track getTrack() { return track; }
    public String getTechnology() { return technology; }
    public String getProtocol() { return protocol; }
    public String getTransport() { return transport; }
    public String getStreamType() { return streamType; }
    public Difficulty getDifficulty() { return difficulty; }
    public ExerciseLevel getLevel() { return level; }
    public SourceLabel getSourceLabel() { return sourceLabel; }
    public String getStatement() { return statement; }
    public String getProcessingRequirement() { return processingRequirement; }
    public String getRequestFormat() { return requestFormat; }
    public String getResponseFormat() { return responseFormat; }
    public String getSubmissionFormat() { return submissionFormat; }
    public Integer getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Map<String, Object> getServerContract() { return serverContract; }
    public Map<String, Object> getTimeoutConfig() { return timeoutConfig; }
    public String getEvaluationMode() { return evaluationMode; }
    public String getGraderKey() { return graderKey; }
    public Integer getTimeLimitMs() { return timeLimitMs; }
    public Integer getMemoryLimitMb() { return memoryLimitMb; }
    public Integer getNetworkSessionTtlSeconds() { return networkSessionTtlSeconds; }
    public Integer getMaxAttempts() { return maxAttempts; }
    public String getStarterAssetPath() { return starterAssetPath; }
    public List<String> getLanguagePolicy() { return languagePolicy; }
    public Map<String, Object> getVerdictPolicy() { return verdictPolicy; }
    public List<Object> getConstraints() { return constraints; }
    public List<Object> getExamples() { return examples; }
    public String getEvidenceDisclaimer() { return evidenceDisclaimer; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public Set<String> getTags() { return tags; }
    public List<ExerciseCommonFailure> getCommonFailures() { return commonFailures; }
    public List<ExerciseHint> getHints() { return hints; }
    public List<ExerciseLearningObjective> getLearningObjectives() { return learningObjectives; }
    public List<ExercisePrerequisite> getPrerequisites() { return prerequisites; }
    public List<ExerciseSource> getSources() { return sources; }
}
