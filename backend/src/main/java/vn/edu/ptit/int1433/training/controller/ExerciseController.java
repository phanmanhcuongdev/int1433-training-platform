package vn.edu.ptit.int1433.training.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.ptit.int1433.training.dto.CodeSubmissionRequest;
import vn.edu.ptit.int1433.training.dto.ChallengeSessionResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseDetailResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseEvaluationResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseFilterResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseSummaryResponse;
import vn.edu.ptit.int1433.training.dto.PaginatedResponse;
import vn.edu.ptit.int1433.training.dto.SubmissionResponse;
import vn.edu.ptit.int1433.training.service.ChallengeSessionService;
import vn.edu.ptit.int1433.training.service.ExerciseService;
import vn.edu.ptit.int1433.training.service.ParticipantService;
import vn.edu.ptit.int1433.training.service.StarterAssetService;
import vn.edu.ptit.int1433.training.service.SubmissionService;

@Validated
@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {
    private final ExerciseService service;
    private final SubmissionService submissionService;
    private final ChallengeSessionService challengeSessionService;
    private final ParticipantService participantService;
    private final StarterAssetService starterAssetService;

    public ExerciseController(
        ExerciseService service,
        SubmissionService submissionService,
        ChallengeSessionService challengeSessionService,
        ParticipantService participantService,
        StarterAssetService starterAssetService
    ) {
        this.service = service;
        this.submissionService = submissionService;
        this.challengeSessionService = challengeSessionService;
        this.participantService = participantService;
        this.starterAssetService = starterAssetService;
    }

    @GetMapping
    public PaginatedResponse<ExerciseSummaryResponse> list(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String technology,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) String sourceLabel,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
        @RequestParam(defaultValue = "displayOrder,id") String sort
    ) {
        return service.list(q, technology, level, sourceLabel, status, page, size, sort);
    }

    @GetMapping("/filters")
    public ExerciseFilterResponse filters() {
        return service.filters();
    }

    @GetMapping("/{id}/evaluation")
    public ExerciseEvaluationResponse evaluation(@PathVariable String id) {
        return service.getEvaluation(id);
    }

    @GetMapping("/{id}/starter")
    public ResponseEntity<byte[]> starter(@PathVariable String id) {
        var archive = starterAssetService.archive(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archive.filename() + "\"")
            .contentType(MediaType.parseMediaType("application/zip"))
            .body(archive.bytes());
    }

    @PostMapping("/{id}/code-submissions")
    public SubmissionResponse submitCode(
        @PathVariable String id,
        @RequestHeader(ParticipantService.HEADER) String participantHeader,
        @Valid @RequestBody CodeSubmissionRequest request
    ) {
        return submissionService.submitCode(id, participantService.parse(participantHeader), request.language(), request.originalFileName(), request.sourceCode());
    }

    @PostMapping(path = "/{id}/submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubmissionResponse submitCodeFile(
        @PathVariable String id,
        @RequestHeader(ParticipantService.HEADER) String participantHeader,
        @RequestParam("file") MultipartFile file
    ) {
        return submissionService.submitCodeFile(id, participantService.parse(participantHeader), file);
    }

    @PostMapping("/{id}/challenge-sessions")
    public ChallengeSessionResponse startChallenge(
        @PathVariable String id,
        @RequestHeader(ParticipantService.HEADER) String participantHeader
    ) {
        return challengeSessionService.start(id, participantService.parse(participantHeader));
    }

    @GetMapping("/{id}")
    public ExerciseDetailResponse detail(@PathVariable String id) {
        return service.getById(id);
    }
}
