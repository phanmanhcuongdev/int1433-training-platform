import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String NS = "http://training.int1433.ptit.edu.vn/ws/factorization";

    public static void main(String[] args) throws Exception {
        String endpoint = env("ENDPOINT", "<ENDPOINT>");
        String wsdl = env("WSDL", "<WSDL>");
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        System.out.println("WSDL: " + wsdl);
        String requestBody = envelope("<f:request xmlns:f=\"" + NS + "\"><f:token>" + token + "</f:token><f:qCode>" + qCode + "</f:qCode></f:request>");
        String requestResponse = post(endpoint, requestBody);
        int n = 0; // TODO: đọc n từ SOAP response.
        int[] factors = new int[0]; // TODO: phân tích n và đưa các thừa số vào mảng tăng dần.

        StringBuilder factorTags = new StringBuilder();
        for (int factor : factors) {
            factorTags.append("<f:factors>").append(factor).append("</f:factors>");
        }
        String submitBody = envelope("<f:submit xmlns:f=\"" + NS + "\"><f:token>" + token + "</f:token><f:qCode>" + qCode + "</f:qCode>" + factorTags + "</f:submit>");
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
