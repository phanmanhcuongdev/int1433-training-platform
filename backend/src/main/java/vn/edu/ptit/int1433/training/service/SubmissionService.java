package vn.edu.ptit.int1433.training.service;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.ptit.int1433.training.dto.SubmissionResponse;
import vn.edu.ptit.int1433.training.dto.SubmissionTestResultResponse;
import vn.edu.ptit.int1433.training.entity.EvaluationMode;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.entity.Submission;
import vn.edu.ptit.int1433.training.entity.SubmissionTestResult;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.exception.InvalidFilterException;
import vn.edu.ptit.int1433.training.exception.SubmissionValidationException;
import vn.edu.ptit.int1433.training.exception.SubmissionNotFoundException;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;
import vn.edu.ptit.int1433.training.repository.SubmissionRepository;
import vn.edu.ptit.int1433.training.runner.JavaCodeRunner;
import vn.edu.ptit.int1433.training.runner.JavaSourceSubmission;
import vn.edu.ptit.int1433.training.runner.RunnerResult;

@Service
public class SubmissionService {
    private static final Logger log = LoggerFactory.getLogger(SubmissionService.class);
    private static final int MAX_SOURCE_BYTES = 20 * 1024;

    private final ExerciseRepository exerciseRepository;
    private final SubmissionRepository submissionRepository;
    private final JavaCodeRunner runner;
    private final JavaSubmissionValidator javaSubmissionValidator;

    public SubmissionService(ExerciseRepository exerciseRepository, SubmissionRepository submissionRepository, JavaCodeRunner runner, JavaSubmissionValidator javaSubmissionValidator) {
        this.exerciseRepository = exerciseRepository;
        this.submissionRepository = submissionRepository;
        this.runner = runner;
        this.javaSubmissionValidator = javaSubmissionValidator;
    }

    @Transactional
    public SubmissionResponse submitCode(String exerciseId, UUID participantId, String language, String originalFileName, String sourceCode) {
        return submitCode(exerciseId, participantId, language, sourceCode, originalFileName, true);
    }

    @Transactional
    public SubmissionResponse submitCodeFile(String exerciseId, UUID participantId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new SubmissionValidationException("File không được rỗng.");
        }
        if (file.getSize() > MAX_SOURCE_BYTES) {
            throw new SubmissionValidationException("File vượt quá giới hạn 20 KB.");
        }
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (Exception exception) {
            throw new SubmissionValidationException("Không đọc được file upload.");
        }
        String sourceCode = decodeUtf8(bytes);
        return submitCode(exerciseId, participantId, "JAVA", sourceCode, file.getOriginalFilename(), false);
    }

    private SubmissionResponse submitCode(String exerciseId, UUID participantId, String language, String sourceCode, String originalFileName, boolean allowFilenameInference) {
        Exercise exercise = exerciseRepository.findDetailById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        if (!EvaluationMode.JAVA_CODE.name().equals(exercise.getEvaluationMode())) {
            throw new InvalidFilterException("Exercise " + exerciseId + " does not accept Java code submissions");
        }
        if (!"JAVA".equals(language)) {
            throw new InvalidFilterException("Only JAVA submissions are supported");
        }
        validateSource(sourceCode);
        JavaSourceSubmission javaSource = allowFilenameInference
            ? javaSubmissionValidator.validateOrInferFilename(originalFileName, sourceCode)
            : javaSubmissionValidator.validate(originalFileName, sourceCode);
        Submission submission = Submission.createCode(participantId, exercise, language, sourceCode, javaSource.originalFileName(), javaSource.entryClassName(), sha256(sourceCode));
        submissionRepository.saveAndFlush(submission);
        log.info("submission created id={} exercise={} mode=JAVA_CODE", submission.getId(), exerciseId);

        RunnerResult result = runner.judge(exercise, javaSource);
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
            submission.getOriginalFileName(),
            submission.getEntryClassName(),
            submission.getSourceSha256(),
            submission.getSourceCode(),
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

    private void validateSource(String sourceCode) {
        if (sourceCode == null || sourceCode.isEmpty()) {
            throw new SubmissionValidationException("File không được rỗng.");
        }
        if (sourceCode.getBytes(StandardCharsets.UTF_8).length > MAX_SOURCE_BYTES) {
            throw new SubmissionValidationException("File vượt quá giới hạn 20 KB.");
        }
    }

    private String decodeUtf8(byte[] bytes) {
        try {
            CharBuffer decoded = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes));
            return decoded.toString();
        } catch (CharacterCodingException exception) {
            throw new SubmissionValidationException("File không phải UTF-8 hợp lệ.");
        }
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
