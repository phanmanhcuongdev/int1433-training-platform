package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercise_hints")
public class ExerciseHint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "display_order")
    private Integer displayOrder;

    public Long getId() { return id; }
    public Exercise getExercise() { return exercise; }
    public String getContent() { return content; }
    public Integer getDisplayOrder() { return displayOrder; }
}
