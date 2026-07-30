package vn.edu.ptit.int1433.training.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ptit.int1433.training.dto.SubmissionResponse;
import vn.edu.ptit.int1433.training.dto.SubmissionTestResultResponse;
import vn.edu.ptit.int1433.training.entity.EvaluationMode;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.entity.Submission;
import vn.edu.ptit.int1433.training.entity.SubmissionTestResult;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.exception.InvalidFilterException;
import vn.edu.ptit.int1433.training.exception.SubmissionNotFoundException;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;
import vn.edu.ptit.int1433.training.repository.SubmissionRepository;
import vn.edu.ptit.int1433.training.runner.JavaCodeRunner;
import vn.edu.ptit.int1433.training.runner.RunnerResult;

@Service
public class SubmissionService {
    private static final Logger log = LoggerFactory.getLogger(SubmissionService.class);

    private final ExerciseRepository exerciseRepository;
    private final SubmissionRepository submissionRepository;
    private final JavaCodeRunner runner;

    public SubmissionService(ExerciseRepository exerciseRepository, SubmissionRepository submissionRepository, JavaCodeRunner runner) {
        this.exerciseRepository = exerciseRepository;
        this.submissionRepository = submissionRepository;
        this.runner = runner;
    }

    @Transactional
    public SubmissionResponse submitCode(String exerciseId, UUID participantId, String language, String sourceCode) {
        Exercise exercise = exerciseRepository.findDetailById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        if (!EvaluationMode.JAVA_CODE.name().equals(exercise.getEvaluationMode())) {
            throw new InvalidFilterException("Exercise " + exerciseId + " does not accept Java code submissions");
        }
        if (!"JAVA".equals(language)) {
            throw new InvalidFilterException("Only JAVA submissions are supported");
        }
        Submission submission = Submission.createCode(participantId, exercise, language, sourceCode);
        submissionRepository.saveAndFlush(submission);
        log.info("submission created id={} exercise={} mode=JAVA_CODE", submission.getId(), exerciseId);

        RunnerResult result = runner.judge(exercise, sourceCode);
        List<SubmissionTestResult> tests = result.tests().stream()
            .map(test -> SubmissionTestResult.of(test.testIndex(), test.verdict(), test.executionTimeMs(), test.diagnosticCode(), test.publicMessage()))
            .toList();
        BigDecimal score = result.verdict() == Verdict.AC ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        submission.markJudged(result.verdict(), score, result.diagnosticCode(), result.publicMessage(), result.compileOutput(), result.runtimeOutput(), tests);
        log.info("submission judged id={} verdict={}", submission.getId(), result.verdict());
        return toResponse(submissionRepository.save(submission));
    }

    @Transactional(readOnly = true)
    public SubmissionResponse get(UUID id, UUID participantId) {
        return submissionRepository.findByIdAndParticipantId(id, participantId)
            .map(this::toResponse)
            .orElseThrow(() -> new SubmissionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> history(UUID participantId) {
        return submissionRepository.findTop20ByParticipantIdOrderByCreatedAtDesc(participantId).stream()
            .map(this::toResponse)
            .toList();
    }

    private SubmissionResponse toResponse(Submission submission) {
        return new SubmissionResponse(
            submission.getId(),
            submission.getExercise().getId(),
            submission.getEvaluationMode().name(),
            submission.getStatus().name(),
            submission.getVerdict().name(),
            submission.getScore(),
            submission.getDiagnosticCode(),
            submission.getPublicMessage(),
            submission.getCompileOutput(),
            submission.getRuntimeOutput(),
            submission.getCreatedAt(),
            submission.getJudgedAt(),
            submission.getTestResults().stream()
                .map(test -> new SubmissionTestResultResponse(
                    test.getTestIndex(),
                    test.getVerdict().name(),
                    test.getExecutionTimeMs(),
                    test.getMemoryKb(),
                    test.getDiagnosticCode(),
                    test.getPublicMessage()
                ))
                .toList()
        );
    }
}
