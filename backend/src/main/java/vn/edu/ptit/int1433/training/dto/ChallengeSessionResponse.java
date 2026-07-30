package vn.edu.ptit.int1433.training.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChallengeSessionResponse(
    UUID sessionId,
    String exerciseId,
    String token,
    String qCode,
    String host,
    Integer port,
    String endpoint,
    OffsetDateTime expiresAt,
    String state,
    String verdict,
    String diagnosticCode,
    String publicMessage,
    List<String> instructions,
    List<Map<String, Object>> protocolTrace
) {
}
