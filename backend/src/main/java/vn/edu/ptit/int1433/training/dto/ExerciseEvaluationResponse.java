package vn.edu.ptit.int1433.training.dto;

import java.util.List;
import java.util.Map;

public record ExerciseEvaluationResponse(
    String exerciseId,
    String evaluationMode,
    List<String> supportedLanguages,
    Integer timeLimitMs,
    Integer memoryLimitMb,
    Integer networkSessionTtlSeconds,
    Integer maxAttempts,
    boolean starterDownloadAvailable,
    String starterAssetPath,
    Map<String, Object> verdictPolicy,
    List<String> instructions
) {
}
