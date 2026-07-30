package vn.edu.ptit.int1433.training.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.entity.ExerciseLevel;
import vn.edu.ptit.int1433.training.entity.ExerciseStatus;
import vn.edu.ptit.int1433.training.entity.SourceLabel;

public interface ExerciseRepository extends JpaRepository<Exercise, String> {
    @Query(
        value = """
            select distinct e from Exercise e left join e.tags t
            where (:qLike is null
                   or lower(e.id) like :qLike
                   or lower(e.title) like :qLike
                   or lower(coalesce(e.summary, '')) like :qLike
                   or lower(t) like :qLike)
              and (:technology is null or e.technology = :technology)
              and (:level is null or e.level = :level)
              and (:sourceLabel is null or e.sourceLabel = :sourceLabel)
              and (:status is null or e.status = :status)
            """,
        countQuery = """
            select count(distinct e) from Exercise e left join e.tags t
            where (:qLike is null
                   or lower(e.id) like :qLike
                   or lower(e.title) like :qLike
                   or lower(coalesce(e.summary, '')) like :qLike
                   or lower(t) like :qLike)
              and (:technology is null or e.technology = :technology)
              and (:level is null or e.level = :level)
              and (:sourceLabel is null or e.sourceLabel = :sourceLabel)
              and (:status is null or e.status = :status)
            """
    )
    Page<Exercise> search(
        @Param("qLike") String qLike,
        @Param("technology") String technology,
        @Param("level") ExerciseLevel level,
        @Param("sourceLabel") SourceLabel sourceLabel,
        @Param("status") ExerciseStatus status,
        Pageable pageable
    );

    @Query("select e from Exercise e where e.id = :id")
    java.util.Optional<Exercise> findDetailById(@Param("id") String id);

    @Query("select distinct e.technology from Exercise e order by e.technology")
    List<String> findDistinctTechnologies();

    @Query("select distinct e.level from Exercise e order by e.level")
    List<ExerciseLevel> findDistinctLevels();

    @Query("select distinct e.sourceLabel from Exercise e order by e.sourceLabel")
    List<SourceLabel> findDistinctSourceLabels();

    @Query("select distinct e.status from Exercise e order by e.status")
    List<ExerciseStatus> findDistinctStatuses();
}
