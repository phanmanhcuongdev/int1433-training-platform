package vn.edu.ptit.int1433.training.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.ptit.int1433.training.AbstractPostgresIntegrationTest;
import vn.edu.ptit.int1433.training.dto.SubmissionResponse;
import vn.edu.ptit.int1433.training.service.ParticipantService;

@AutoConfigureMockMvc
class JavaCodeRunnerIntegrationTest extends AbstractPostgresIntegrationTest {
    private static final String PARTICIPANT = "33333333-3333-4333-8333-333333333333";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void characterValidSolutionReturnsAcAndIsPersisted() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", characterSolution());

        assertThat(response.verdict()).isEqualTo("AC");
        mockMvc.perform(get("/api/v1/submissions/" + response.id()).header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.verdict").value("AC"))
            .andExpect(jsonPath("$.testResults.length()").value(5));
    }

    @Test
    void dataOrderValidSolutionReturnsAc() throws Exception {
        SubmissionResponse response = submit("fnd-data-order-001", dataOrderSolution());

        assertThat(response.verdict()).isEqualTo("AC");
        assertThat(response.testResults()).hasSize(3);
    }

    @Test
    void wrongAnswerReturnsWa() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("SAI");
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("WA");
    }

    @Test
    void syntaxErrorReturnsCe() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", "public class Main {");

        assertThat(response.verdict()).isEqualTo("CE");
    }

    @Test
    void runtimeExceptionReturnsRe() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            public class Main {
                public static void main(String[] args) {
                    throw new RuntimeException("boom");
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("RE");
    }

    @Test
    void infiniteLoopReturnsTle() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            public class Main {
                public static void main(String[] args) {
                    while (true) {
                    }
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("TLE");
    }

    @Test
    void excessiveOutputIsCapped() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            public class Main {
                public static void main(String[] args) {
                    for (int i = 0; i < 200000; i++) {
                        System.out.print("x");
                    }
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("WA");
        assertThat(response.runtimeOutput().getBytes(java.nio.charset.StandardCharsets.UTF_8).length).isLessThanOrEqualTo(65536);
    }

    @Test
    void outboundNetworkAttemptDoesNotSucceed() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            public class Main {
                public static void main(String[] args) throws Exception {
                    new java.net.Socket("example.com", 80);
                    System.out.println("SHOULD_NOT_CONNECT");
                }
            }
            """);

        assertThat(response.verdict()).isIn("RE", "TLE", "WA");
    }

    @Test
    void missingMainClassReturnsCe() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", """
            class Main {
                public static void main(String[] args) {
                    System.out.println("HELLO");
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("CE");
        assertThat(response.diagnosticCode()).isEqualTo("MISSING_MAIN_CLASS");
    }

    @Test
    void oversizedSourceIsRejectedBeforeRunner() throws Exception {
        String source = "public class Main {" + " ".repeat(20001) + "}";
        mockMvc.perform(post("/api/v1/exercises/fnd-character-flush-001/code-submissions")
                .header(ParticipantService.HEADER, PARTICIPANT)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Map.of("language", "JAVA", "sourceCode", source))))
            .andExpect(status().isBadRequest());
    }

    private SubmissionResponse submit(String exerciseId, String sourceCode) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("language", "JAVA", "sourceCode", sourceCode));
        String response = mockMvc.perform(post("/api/v1/exercises/" + exerciseId + "/code-submissions")
                .header(ParticipantService.HEADER, PARTICIPANT)
                .contentType("application/json")
                .content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return objectMapper.readValue(response, SubmissionResponse.class);
    }

    private String characterSolution() {
        return """
            import java.io.*;
            import java.nio.charset.StandardCharsets;
            import java.util.Locale;

            public class Main {
                public static void main(String[] args) throws Exception {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
                    String line = in.readLine();
                    if (line == null) {
                        line = "";
                    }
                    String normalized = line.strip().replaceAll("\\\\s+", " ").toUpperCase(Locale.ROOT);
                    out.write(normalized);
                    out.newLine();
                    out.flush();
                }
            }
            """;
    }

    private String dataOrderSolution() {
        return """
            import java.io.*;

            public class Main {
                public static void main(String[] args) throws Exception {
                    DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(System.out));
                    int a = in.readInt();
                    int b = in.readInt();
                    long c = in.readLong();
                    double d = in.readDouble();
                    int gcd = gcd(a, b);
                    long lcm = Math.abs((long) a / gcd * b);
                    out.writeInt(gcd);
                    out.writeLong(lcm);
                    out.writeLong(c + a + b);
                    out.writeDouble(d * 2.0d);
                    out.flush();
                }

                private static int gcd(int a, int b) {
                    a = Math.abs(a);
                    b = Math.abs(b);
                    while (b != 0) {
                        int t = a % b;
                        a = b;
                        b = t;
                    }
                    return a;
                }
            }
            """;
    }
}
