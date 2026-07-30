package vn.edu.ptit.int1433.training.runner;

import java.util.List;
import vn.edu.ptit.int1433.training.entity.Verdict;

public record RunnerResult(
    Verdict verdict,
    String diagnosticCode,
    String publicMessage,
    String compileOutput,
    String runtimeOutput,
    List<RunnerTestResult> tests
) {
}
