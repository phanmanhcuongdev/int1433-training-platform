package vn.edu.ptit.int1433.training.dto;

import java.util.List;

public record ExerciseSummaryResponse(
    String id,
    String title,
    String summary,
    String status,
    String track,
    String technology,
    String protocol,
    String streamType,
    String difficulty,
    String level,
    String sourceLabel,
    Integer estimatedTimeMinutes,
    Integer displayOrder,
    List<String> tags
) {
}
