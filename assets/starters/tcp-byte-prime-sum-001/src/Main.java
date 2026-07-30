import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        try (Socket socket = new Socket(host, port)) {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            writeUtf8Frame(out, token);
            writeUtf8Frame(out, qCode);
            out.flush();

            int count = in.readInt();
            int[] values = new int[count];
            for (int i = 0; i < count; i++) {
                values[i] = in.readInt();
            }

            int primeCount = 0; // TODO: tinh so luong so nguyen to.
            long primeSum = 0L; // TODO: tinh tong so nguyen to.

            out.writeInt(primeCount);
            out.writeLong(primeSum);
            out.flush();
        }
    }

    private static void writeUtf8Frame(DataOutputStream out, String value) throws Exception {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
