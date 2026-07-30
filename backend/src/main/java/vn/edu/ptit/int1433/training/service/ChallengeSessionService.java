package vn.edu.ptit.int1433.training.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.ChallengeHandlerRegistry;
import vn.edu.ptit.int1433.training.dto.ChallengeSessionResponse;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.EvaluationMode;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.exception.ChallengeSessionNotFoundException;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.exception.InvalidFilterException;
import vn.edu.ptit.int1433.training.repository.ChallengeSessionRepository;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;

@Service
public class ChallengeSessionService {
    private static final Logger log = LoggerFactory.getLogger(ChallengeSessionService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final ExerciseRepository exerciseRepository;
    private final ChallengeSessionRepository repository;
    private final ChallengeProperties properties;
    private final ChallengeHandlerRegistry handlerRegistry;

    public ChallengeSessionService(
        ExerciseRepository exerciseRepository,
        ChallengeSessionRepository repository,
        ChallengeProperties properties,
        ChallengeHandlerRegistry handlerRegistry
    ) {
        this.exerciseRepository = exerciseRepository;
        this.repository = repository;
        this.properties = properties;
        this.handlerRegistry = handlerRegistry;
    }

    @Transactional
    public ChallengeSessionResponse start(String exerciseId, UUID participantId) {
        Exercise exercise = exerciseRepository.findDetailById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        if (!EvaluationMode.NETWORK_CHALLENGE.name().equals(exercise.getEvaluationMode())) {
            throw new InvalidFilterException("Exercise " + exerciseId + " does not use network challenge sessions");
        }
        long active = repository.countByParticipantIdAndStateInAndExpiresAtAfter(
            participantId,
            List.of(ChallengeState.ACTIVE, ChallengeState.CONNECTED, ChallengeState.REQUEST_ACCEPTED, ChallengeState.RESPONSE_SENT),
            OffsetDateTime.now()
        );
        if (active >= properties.maxActiveSessionsPerParticipant()) {
            throw new InvalidFilterException("Too many active challenge sessions for this participant");
        }

        String token = randomToken();
        String qCode = "Q" + Math.abs(UUID.randomUUID().hashCode());
        long seed = System.nanoTime() ^ participantId.getLeastSignificantBits();
        GeneratedChallenge generated = generate(exercise, seed);
        int ttl = exercise.getNetworkSessionTtlSeconds() == null ? 600 : exercise.getNetworkSessionTtlSeconds();
        ChallengeSession session = ChallengeSession.create(
            exercise,
            participantId,
            sha256(token),
            qCode,
            generated.requestId(),
            properties.publicHost(),
            null,
            generated.endpoint(),
            seed,
            generated.payload(),
            generated.expected(),
            ttl
        );
        repository.saveAndFlush(session);
        handlerRegistry.get(exercise.getGraderKey()).start(session, token);
        repository.saveAndFlush(session);
        log.info("challenge session created id={} exercise={} participant={}", session.getId(), exerciseId, participantId);
        return toStartResponse(session, token);
    }

    @Transactional(readOnly = true)
    public ChallengeSessionResponse get(UUID sessionId, UUID participantId) {
        return repository.findByIdAndParticipantId(sessionId, participantId)
            .map(session -> toStartResponse(session, null))
            .orElseThrow(() -> new ChallengeSessionNotFoundException(sessionId));
    }

    private GeneratedChallenge generate(Exercise exercise, long seed) {
        Random random = new Random(seed);
        String requestId = "RID" + Integer.toUnsignedString(random.nextInt(), 36);
        return switch (exercise.getId()) {
            case "tcp-byte-prime-sum-001" -> {
                List<Integer> values = random.ints(12, 0, 200).boxed().toList();
                int count = (int) values.stream().filter(this::isPrime).count();
                int sum = values.stream().filter(this::isPrime).mapToInt(Integer::intValue).sum();
                yield new GeneratedChallenge(requestId, Map.of("values", values), Map.of("primeCount", count, "primeSum", sum), null);
            }
            case "tcp-data-gcd-lcm-001" -> {
                int a = random.nextInt(99) + 2;
                int b = random.nextInt(99) + 2;
                int c = random.nextInt(20) + 1;
                int d = random.nextInt(20) + 1;
                int gcd = gcd(a, b);
                yield new GeneratedChallenge(requestId, Map.of("a", a, "b", b, "c", c, "d", d), Map.of("gcd", gcd, "lcm", (long) a * b / gcd, "sum", a + b + c + d, "product", (long) a * b * c * d), null);
            }
            case "tcp-character-normalize-001" -> new GeneratedChallenge(requestId, Map.of("text", "lẬp   TRÌNH   mẠng"), Map.of("answer", "Lập Trình Mạng"), null);
            case "tcp-object-product-001" -> new GeneratedChallenge(requestId, Map.of("name", "  usb   cable ", "price", 12.345d), Map.of("name", "Usb Cable", "price", 12.35d, "normalized", true), null);
            case "udp-string-request-id-001" -> new GeneratedChallenge(requestId, Map.of("payload", "lập trình"), Map.of("requestId", requestId, "answer", "hnìrt pậl"), null);
            case "udp-object-product-001" -> new GeneratedChallenge(requestId, Map.of("name", "usb hub", "quantity", 8), Map.of("requestId", requestId, "name", "Usb Hub", "quantity", 4, "normalized", true), null);
            case "rmi-data-pythagorean-001" -> new GeneratedChallenge(requestId, Map.of("values", List.of(3, 4, 5, 6, 8, 10)), Map.of("triples", List.of(List.of(3, 4, 5))), null);
            case "ws-data-factorization-001" -> new GeneratedChallenge(requestId, Map.of("n", 360), Map.of("factors", List.of(2, 2, 2, 3, 3, 5)), "/ws");
            default -> new GeneratedChallenge(requestId, Map.of(), Map.of(), null);
        };
    }

    private ChallengeSessionResponse toStartResponse(ChallengeSession session, String token) {
        return new ChallengeSessionResponse(
            session.getId(),
            session.getExercise().getId(),
            token,
            session.getQCode(),
            session.getHostMetadata(),
            session.getPortMetadata(),
            session.getEndpointMetadata(),
            session.getExpiresAt(),
            session.getState().name(),
            session.getVerdict().name(),
            session.getDiagnosticCode(),
            session.getPublicMessage(),
            instructions(session),
            session.getProtocolTrace()
        );
    }

    private List<String> instructions(ChallengeSession session) {
        return List.of(
            "Token chỉ hiển thị khi tạo phiên; backend chỉ lưu hash.",
            "Phiên phải được hoàn thành trước " + session.getExpiresAt() + ".",
            "Platform tự động ghi nhận verdict khi client hoàn tất đúng protocol."
        );
    }

    private String randomToken() {
        byte[] bytes = new byte[18];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private boolean isPrime(int value) {
        if (value < 2) return false;
        for (int i = 2; i * i <= value; i += 1) {
            if (value % i == 0) return false;
        }
        return true;
    }

    private int gcd(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int t = x % y;
            x = y;
            y = t;
        }
        return x;
    }

    private record GeneratedChallenge(String requestId, Map<String, Object> payload, Map<String, Object> expected, String endpoint) {}
}
