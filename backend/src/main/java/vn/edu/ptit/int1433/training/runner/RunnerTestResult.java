package vn.edu.ptit.int1433.training.runner;

import vn.edu.ptit.int1433.training.entity.Verdict;

public record RunnerTestResult(
    int testIndex,
    Verdict verdict,
    int executionTimeMs,
    String diagnosticCode,
    String publicMessage
) {
}
