package vn.edu.ptit.int1433.training.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.ptit.int1433.training.AbstractPostgresIntegrationTest;
import vn.edu.ptit.int1433.training.contract.Product;
import vn.edu.ptit.int1433.training.contract.rmi.DataService;
import vn.edu.ptit.int1433.training.dto.ChallengeSessionResponse;
import vn.edu.ptit.int1433.training.entity.Verdict;
import vn.edu.ptit.int1433.training.service.ChallengeSessionService;

@AutoConfigureMockMvc
class ChallengeEndToEndIntegrationTest extends AbstractPostgresIntegrationTest {
    private static final UUID PARTICIPANT = UUID.fromString("22222222-2222-4222-8222-222222222222");

    @Autowired
    ChallengeSessionService sessions;

    @Autowired
    MockMvc mockMvc;

    @Test
    void tcpBytePrimeSumAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-byte-prime-sum-001", PARTICIPANT);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", session.port()), 2000);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            writeFrame(out, session.token());
            writeFrame(out, session.qCode());
            out.flush();
            int count = in.readInt();
            int primeCount = 0;
            long primeSum = 0;
            for (int i = 0; i < count; i += 1) {
                int value = in.readInt();
                if (isPrime(value)) {
                    primeCount += 1;
                    primeSum += value;
                }
            }
            out.writeInt(primeCount);
            out.writeLong(primeSum);
            out.flush();
        }
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void tcpBytePrimeSumWrongAnswer() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-byte-prime-sum-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            writeFrame(out, session.token());
            writeFrame(out, session.qCode());
            out.flush();
            int count = in.readInt();
            for (int i = 0; i < count; i += 1) {
                in.readInt();
            }
            out.writeInt(0);
            out.writeLong(0L);
            out.flush();
        }
        assertVerdict(session, Verdict.WA);
    }

    @Test
    void tcpDataGcdLcmAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-data-gcd-lcm-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(session.token());
            out.writeUTF(session.qCode());
            out.flush();
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            int d = in.readInt();
            int gcd = gcd(a, b);
            out.writeInt(gcd);
            out.writeLong((long) a * b / gcd);
            out.writeLong((long) a + b + c + d);
            out.writeLong((long) a * b * c * d);
            out.flush();
        }
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void tcpDataGcdLcmWrongAnswer() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-data-gcd-lcm-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(session.token());
            out.writeUTF(session.qCode());
            out.flush();
            in.readInt();
            in.readInt();
            in.readInt();
            in.readInt();
            out.writeInt(-1);
            out.writeLong(-1L);
            out.writeLong(-1L);
            out.writeLong(-1L);
            out.flush();
        }
        assertVerdict(session, Verdict.WA);
    }

    @Test
    void tcpCharacterNormalizeAcAndWa() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-character-normalize-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            out.write(session.token() + ";" + session.qCode());
            out.newLine();
            out.flush();
            in.readLine();
            out.write("Lập Trình Mạng");
            out.newLine();
            out.flush();
        }
        assertVerdict(session, Verdict.AC);

        ChallengeSessionResponse wrong = sessions.start("tcp-character-normalize-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", wrong.port())) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            out.write(wrong.token() + ";" + wrong.qCode());
            out.newLine();
            out.flush();
            in.readLine();
            out.write("sai");
            out.newLine();
            out.flush();
        }
        assertVerdict(wrong, Verdict.WA);
    }

    @Test
    void tcpObjectProductAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-object-product-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeUTF(session.token());
            out.writeUTF(session.qCode());
            out.flush();
            Product product = (Product) in.readObject();
            product.setName("Usb Cable");
            product.setPrice(12.35d);
            product.setNormalized(true);
            out.writeObject(product);
            out.flush();
        }
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void tcpObjectProductWrongAnswer() throws Exception {
        ChallengeSessionResponse session = sessions.start("tcp-object-product-001", PARTICIPANT);
        try (Socket socket = new Socket("127.0.0.1", session.port())) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeUTF(session.token());
            out.writeUTF(session.qCode());
            out.flush();
            Product product = (Product) in.readObject();
            out.writeObject(product);
            out.flush();
        }
        assertVerdict(session, Verdict.WA);
    }

    @Test
    void udpStringRequestIdAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("udp-string-request-id-001", PARTICIPANT);
        try (DatagramSocket socket = new DatagramSocket()) {
            sendText(socket, session.port(), session.token() + ";" + session.qCode());
            DatagramPacket response = receive(socket);
            String responseText = text(response);
            String requestId = responseText.substring(0, responseText.indexOf(';'));
            sendText(socket, session.port(), requestId + ";hnìrt pậl");
        }
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void udpStringRequestIdRejectsStaleRequestId() throws Exception {
        ChallengeSessionResponse session = sessions.start("udp-string-request-id-001", PARTICIPANT);
        try (DatagramSocket socket = new DatagramSocket()) {
            sendText(socket, session.port(), session.token() + ";" + session.qCode());
            receive(socket);
            sendText(socket, session.port(), "STALE;hnìrt pậl");
        }
        assertVerdict(session, Verdict.PROTOCOL_ERROR);
    }

    @Test
    void udpObjectProductAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("udp-object-product-001", PARTICIPANT);
        try (DatagramSocket socket = new DatagramSocket()) {
            sendText(socket, session.port(), session.token() + ";" + session.qCode());
            DatagramPacket response = receive(socket);
            byte[] payload = Arrays.copyOfRange(response.getData(), response.getOffset(), response.getOffset() + response.getLength());
            byte[] requestId = Arrays.copyOf(payload, 8);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Arrays.copyOfRange(payload, 8, payload.length)));
            Product product = (Product) in.readObject();
            product.setName("Usb Hub");
            product.setQuantity(4);
            product.setNormalized(true);
            ByteArrayOutputStream objectBytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(objectBytes);
            out.writeObject(product);
            out.flush();
            byte[] submittedObject = objectBytes.toByteArray();
            byte[] submitted = new byte[8 + submittedObject.length];
            System.arraycopy(requestId, 0, submitted, 0, 8);
            System.arraycopy(submittedObject, 0, submitted, 8, submittedObject.length);
            socket.send(new DatagramPacket(submitted, submitted.length, new InetSocketAddress("127.0.0.1", session.port())));
        }
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void udpObjectProductRejectsWrongRequestId() throws Exception {
        ChallengeSessionResponse session = sessions.start("udp-object-product-001", PARTICIPANT);
        try (DatagramSocket socket = new DatagramSocket()) {
            sendText(socket, session.port(), session.token() + ";" + session.qCode());
            DatagramPacket response = receive(socket);
            byte[] payload = Arrays.copyOfRange(response.getData(), response.getOffset(), response.getOffset() + response.getLength());
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Arrays.copyOfRange(payload, 8, payload.length)));
            Product product = (Product) in.readObject();
            ByteArrayOutputStream objectBytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(objectBytes);
            out.writeObject(product);
            out.flush();
            byte[] submittedObject = objectBytes.toByteArray();
            byte[] submitted = new byte[8 + submittedObject.length];
            Arrays.fill(submitted, 0, 8, (byte) 'X');
            System.arraycopy(submittedObject, 0, submitted, 8, submittedObject.length);
            socket.send(new DatagramPacket(submitted, submitted.length, new InetSocketAddress("127.0.0.1", session.port())));
        }
        assertVerdict(session, Verdict.PROTOCOL_ERROR);
    }

    @Test
    void rmiDataPythagoreanAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("rmi-data-pythagorean-001", PARTICIPANT);
        DataService service = (DataService) LocateRegistry.getRegistry("127.0.0.1", session.port()).lookup(session.endpoint());
        int[] values = service.request(session.token(), session.qCode());
        assertThat(values).contains(3, 4, 5);
        assertThat(service.submit(session.token(), session.qCode(), new int[][] {{3, 4, 5}})).isTrue();
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void rmiDataPythagoreanWrongAnswer() throws Exception {
        ChallengeSessionResponse session = sessions.start("rmi-data-pythagorean-001", PARTICIPANT);
        DataService service = (DataService) LocateRegistry.getRegistry("127.0.0.1", session.port()).lookup(session.endpoint());
        service.request(session.token(), session.qCode());
        assertThat(service.submit(session.token(), session.qCode(), new int[][] {{1, 2, 3}})).isFalse();
        assertVerdict(session, Verdict.WA);
    }

    @Test
    void soapFactorizationAc() throws Exception {
        ChallengeSessionResponse session = sessions.start("ws-data-factorization-001", PARTICIPANT);
        mockMvc.perform(post("/ws/factorization")
                .contentType("text/xml")
                .content(envelope("<request><token>" + session.token() + "</token><qCode>" + session.qCode() + "</qCode></request>")))
            .andExpect(status().isOk());
        mockMvc.perform(post("/ws/factorization")
                .contentType("text/xml")
                .content(envelope("<submit><token>" + session.token() + "</token><qCode>" + session.qCode() + "</qCode><factors>2,2,2,3,3,5</factors></submit>")))
            .andExpect(status().isOk());
        assertVerdict(session, Verdict.AC);
    }

    @Test
    void soapFactorizationWrongAnswer() throws Exception {
        ChallengeSessionResponse session = sessions.start("ws-data-factorization-001", PARTICIPANT);
        mockMvc.perform(post("/ws/factorization")
                .contentType("text/xml")
                .content(envelope("<request><token>" + session.token() + "</token><qCode>" + session.qCode() + "</qCode></request>")))
            .andExpect(status().isOk());
        mockMvc.perform(post("/ws/factorization")
                .contentType("text/xml")
                .content(envelope("<submit><token>" + session.token() + "</token><qCode>" + session.qCode() + "</qCode><factors>2,3</factors></submit>")))
            .andExpect(status().isOk());
        assertVerdict(session, Verdict.WA);
    }

    private void assertVerdict(ChallengeSessionResponse session, Verdict verdict) throws Exception {
        for (int i = 0; i < 20; i += 1) {
            ChallengeSessionResponse current = sessions.get(session.sessionId(), PARTICIPANT);
            if (verdict.name().equals(current.verdict())) {
                return;
            }
            Thread.sleep(100);
        }
        assertThat(sessions.get(session.sessionId(), PARTICIPANT).verdict()).isEqualTo(verdict.name());
    }

    private void writeFrame(DataOutputStream out, String value) throws Exception {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private boolean isPrime(int value) {
        if (value < 2) return false;
        for (int i = 2; i * i <= value; i += 1) if (value % i == 0) return false;
        return true;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    private void sendText(DatagramSocket socket, int port, String value) throws Exception {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(bytes, bytes.length, new InetSocketAddress("127.0.0.1", port)));
    }

    private DatagramPacket receive(DatagramSocket socket) throws Exception {
        byte[] buffer = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.setSoTimeout(3000);
        socket.receive(packet);
        return packet;
    }

    private String text(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    private String envelope(String inner) {
        return "<?xml version=\"1.0\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" + inner + "</soap:Body></soap:Envelope>";
    }
}
