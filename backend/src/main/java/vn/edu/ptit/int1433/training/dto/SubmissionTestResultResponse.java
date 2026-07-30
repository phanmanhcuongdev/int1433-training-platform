package vn.edu.ptit.int1433.training.dto;

public record SubmissionTestResultResponse(
    Integer testIndex,
    String verdict,
    Integer executionTimeMs,
    Integer memoryKb,
    String diagnosticCode,
    String publicMessage
) {
}
