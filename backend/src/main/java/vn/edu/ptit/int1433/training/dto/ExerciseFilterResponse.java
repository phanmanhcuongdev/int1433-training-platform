package vn.edu.ptit.int1433.training.dto;

import java.util.List;

public record ExerciseFilterResponse(
    List<String> technologies,
    List<String> levels,
    List<String> sourceLabels,
    List<String> statuses
) {
}
