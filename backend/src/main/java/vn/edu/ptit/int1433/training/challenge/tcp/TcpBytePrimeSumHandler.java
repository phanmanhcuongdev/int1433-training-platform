package vn.edu.ptit.int1433.training.challenge.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class TcpBytePrimeSumHandler extends AbstractTcpChallengeHandler {
    public TcpBytePrimeSumHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
    }

    @Override
    public String graderKey() {
        return "net.tcp.byte_prime_sum.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String receivedToken = readFramedString(in);
        String receivedQCode = readFramedString(in);
        if (!token.equals(receivedToken) || !session.getQCode().equals(receivedQCode)) {
            protocol(session.getId(), "BAD_TOKEN_OR_QCODE", "Token hoặc qCode không hợp lệ.");
            return;
        }
        acceptRequest(session.getId());
        @SuppressWarnings("unchecked")
        List<Integer> values = (List<Integer>) session.getPayload().get("values");
        out.writeInt(values.size());
        out.flush();
        for (Integer value : values) {
            out.writeInt(value);
            out.flush();
            Thread.sleep(15);
        }
        responseSent(session.getId());
        int primeCount = in.readInt();
        long primeSum = in.readLong();
        long expectedCount = ((Number) session.getExpectedAnswer().get("primeCount")).longValue();
        long expectedSum = ((Number) session.getExpectedAnswer().get("primeSum")).longValue();
        if (primeCount == expectedCount && primeSum == expectedSum) {
            ac(session.getId());
        } else {
            wa(session.getId(), "Prime count hoặc prime sum sai.");
        }
    }

    private String readFramedString(DataInputStream in) throws Exception {
        int length = in.readInt();
        if (length < 1 || length > 512) {
            throw new IllegalArgumentException("Invalid string frame length");
        }
        byte[] bytes = in.readNBytes(length);
        if (bytes.length != length) {
            throw new IllegalArgumentException("Short string frame");
        }
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
