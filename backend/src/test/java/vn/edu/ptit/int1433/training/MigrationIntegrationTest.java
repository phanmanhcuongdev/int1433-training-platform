package vn.edu.ptit.int1433.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class MigrationIntegrationTest extends AbstractPostgresIntegrationTest {
    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void flywayCreatesSchemaAndSeedsTenExercises() {
        assertThat(flyway.info().applied()).hasSizeGreaterThanOrEqualTo(4);

        Integer exerciseCount = jdbcTemplate.queryForObject("select count(*) from exercises", Integer.class);
        Integer tagCount = jdbcTemplate.queryForObject("select count(*) from exercise_tags", Integer.class);

        assertThat(exerciseCount).isEqualTo(10);
        assertThat(tagCount).isGreaterThanOrEqualTo(40);
    }
}
