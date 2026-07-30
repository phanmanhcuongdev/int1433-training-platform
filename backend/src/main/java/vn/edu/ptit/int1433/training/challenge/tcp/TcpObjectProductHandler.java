package vn.edu.ptit.int1433.training.challenge.tcp;

import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.contract.Product;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class TcpObjectProductHandler extends AbstractTcpChallengeHandler {
    public TcpObjectProductHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
    }

    @Override
    public String graderKey() {
        return "net.tcp.object_product.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, Socket socket) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        in.setObjectInputFilter(allowProduct());
        String receivedToken = in.readUTF();
        String receivedQCode = in.readUTF();
        if (!token.equals(receivedToken) || !session.getQCode().equals(receivedQCode)) {
            protocol(session.getId(), "BAD_TOKEN_OR_QCODE", "Token hoặc qCode không hợp lệ.");
            return;
        }
        acceptRequest(session.getId());
        Product product = new Product("P1", String.valueOf(session.getPayload().get("name")), ((Number) session.getPayload().get("price")).doubleValue(), 0, false);
        out.writeObject(product);
        out.flush();
        responseSent(session.getId());
        Object submitted = in.readObject();
        if (!(submitted instanceof Product result)) {
            protocol(session.getId(), "WRONG_OBJECT_TYPE", "Submission phải là Product đúng package.");
            return;
        }
        String expectedName = String.valueOf(session.getExpectedAnswer().get("name"));
        double expectedPrice = ((Number) session.getExpectedAnswer().get("price")).doubleValue();
        if (expectedName.equals(result.getName()) && Math.abs(expectedPrice - result.getPrice()) < 0.0001 && result.isNormalized()) {
            ac(session.getId());
        } else {
            wa(session.getId(), "Product đúng kiểu nhưng dữ liệu chuẩn hóa sai.");
        }
    }

    private ObjectInputFilter allowProduct() {
        return info -> {
            Class<?> serialClass = info.serialClass();
            if (serialClass == null) return ObjectInputFilter.Status.UNDECIDED;
            if (serialClass == String.class || serialClass == Product.class || serialClass.isArray() && serialClass.componentType() == byte.class) {
                return ObjectInputFilter.Status.ALLOWED;
            }
            if (serialClass.isPrimitive()) return ObjectInputFilter.Status.ALLOWED;
            return ObjectInputFilter.Status.REJECTED;
        };
    }
}
