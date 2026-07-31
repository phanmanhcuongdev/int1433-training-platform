package vn.edu.ptit.int1433.training.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SubmissionResponse(
    UUID id,
    String exerciseId,
    String evaluationMode,
    String status,
    String verdict,
    BigDecimal score,
    String diagnosticCode,
    String publicMessage,
    String originalFileName,
    String entryClassName,
    String sourceSha256,
    String sourceCode,
    String compileOutput,
    String runtimeOutput,
    OffsetDateTime createdAt,
    OffsetDateTime judgedAt,
    List<SubmissionTestResultResponse> testResults
) {
}
