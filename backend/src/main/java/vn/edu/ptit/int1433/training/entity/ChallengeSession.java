package vn.edu.ptit.int1433.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "challenge_sessions")
public class ChallengeSession {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "participant_id")
    private UUID participantId;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "q_code")
    private String qCode;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "host_metadata")
    private String hostMetadata;

    @Column(name = "port_metadata")
    private Integer portMetadata;

    @Column(name = "endpoint_metadata")
    private String endpointMetadata;

    @Enumerated(EnumType.STRING)
    private ChallengeState state;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    @Column(name = "diagnostic_code")
    private String diagnosticCode;

    @Column(name = "public_message")
    private String publicMessage;

    private Long seed;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "expected_answer", columnDefinition = "jsonb")
    private Map<String, Object> expectedAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "protocol_trace", columnDefinition = "jsonb")
    private List<Map<String, Object>> protocolTrace;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    public static ChallengeSession create(
        Exercise exercise,
        UUID participantId,
        String tokenHash,
        String qCode,
        String requestId,
        String host,
        Integer port,
        String endpoint,
        long seed,
        Map<String, Object> payload,
        Map<String, Object> expectedAnswer,
        int ttlSeconds
    ) {
        ChallengeSession session = new ChallengeSession();
        session.id = UUID.randomUUID();
        session.exercise = exercise;
        session.participantId = participantId;
        session.tokenHash = tokenHash;
        session.qCode = qCode;
        session.requestId = requestId;
        session.hostMetadata = host;
        session.portMetadata = port;
        session.endpointMetadata = endpoint;
        session.state = ChallengeState.ACTIVE;
        session.verdict = Verdict.PENDING;
        session.seed = seed;
        session.payload = payload;
        session.expectedAnswer = expectedAnswer;
        session.protocolTrace = List.of(Map.of("state", "ACTIVE", "message", "Phiên đã được tạo."));
        session.createdAt = OffsetDateTime.now();
        session.expiresAt = session.createdAt.plusSeconds(ttlSeconds);
        return session;
    }

    public UUID getId() { return id; }
    public Exercise getExercise() { return exercise; }
    public UUID getParticipantId() { return participantId; }
    public String getTokenHash() { return tokenHash; }
    public String getQCode() { return qCode; }
    public String getRequestId() { return requestId; }
    public String getHostMetadata() { return hostMetadata; }
    public Integer getPortMetadata() { return portMetadata; }
    public String getEndpointMetadata() { return endpointMetadata; }
    public ChallengeState getState() { return state; }
    public Verdict getVerdict() { return verdict; }
    public String getDiagnosticCode() { return diagnosticCode; }
    public String getPublicMessage() { return publicMessage; }
    public Map<String, Object> getPayload() { return payload; }
    public Map<String, Object> getExpectedAnswer() { return expectedAnswer; }
    public List<Map<String, Object>> getProtocolTrace() { return protocolTrace; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }

    public void mark(ChallengeState state, Verdict verdict, String diagnosticCode, String publicMessage) {
        this.state = state;
        this.verdict = verdict;
        this.diagnosticCode = diagnosticCode;
        this.publicMessage = publicMessage;
        if (verdict != Verdict.PENDING && verdict != Verdict.RUNNING) {
            this.completedAt = OffsetDateTime.now();
        }
    }

    public void setPortMetadata(Integer portMetadata) {
        this.portMetadata = portMetadata;
    }

    public void setEndpointMetadata(String endpointMetadata) {
        this.endpointMetadata = endpointMetadata;
    }

    public void appendTrace(String state, String message) {
        var next = new java.util.ArrayList<Map<String, Object>>(protocolTrace == null ? List.of() : protocolTrace);
        if (next.size() >= 40) {
            next.remove(0);
        }
        next.add(Map.of("at", OffsetDateTime.now().toString(), "state", state, "message", message));
        protocolTrace = next;
    }
}
