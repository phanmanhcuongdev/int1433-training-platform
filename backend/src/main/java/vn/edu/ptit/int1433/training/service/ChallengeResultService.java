package vn.edu.ptit.int1433.training.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.repository.ChallengeSessionRepository;

@Service
public class ChallengeResultService {
    private static final Logger log = LoggerFactory.getLogger(ChallengeResultService.class);
    private final ChallengeSessionRepository repository;

    public ChallengeResultService(ChallengeSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void trace(UUID sessionId, String state, String message) {
        repository.findById(sessionId).ifPresent(session -> {
            session.appendTrace(state, message);
            if (state != null) {
                try {
                    session.mark(ChallengeState.valueOf(state), session.getVerdict(), session.getDiagnosticCode(), session.getPublicMessage());
                } catch (IllegalArgumentException ignored) {
                    // Trace-only states are kept in the protocol log.
                }
            }
            log.info("challenge state transition session={} state={}", sessionId, state);
        });
    }

    @Transactional
    public void verdict(UUID sessionId, ChallengeState state, Verdict verdict, String diagnosticCode, String publicMessage) {
        repository.findById(sessionId).ifPresent(session -> {
            session.mark(state, verdict, diagnosticCode, publicMessage);
            session.appendTrace(state.name(), publicMessage);
            log.info("challenge verdict assigned session={} verdict={}", sessionId, verdict);
        });
    }
}
