package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "participant_id")
    private UUID participantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_mode")
    private EvaluationMode evaluationMode;

    private String language;

    @Column(name = "source_code", columnDefinition = "text")
    private String sourceCode;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "source_sha256")
    private String sourceSha256;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "submitted_answer", columnDefinition = "jsonb")
    private Map<String, Object> submittedAnswer;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private BigDecimal score;

    @Column(name = "diagnostic_code")
    private String diagnosticCode;

    @Column(name = "public_message", columnDefinition = "text")
    private String publicMessage;

    @Column(name = "compile_output", columnDefinition = "text")
    private String compileOutput;

    @Column(name = "runtime_output", columnDefinition = "text")
    private String runtimeOutput;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "judged_at")
    private OffsetDateTime judgedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionTestResult> testResults = new ArrayList<>();

    public static Submission createCode(UUID participantId, Exercise exercise, String language, String sourceCode, String originalFileName, String sourceSha256) {
        Submission submission = new Submission();
        submission.id = UUID.randomUUID();
        submission.participantId = participantId;
        submission.exercise = exercise;
        submission.evaluationMode = EvaluationMode.JAVA_CODE;
        submission.language = language;
        submission.sourceCode = sourceCode;
        submission.originalFileName = originalFileName;
        submission.sourceSha256 = sourceSha256;
        submission.status = SubmissionStatus.PENDING;
        submission.verdict = Verdict.PENDING;
        submission.score = BigDecimal.ZERO;
        submission.createdAt = OffsetDateTime.now();
        return submission;
    }

    public UUID getId() { return id; }
    public Exercise getExercise() { return exercise; }
    public UUID getParticipantId() { return participantId; }
    public EvaluationMode getEvaluationMode() { return evaluationMode; }
    public String getLanguage() { return language; }
    public String getSourceCode() { return sourceCode; }
    public String getOriginalFileName() { return originalFileName; }
    public String getSourceSha256() { return sourceSha256; }
    public SubmissionStatus getStatus() { return status; }
    public Verdict getVerdict() { return verdict; }
    public BigDecimal getScore() { return score; }
    public String getDiagnosticCode() { return diagnosticCode; }
    public String getPublicMessage() { return publicMessage; }
    public String getCompileOutput() { return compileOutput; }
    public String getRuntimeOutput() { return runtimeOutput; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getJudgedAt() { return judgedAt; }
    public List<SubmissionTestResult> getTestResults() { return testResults; }

    public void markJudged(Verdict verdict, BigDecimal score, String diagnosticCode, String publicMessage, String compileOutput, String runtimeOutput, List<SubmissionTestResult> testResults) {
        this.status = SubmissionStatus.JUDGED;
        this.verdict = verdict;
        this.score = score;
        this.diagnosticCode = diagnosticCode;
        this.publicMessage = publicMessage;
        this.compileOutput = truncate(compileOutput);
        this.runtimeOutput = truncate(runtimeOutput);
        this.judgedAt = OffsetDateTime.now();
        this.testResults.clear();
        for (SubmissionTestResult testResult : testResults) {
            testResult.attach(this);
        }
        this.testResults = testResults;
    }

    private String truncate(String value) {
        if (value == null || value.length() <= 8192) {
            return value;
        }
        return value.substring(0, 8192);
    }
}
