package vn.edu.ptit.int1433.training.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.ptit.int1433.training.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    Optional<Submission> findByIdAndParticipantId(UUID id, UUID participantId);
    List<Submission> findTop20ByParticipantIdOrderByCreatedAtDesc(UUID participantId);
}
