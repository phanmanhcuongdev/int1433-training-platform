package vn.edu.ptit.int1433.training.challenge.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class TcpDataGcdLcmHandler extends AbstractTcpChallengeHandler {
    public TcpDataGcdLcmHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
    }

    @Override
    public String graderKey() {
        return "net.tcp.data_gcd_lcm.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String receivedToken = in.readUTF();
        String receivedQCode = in.readUTF();
        if (!token.equals(receivedToken) || !session.getQCode().equals(receivedQCode)) {
            protocol(session.getId(), "BAD_TOKEN_OR_QCODE", "Token hoặc qCode không hợp lệ.");
            return;
        }
        acceptRequest(session.getId());
        int a = ((Number) session.getPayload().get("a")).intValue();
        int b = ((Number) session.getPayload().get("b")).intValue();
        int c = ((Number) session.getPayload().get("c")).intValue();
        int d = ((Number) session.getPayload().get("d")).intValue();
        out.writeInt(a);
        out.writeInt(b);
        out.writeInt(c);
        out.writeInt(d);
        out.flush();
        responseSent(session.getId());
        int gcd = in.readInt();
        long lcm = in.readLong();
        long sum = in.readLong();
        long product = in.readLong();
        if (gcd == ((Number) session.getExpectedAnswer().get("gcd")).intValue()
            && lcm == ((Number) session.getExpectedAnswer().get("lcm")).longValue()
            && sum == ((Number) session.getExpectedAnswer().get("sum")).longValue()
            && product == ((Number) session.getExpectedAnswer().get("product")).longValue()) {
            ac(session.getId());
        } else {
            wa(session.getId(), "GCD/LCM/tổng/tích sai hoặc sai thứ tự giá trị.");
        }
    }
}
