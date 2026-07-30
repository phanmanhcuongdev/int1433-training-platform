package vn.edu.ptit.int1433.training.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.entity.ChallengeState;

public interface ChallengeSessionRepository extends JpaRepository<ChallengeSession, UUID> {
    Optional<ChallengeSession> findByIdAndParticipantId(UUID id, UUID participantId);
    @Query("select s from ChallengeSession s where s.qCode = :qCode")
    Optional<ChallengeSession> findByQCode(String qCode);
    long countByParticipantIdAndStateInAndExpiresAtAfter(UUID participantId, List<ChallengeState> states, OffsetDateTime now);
}
