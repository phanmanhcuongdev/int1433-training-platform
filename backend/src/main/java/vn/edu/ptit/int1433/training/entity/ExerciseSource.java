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
@Table(name = "exercise_sources")
public class ExerciseSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "claim_id")
    private String claimId;

    @Column(name = "source_file", columnDefinition = "text")
    private String sourceFile;

    @Column(name = "evidence_note", columnDefinition = "text")
    private String evidenceNote;

    public Long getId() { return id; }
    public Exercise getExercise() { return exercise; }
    public String getClaimId() { return claimId; }
    public String getSourceFile() { return sourceFile; }
    public String getEvidenceNote() { return evidenceNote; }
}
