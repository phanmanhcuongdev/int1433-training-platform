package vn.edu.ptit.int1433.training.controller;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.ptit.int1433.training.dto.ChallengeSessionResponse;
import vn.edu.ptit.int1433.training.service.ChallengeSessionService;
import vn.edu.ptit.int1433.training.service.ParticipantService;

@RestController
@RequestMapping("/api/v1/challenge-sessions")
public class ChallengeSessionController {
    private final ChallengeSessionService challengeSessionService;
    private final ParticipantService participantService;

    public ChallengeSessionController(ChallengeSessionService challengeSessionService, ParticipantService participantService) {
        this.challengeSessionService = challengeSessionService;
        this.participantService = participantService;
    }

    @GetMapping("/{id}")
    public ChallengeSessionResponse get(
        @PathVariable UUID id,
        @RequestHeader(ParticipantService.HEADER) String participantHeader
    ) {
        return challengeSessionService.get(id, participantService.parse(participantHeader));
    }
}
