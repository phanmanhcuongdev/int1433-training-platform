package vn.edu.ptit.int1433.training.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.dto.ExerciseDetailResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseSourceResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseSummaryResponse;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.entity.ExerciseCommonFailure;
import vn.edu.ptit.int1433.training.entity.ExerciseHint;
import vn.edu.ptit.int1433.training.entity.ExerciseLearningObjective;
import vn.edu.ptit.int1433.training.entity.ExercisePrerequisite;
import vn.edu.ptit.int1433.training.entity.ExerciseSource;

@Component
public class ExerciseMapper {
    public ExerciseSummaryResponse toSummary(Exercise exercise) {
        return new ExerciseSummaryResponse(
            exercise.getId(),
            exercise.getTitle(),
            exercise.getSummary(),
            exercise.getStatus().name(),
            exercise.getTrack().name(),
            exercise.getTechnology(),
            exercise.getProtocol(),
            exercise.getStreamType(),
            exercise.getDifficulty().name(),
            exercise.getLevel().name(),
            exercise.getSourceLabel().name(),
            exercise.getEstimatedTimeMinutes(),
            exercise.getDisplayOrder(),
            sortedStrings(exercise.getTags())
        );
    }

    public ExerciseDetailResponse toDetail(Exercise exercise) {
        return new ExerciseDetailResponse(
            exercise.getId(),
            exercise.getTitle(),
            exercise.getSummary(),
            exercise.getStatus().name(),
            exercise.getTrack().name(),
            exercise.getTechnology(),
            exercise.getProtocol(),
            exercise.getTransport(),
            exercise.getStreamType(),
            exercise.getDifficulty().name(),
            exercise.getLevel().name(),
            exercise.getSourceLabel().name(),
            exercise.getStatement(),
            exercise.getProcessingRequirement(),
            exercise.getRequestFormat(),
            exercise.getResponseFormat(),
            exercise.getSubmissionFormat(),
            exercise.getEstimatedTimeMinutes(),
            exercise.getDisplayOrder(),
            exercise.getServerContract(),
            exercise.getTimeoutConfig(),
            sortedStrings(exercise.getTags()),
            exercise.getCommonFailures().stream()
                .sorted(Comparator.comparing(ExerciseCommonFailure::getDisplayOrder))
                .map(ExerciseCommonFailure::getFailureCode)
                .toList(),
            exercise.getHints().stream()
                .sorted(Comparator.comparing(ExerciseHint::getDisplayOrder))
                .map(ExerciseHint::getContent)
                .toList(),
            exercise.getLearningObjectives().stream()
                .sorted(Comparator.comparing(ExerciseLearningObjective::getDisplayOrder))
                .map(ExerciseLearningObjective::getObjective)
                .toList(),
            exercise.getPrerequisites().stream()
                .sorted(Comparator.comparing(ExercisePrerequisite::getDisplayOrder))
                .map(ExercisePrerequisite::getPrerequisite)
                .toList(),
            exercise.getSources().stream()
                .sorted(Comparator.comparing(source -> source.getClaimId() == null ? "" : source.getClaimId()))
                .map(this::toSource)
                .toList()
        );
    }

    private ExerciseSourceResponse toSource(ExerciseSource source) {
        return new ExerciseSourceResponse(source.getClaimId(), source.getSourceFile(), source.getEvidenceNote());
    }

    private List<String> sortedStrings(Iterable<String> values) {
        List<String> result = new ArrayList<>();
        values.forEach(result::add);
        result.sort(String::compareTo);
        return result;
    }
}
