package vn.edu.ptit.int1433.training.challenge.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class TcpCharacterNormalizeHandler extends AbstractTcpChallengeHandler {
    public TcpCharacterNormalizeHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
    }

    @Override
    public String graderKey() {
        return "net.tcp.character_normalize.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, Socket socket) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        String request = in.readLine();
        if (!("%s;%s".formatted(token, session.getQCode())).equals(request)) {
            protocol(session.getId(), "BAD_REQUEST_LINE", "Request line phải là token;qCode.");
            return;
        }
        acceptRequest(session.getId());
        out.write(String.valueOf(session.getPayload().get("text")));
        out.newLine();
        out.flush();
        responseSent(session.getId());
        String answer = in.readLine();
        if (answer == null) {
            protocol(session.getId(), "MISSING_SUBMISSION_LINE", "Không nhận được submission line.");
            return;
        }
        if (String.valueOf(session.getExpectedAnswer().get("answer")).equals(answer)) {
            ac(session.getId());
        } else {
            wa(session.getId(), "Chuẩn hóa từ chưa đúng.");
        }
    }
}
