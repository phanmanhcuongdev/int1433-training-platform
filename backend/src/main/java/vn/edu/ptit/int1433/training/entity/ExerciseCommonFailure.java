package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "exercise_common_failures")
@IdClass(ExerciseCommonFailure.Key.class)
public class ExerciseCommonFailure {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Id
    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "display_order")
    private Integer displayOrder;

    public Exercise getExercise() { return exercise; }
    public String getFailureCode() { return failureCode; }
    public Integer getDisplayOrder() { return displayOrder; }

    public static class Key implements Serializable {
        private String exercise;
        private String failureCode;

        public Key() {
        }

        public Key(String exercise, String failureCode) {
            this.exercise = exercise;
            this.failureCode = failureCode;
        }

        public String getExercise() { return exercise; }
        public void setExercise(String exercise) { this.exercise = exercise; }
        public String getFailureCode() { return failureCode; }
        public void setFailureCode(String failureCode) { this.failureCode = failureCode; }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Key key)) return false;
            return Objects.equals(exercise, key.exercise) && Objects.equals(failureCode, key.failureCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exercise, failureCode);
        }
    }
}
