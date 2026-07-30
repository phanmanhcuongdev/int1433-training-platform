import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import vn.edu.ptit.int1433.training.contract.Product;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] request = (token + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(host), port));

            byte[] buffer = new byte[4096];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            byte[] packet = Arrays.copyOfRange(response.getData(), response.getOffset(), response.getOffset() + response.getLength());

            byte[] requestId = Arrays.copyOfRange(packet, 0, 8);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet, 8, packet.length - 8));
            Product product = (Product) in.readObject();
            Product answer = product; // TODO: sửa Product theo đề bài.

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(requestId);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(answer);
            out.flush();

            byte[] submit = output.toByteArray();
            socket.send(new DatagramPacket(submit, submit.length, InetAddress.getByName(host), port));
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
