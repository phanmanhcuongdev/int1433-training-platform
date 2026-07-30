package vn.edu.ptit.int1433.training.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.edu.ptit.int1433.training.AbstractPostgresIntegrationTest;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;

class ExerciseServiceIntegrationTest extends AbstractPostgresIntegrationTest {
    @Autowired
    private ExerciseRepository repository;

    @Autowired
    private ExerciseService service;

    @Test
    void listsThreeSeedExercises() {
        var page = service.list(null, null, null, null, null, 0, 20, "displayOrder,id");

        assertThat(repository.count()).isEqualTo(3);
        assertThat(page.totalItems()).isEqualTo(3);
        assertThat(page.items()).extracting("id").containsExactly(
            "fnd-character-flush-001",
            "tcp-character-001",
            "udp-string-request-id-001"
        );
    }

    @Test
    void retrievesDetailById() {
        var detail = service.getById("tcp-character-001");

        assertThat(detail.id()).isEqualTo("tcp-character-001");
        assertThat(detail.tags()).contains("tcp", "character-stream");
        assertThat(detail.commonFailures()).contains("missing_flush");
        assertThat(detail.serverContract()).containsEntry("host", "<HOST>");
    }

    @Test
    void unknownIdFailsCorrectly() {
        assertThatThrownBy(() -> service.getById("not-found"))
            .isInstanceOf(ExerciseNotFoundException.class)
            .hasMessageContaining("not-found");
    }
}
