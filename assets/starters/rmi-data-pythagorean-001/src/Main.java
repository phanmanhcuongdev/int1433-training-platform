import java.rmi.registry.LocateRegistry;
import vn.edu.ptit.int1433.training.contract.rmi.DataService;

public class Main {
    public static void main(String[] args) throws Exception {
        String host = env("HOST", "<HOST>");
        int port = Integer.parseInt(env("PORT", "0"));
        String serviceName = env("SERVICE", "Int1433DataService");
        String token = env("TOKEN", "<TOKEN>");
        String qCode = env("QCODE", "<QCODE>");

        DataService service = (DataService) LocateRegistry.getRegistry(host, port).lookup(serviceName);
        int[] values = service.request(token, qCode);
        int[][] triples = new int[0][0]; // TODO: tìm bộ ba Pythagore từ values.
        service.submit(token, qCode, triples);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
