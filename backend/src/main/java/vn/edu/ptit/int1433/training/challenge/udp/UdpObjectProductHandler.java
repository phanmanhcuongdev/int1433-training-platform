package vn.edu.ptit.int1433.training.challenge.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.challenge.PortLeaseManager;
import vn.edu.ptit.int1433.training.contract.Product;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;
import vn.edu.ptit.int1433.training.entity.ChallengeState;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.service.ChallengeResultService;

@Component
public class UdpObjectProductHandler extends AbstractUdpChallengeHandler {
    private final ChallengeResultService resultService;

    public UdpObjectProductHandler(PortLeaseManager portLeaseManager, ChallengeProperties properties, ChallengeResultService resultService) {
        super(portLeaseManager, properties, resultService);
        this.resultService = resultService;
    }

    @Override
    public String graderKey() {
        return "net.udp.object_product.v1";
    }

    @Override
    protected void handle(ChallengeSession session, String token, DatagramSocket socket) throws Exception {
        DatagramPacket request = receive(socket, 2048);
        if (!("%s;%s".formatted(token, session.getQCode())).equals(text(request))) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_REQUEST", "Request UDP phải là token;qCode.");
            return;
        }
        resultService.trace(session.getId(), ChallengeState.REQUEST_ACCEPTED.name(), "Request UDP object hợp lệ.");
        byte[] requestIdBytes = fixedRequestId(session.getRequestId());
        Product product = new Product("P2", String.valueOf(session.getPayload().get("name")), 0, ((Number) session.getPayload().get("quantity")).intValue(), false);
        byte[] objectBytes = serialize(product);
        byte[] response = new byte[requestIdBytes.length + objectBytes.length];
        System.arraycopy(requestIdBytes, 0, response, 0, requestIdBytes.length);
        System.arraycopy(objectBytes, 0, response, requestIdBytes.length, objectBytes.length);
        socket.send(new DatagramPacket(response, response.length, request.getAddress(), request.getPort()));
        resultService.trace(session.getId(), ChallengeState.RESPONSE_SENT.name(), "Đã gửi requestId bytes và Product.");

        DatagramPacket submission = receive(socket, 4096);
        if (submission.getLength() <= 8) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "TRUNCATED_DATAGRAM", "Datagram submission quá ngắn.");
            return;
        }
        byte[] submitted = Arrays.copyOfRange(submission.getData(), submission.getOffset(), submission.getOffset() + submission.getLength());
        if (!Arrays.equals(requestIdBytes, Arrays.copyOfRange(submitted, 0, 8))) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "BAD_REQUEST_ID", "8 byte requestId không được giữ nguyên.");
            return;
        }
        Object object = deserialize(Arrays.copyOfRange(submitted, 8, submitted.length));
        if (!(object instanceof Product result)) {
            resultService.verdict(session.getId(), ChallengeState.PROTOCOL_ERROR, Verdict.PROTOCOL_ERROR, "WRONG_OBJECT_TYPE", "Object không phải Product.");
            return;
        }
        if (String.valueOf(session.getExpectedAnswer().get("name")).equals(result.getName())
            && ((Number) session.getExpectedAnswer().get("quantity")).intValue() == result.getQuantity()
            && result.isNormalized()) {
            resultService.verdict(session.getId(), ChallengeState.AC, Verdict.AC, "AC", "Kết quả đúng.");
        } else {
            resultService.verdict(session.getId(), ChallengeState.WA, Verdict.WA, "WRONG_ANSWER", "Product đúng contract nhưng dữ liệu sai.");
        }
    }

    private byte[] fixedRequestId(String requestId) {
        byte[] raw = requestId.getBytes(StandardCharsets.UTF_8);
        return Arrays.copyOf(raw, 8);
    }

    private byte[] serialize(Product product) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(product);
        out.flush();
        return bytes.toByteArray();
    }

    private Object deserialize(byte[] bytes) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        in.setObjectInputFilter(info -> {
            Class<?> serialClass = info.serialClass();
            if (serialClass == null) return ObjectInputFilter.Status.UNDECIDED;
            if (serialClass == Product.class || serialClass == String.class || serialClass.isPrimitive()) return ObjectInputFilter.Status.ALLOWED;
            return ObjectInputFilter.Status.REJECTED;
        });
        return in.readObject();
    }
}
