package vn.edu.ptit.int1433.training.dto;

import java.util.List;
import java.util.Map;

public record ExerciseDetailResponse(
    String id,
    String title,
    String summary,
    String status,
    String track,
    String technology,
    String protocol,
    String transport,
    String streamType,
    String difficulty,
    String level,
    String sourceLabel,
    String evaluationMode,
    String statement,
    String processingRequirement,
    String requestFormat,
    String responseFormat,
    String submissionFormat,
    Integer estimatedTimeMinutes,
    Integer displayOrder,
    Map<String, Object> serverContract,
    Map<String, Object> timeoutConfig,
    List<String> languagePolicy,
    Integer timeLimitMs,
    Integer memoryLimitMb,
    Integer networkSessionTtlSeconds,
    Integer maxAttempts,
    String starterAssetPath,
    Map<String, Object> verdictPolicy,
    List<Object> constraints,
    List<Object> examples,
    String evidenceDisclaimer,
    List<String> tags,
    List<String> commonFailures,
    List<String> hints,
    List<String> learningObjectives,
    List<String> prerequisites,
    List<ExerciseSourceResponse> sources
) {
}
