package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "submission_test_results")
public class SubmissionTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Column(name = "test_index")
    private Integer testIndex;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "memory_kb")
    private Integer memoryKb;

    @Column(name = "diagnostic_code")
    private String diagnosticCode;

    @Column(name = "public_message")
    private String publicMessage;

    public static SubmissionTestResult of(int testIndex, Verdict verdict, int executionTimeMs, String diagnosticCode, String publicMessage) {
        SubmissionTestResult result = new SubmissionTestResult();
        result.testIndex = testIndex;
        result.verdict = verdict;
        result.executionTimeMs = executionTimeMs;
        result.diagnosticCode = diagnosticCode;
        result.publicMessage = publicMessage;
        return result;
    }

    public void attach(Submission submission) { this.submission = submission; }
    public Integer getTestIndex() { return testIndex; }
    public Verdict getVerdict() { return verdict; }
    public Integer getExecutionTimeMs() { return executionTimeMs; }
    public Integer getMemoryKb() { return memoryKb; }
    public String getDiagnosticCode() { return diagnosticCode; }
    public String getPublicMessage() { return publicMessage; }
}
