import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        try (Socket socket = new Socket(host, port)) {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            out.writeUTF(token);
            out.writeUTF(qCode);
            out.flush();

            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            int d = in.readInt();

            int gcd = 0;      // TODO
            long lcm = 0L;    // TODO
            long sum = 0L;    // TODO
            long product = 0L; // TODO

            out.writeInt(gcd);
            out.writeLong(lcm);
            out.writeLong(sum);
            out.writeLong(product);
            out.flush();
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
