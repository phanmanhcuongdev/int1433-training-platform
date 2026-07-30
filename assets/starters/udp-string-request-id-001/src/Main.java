import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] request = (token + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(host), port));

            byte[] buffer = new byte[2048];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            String message = new String(response.getData(), response.getOffset(), response.getLength(), StandardCharsets.UTF_8);

            String requestId = ""; // TODO: tach requestId tu message.
            String answer = "";    // TODO: xu ly payload.
            byte[] submit = (requestId + ";" + answer).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(submit, submit.length, InetAddress.getByName(host), port));
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
