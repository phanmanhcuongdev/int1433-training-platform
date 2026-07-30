package vn.edu.ptit.int1433.training.challenge.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class UdpStringRequestIdHandler extends AbstractUdpChallengeHandler {
    private final ChallengeResultService resultService;

    public UdpStringRequestIdHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
        this.resultService = resultService;
    }

    @Override
    public String graderKey() {
        return "net.udp.string_request_id.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, DatagramSocket socket) throws Exception {
        DatagramPacket request = receive(socket, 2048);
        String requestText = text(request);
        if (!("%s;%s".formatted(token, session.getQCode())).equals(requestText)) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_REQUEST", "Request UDP phải là token;qCode.");
            return;
        }
        resultService.trace(session.getId(), ChallengeState.REQUEST_ACCEPTED.name(), "Request UDP hợp lệ.");
        String responseText = session.getRequestId() + ";" + session.getPayload().get("payload");
        byte[] response = responseText.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(response, response.length, request.getAddress(), request.getPort()));
        resultService.trace(session.getId(), ChallengeState.RESPONSE_SENT.name(), "Đã gửi requestId và payload.");
        DatagramPacket submission = receive(socket, 2048);
        String submitted = text(submission);
        String expectedPrefix = session.getRequestId() + ";";
        if (!submitted.startsWith(expectedPrefix)) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_REQUEST_ID", "Submission thiếu hoặc sai requestId.");
            return;
        }
        String answer = submitted.substring(expectedPrefix.length());
        if (String.valueOf(session.getExpectedAnswer().get("answer")).equals(answer)) {
            resultService.verdict(session.getId(), ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
        } else {
            resultService.verdict(session.getId(), ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", "requestId đúng nhưng answer sai.");
        }
    }
}
