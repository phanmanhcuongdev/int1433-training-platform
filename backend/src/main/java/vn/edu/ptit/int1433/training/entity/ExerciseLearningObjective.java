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
@Table(name = "exercise_learning_objectives")
@IdClass(ExerciseLearningObjective.Key.class)
public class ExerciseLearningObjective {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(columnDefinition = "text")
    private String objective;

    @Id
    @Column(name = "display_order")
    private Integer displayOrder;

    public Exercise getExercise() { return exercise; }
    public String getObjective() { return objective; }
    public Integer getDisplayOrder() { return displayOrder; }

    public static class Key implements Serializable {
        private String exercise;
        private Integer displayOrder;

        public Key() {
        }

        public Key(String exercise, Integer displayOrder) {
            this.exercise = exercise;
            this.displayOrder = displayOrder;
        }

        public String getExercise() { return exercise; }
        public void setExercise(String exercise) { this.exercise = exercise; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Key key)) return false;
            return Objects.equals(exercise, key.exercise) && Objects.equals(displayOrder, key.displayOrder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exercise, displayOrder);
        }
    }
}
