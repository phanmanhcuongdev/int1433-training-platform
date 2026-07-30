import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        String endpoint = env("ENDPOINT", "<ENDPOINT>");
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        String requestBody = envelope("<request><token>" + token + "</token><qCode>" + qCode + "</qCode></request>");
        String requestResponse = post(endpoint, requestBody);
        int n = 0; // TODO: đọc n từ SOAP response.
        String factors = ""; // TODO: phân tích n và submit dạng 2,2,3.

        String submitBody = envelope("<submit><token>" + token + "</token><qCode>" + qCode + "</qCode><factors>" + factors + "</factors></submit>");
        post(endpoint, submitBody);
    }

    private static String post(String endpoint, String body) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) URI.create(endpoint).toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setDoOutput(true);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream out = connection.getOutputStream()) {
            out.write(bytes);
        }
        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String envelope(String body) {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" + body + "</soap:Body></soap:Envelope>";
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
