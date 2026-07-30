package vn.edu.ptit.int1433.training.challenge.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
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

abstract class AbstractUdpChallengeHandler implements ChallengeHandler {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    protected final PortLeaseManager portLeaseManager;
    protected final ChallengeProperties properties;
    protected final ChallengeResultService resultService;

    protected AbstractUdpChallengeHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        this.portLeaseManager = portLeaseManager;
        this.properties = properties;
        this.resultService = resultService;
    }

    @Override
    public void start(ChallengeSession session, String plaintextToken) {
        PortLeaseManager.Lease lease = portLeaseManager.leaseUdp();
        session.setPortMetadata(lease.port());
        executor.submit(() -> run(session, plaintextToken, lease));
    }

    private void run(ChallengeSession session, String token, PortLeaseManager.Lease lease) {
        UUID sessionId = session.getId();
        try (lease; DatagramSocket socket = new DatagramSocket(lease.port(), InetAddress.getByName(properties.bindAddress()))) {
            socket.setSoTimeout(5_000);
            resultService.trace(sessionId, ChallengeState.ACTIVE.name(), "UDP listener đang chờ datagram.");
            handle(session, token, socket);
        } catch (java.net.SocketTimeoutException exception) {
            resultService.verdict(sessionId, ChallengeState.TIMEOUT, Verdict.TLE, "UDP_TIMEOUT", "Không nhận được UDP datagram trước timeout.");
        } catch (Exception exception) {
            resultService.verdict(sessionId, ChallengeState.INTERNAL_ERROR, Verdict.INTERNAL_ERROR, "UDP_INTERNAL_ERROR", "UDP handler gặp lỗi nội bộ.");
        }
    }

    protected abstract void handle(ChallengeSession session, String token, DatagramSocket socket) throws Exception;

    protected DatagramPacket receive(DatagramSocket socket, int maxBytes) throws Exception {
        byte[] buffer = new byte[maxBytes];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }

    protected String text(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }
}
