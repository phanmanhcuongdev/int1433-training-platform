import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import vn.edu.ptit.int1433.training.contract.Product;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            out.writeUTF(token);
            out.writeUTF(qCode);
            out.flush();

            Product product = (Product) in.readObject();
            Product answer = product; // TODO: chuẩn hóa Product theo đề bài.

            out.writeObject(answer);
            out.flush();
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
