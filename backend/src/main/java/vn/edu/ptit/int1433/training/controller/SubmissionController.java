package vn.edu.ptit.int1433.training.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.ptit.int1433.training.dto.SubmissionResponse;
import vn.edu.ptit.int1433.training.service.ParticipantService;
import vn.edu.ptit.int1433.training.service.SubmissionService;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final ParticipantService participantService;

    public SubmissionController(SubmissionService submissionService, ParticipantService participantService) {
        this.submissionService = submissionService;
        this.participantService = participantService;
    }

    @GetMapping("/{id}")
    public SubmissionResponse get(@PathVariable UUID id, @RequestHeader(ParticipantService.HEADER) String participantHeader) {
        return submissionService.get(id, participantService.parse(participantHeader));
    }

    @GetMapping
    public List<SubmissionResponse> history(@RequestHeader(ParticipantService.HEADER) String participantHeader) {
        return submissionService.history(participantService.parse(participantHeader));
    }
}
