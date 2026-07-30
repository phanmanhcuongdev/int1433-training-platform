package vn.edu.ptit.int1433.training.challenge.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import vn.edu.ptit.int1433.training.challenge.ChallengeHandler;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

abstract class AbstractTcpChallengeHandler implements ChallengeHandler {
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    protected final PortLeaseManager portLeaseManager;
    protected final ChallengeProperties properties;
    protected final ChallengeResultService resultService;

    protected AbstractTcpChallengeHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        this.portLeaseManager = portLeaseManager;
        this.properties = properties;
        this.resultService = resultService;
    }

    @Override
    public void start(ChallengeSession session, String plaintextToken) {
        PortLeaseManager.Lease lease = portLeaseManager.leaseTcp();
        session.setPortMetadata(lease.port());
        executor.submit(() -> runSession(session, plaintextToken, lease));
    }

    private void runSession(ChallengeSession session, String token, PortLeaseManager.Lease lease) {
        UUID sessionId = session.getId();
        try (lease; ServerSocket server = new ServerSocket(lease.port(), 1, InetAddress.getByName(properties.bindAddress()))) {
            server.setSoTimeout(600_000);
            resultService.trace(sessionId, ChallengeState.ACTIVE.name(), "TCP listener đang chờ client.");
            try (Socket socket = server.accept()) {
                socket.setSoTimeout(5_000);
                resultService.trace(sessionId, ChallengeState.CONNECTED.name(), "Client đã kết nối TCP.");
                handle(session, token, socket);
            }
        } catch (java.net.SocketTimeoutException exception) {
            resultService.verdict(sessionId, ChallengeState.TIMEOUT, Verdict.TLE, "TCP_TIMEOUT", "Không hoàn tất TCP challenge trước timeout.");
        } catch (Exception exception) {
            resultService.verdict(sessionId, ChallengeState.INTERNAL_ERROR, Verdict.INTERNAL_ERROR, "TCP_INTERNAL_ERROR", "TCP handler gặp lỗi nội bộ.");
        }
    }

    protected void acceptRequest(UUID sessionId) {
        resultService.trace(sessionId, ChallengeState.REQUEST_ACCEPTED.name(), "Request token/qCode hợp lệ.");
    }

    protected void responseSent(UUID sessionId) {
        resultService.trace(sessionId, ChallengeState.RESPONSE_SENT.name(), "Server đã gửi payload.");
    }

    protected void ac(UUID sessionId) {
        resultService.verdict(sessionId, ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
    }

    protected void wa(UUID sessionId, String message) {
        resultService.verdict(sessionId, ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", message);
    }

    protected void protocol(UUID sessionId, String code, String message) {
        resultService.verdict(sessionId, ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, code, message);
    }

    protected boolean expired(ChallengeSession session) {
        return OffsetDateTime.now().isAfter(session.getExpiresAt());
    }

    protected abstract void handle(ChallengeSession session, String token, Socket socket) throws Exception;

    protected boolean isPrime(int value) {
        if (value < 2) return false;
        for (int i = 2; i * i <= value; i += 1) if (value % i == 0) return false;
        return true;
    }

    protected int gcd(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int t = x % y;
            x = y;
            y = t;
        }
        return x;
    }
}
