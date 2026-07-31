package vn.edu.ptit.int1433.training.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
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
        SubmissionResponse response = submit("fnd-character-flush-001", "WrongAnswer.java", """
            public class WrongAnswer {
                public static void main(String[] args) {
                    System.out.println("SAI");
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("WA");
    }

    @Test
    void syntaxErrorReturnsCe() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", "Broken.java", """
            public class Broken {
                public static void main(String[] args) {
                    int value = ;
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("CE");
    }

    @Test
    void runtimeExceptionReturnsRe() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", "Boom.java", """
            public class Boom {
                public static void main(String[] args) {
                    throw new RuntimeException("boom");
                }
            }
            """);

        assertThat(response.verdict()).isEqualTo("RE");
    }

    @Test
    void infiniteLoopReturnsTle() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", "Loop.java", """
            public class Loop {
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
        SubmissionResponse response = submit("fnd-character-flush-001", "Noisy.java", """
            public class Noisy {
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
        SubmissionResponse response = submit("fnd-character-flush-001", "NetworkAttempt.java", """
            public class NetworkAttempt {
                public static void main(String[] args) throws Exception {
                    new java.net.Socket("example.com", 80);
                    System.out.println("SHOULD_NOT_CONNECT");
                }
            }
            """);

        assertThat(response.verdict()).isIn("RE", "TLE", "WA");
    }

    @Test
    void jsonSubmissionWithoutFilenameInfersClassNameForCompatibility() throws Exception {
        SubmissionResponse response = submit("fnd-character-flush-001", null, characterSolution("CharacterFlush"));

        assertThat(response.verdict()).isEqualTo("AC");
        assertThat(response.originalFileName()).isEqualTo("CharacterFlush.java");
        assertThat(response.entryClassName()).isEqualTo("CharacterFlush");
    }

    @Test
    void missingPublicClassIsRejected() throws Exception {
        mockMvc.perform(post("/api/v1/exercises/fnd-character-flush-001/code-submissions")
                .header(ParticipantService.HEADER, PARTICIPANT)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Map.of("language", "JAVA", "originalFileName", "Main.java", "sourceCode", """
                    class Main {
                        public static void main(String[] args) {
                            System.out.println("HELLO");
                        }
                    }
                    """))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Không tìm thấy top-level public class."));
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

    @Test
    void multipartUploadWithNaturalJavaFilenameReturnsAcAndPersistsSourceMetadata() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "CharacterFlush.java",
            "text/x-java-source",
            characterSolution("CharacterFlush").getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        String responseBody = mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.verdict").value("AC"))
            .andExpect(jsonPath("$.originalFileName").value("CharacterFlush.java"))
            .andExpect(jsonPath("$.entryClassName").value("CharacterFlush"))
            .andExpect(jsonPath("$.sourceSha256").isNotEmpty())
            .andExpect(jsonPath("$.sourceCode").value(org.hamcrest.Matchers.containsString("public class CharacterFlush")))
            .andReturn()
            .getResponse()
            .getContentAsString();

        SubmissionResponse response = objectMapper.readValue(responseBody, SubmissionResponse.class);
        mockMvc.perform(get("/api/v1/submissions/" + response.id()).header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalFileName").value("CharacterFlush.java"))
            .andExpect(jsonPath("$.entryClassName").value("CharacterFlush"))
            .andExpect(jsonPath("$.sourceCode").value(org.hamcrest.Matchers.containsString("BufferedReader")));
    }

    @Test
    void multipartUploadAcceptsShortClassNameA() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            public class A {
                public static void main(String[] args) {
                    System.out.println("SAI");
                }
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalFileName").value("A.java"))
            .andExpect(jsonPath("$.entryClassName").value("A"));
    }

    @Test
    void multipartUploadAcceptsAnnotatedFinalPublicClassAndIgnoresCommentText() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            // package foo;
            // public class CommentOnly {}
            @Deprecated
            final public class A {
                public static void main(String[] args) throws Exception {
                    System.out.println("SAI");
                }
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalFileName").value("A.java"))
            .andExpect(jsonPath("$.entryClassName").value("A"));
    }

    @Test
    void multipartUploadIgnoresPublicClassTextInsideString() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            public class A {
                public static void main(String[] args) {
                    String text = "public class B {} package foo;";
                    System.out.println(text);
                }
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.entryClassName").value("A"));
    }

    @Test
    void multipartUploadRejectsClassNameDifferentFromFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("HELLO");
                }
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Tên public class Main không trùng với tên file A.java."));
    }

    @Test
    void multipartUploadRejectsInvalidJavaIdentifierFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Invalid-name.java", "text/x-java-source", characterSolution("InvalidName").getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Tên file không phải là tên lớp Java hợp lệ."));
    }

    @Test
    void multipartUploadRejectsPackageDeclaration() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            package foo;
            public class A {
                public static void main(String[] args) {
                }
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Không được khai báo package trong bài một file."));
    }

    @Test
    void multipartUploadRejectsTwoTopLevelPublicClasses() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", """
            public class A {
                public static void main(String[] args) {
                }
            }
            public class B {
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Chỉ được khai báo một top-level public class."));
    }

    @Test
    void multipartUploadRejectsMissingMainMethod() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", "public class A {}".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Không tìm thấy public static void main(String[] args)."));
    }

    @Test
    void jsonSubmissionRejectsPathTraversalFilename() throws Exception {
        mockMvc.perform(post("/api/v1/exercises/fnd-character-flush-001/code-submissions")
                .header(ParticipantService.HEADER, PARTICIPANT)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Map.of(
                    "language", "JAVA",
                    "originalFileName", "../A.java",
                    "sourceCode", """
                        public class A {
                            public static void main(String[] args) {
                            }
                        }
                        """
                ))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Tên file không phải là tên lớp Java hợp lệ."));
    }

    @Test
    void multipartUploadRejectsEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", new byte[0]);

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("File không được rỗng."));
    }

    @Test
    void multipartUploadRejectsOversizedFile() throws Exception {
        String source = "public class Main {" + " ".repeat(20 * 1024) + "}";
        MockMultipartFile file = new MockMultipartFile("file", "Main.java", "text/x-java-source", source.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("File vượt quá giới hạn 20 KB."));
    }

    @Test
    void multipartUploadRejectsInvalidUtf8() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "A.java", "text/x-java-source", new byte[] {(byte) 0xc3, 0x28});

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("File không phải UTF-8 hợp lệ."));
    }

    @Test
    void multipartUploadRejectsMissingPublicClass() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Main.java", "text/x-java-source", "class Main {}".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/exercises/fnd-character-flush-001/submissions")
                .file(file)
                .header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Không tìm thấy top-level public class."));
    }

    @Test
    void resubmissionCreatesNewSubmissionAndKeepsOldSource() throws Exception {
        SubmissionResponse first = submit("fnd-character-flush-001", "CharacterFlush.java", characterSolution("CharacterFlush"));
        SubmissionResponse second = submit("fnd-character-flush-001", "WrongAnswer.java", """
            public class WrongAnswer {
                public static void main(String[] args) {
                    System.out.println("SAI");
                }
            }
            """);

        assertThat(first.id()).isNotEqualTo(second.id());
        assertThat(first.verdict()).isEqualTo("AC");
        assertThat(second.verdict()).isEqualTo("WA");
        mockMvc.perform(get("/api/v1/submissions/" + first.id()).header(ParticipantService.HEADER, PARTICIPANT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.verdict").value("AC"))
            .andExpect(jsonPath("$.sourceCode").value(org.hamcrest.Matchers.containsString("BufferedReader")));
    }

    @Test
    void resubmissionWithChangedClassAndFilenameCreatesNewSubmission() throws Exception {
        SubmissionResponse first = submit("fnd-character-flush-001", "CharacterFlush.java", characterSolution("CharacterFlush"));
        SubmissionResponse second = submit("fnd-character-flush-001", "RenamedFlush.java", characterSolution("RenamedFlush"));

        assertThat(first.id()).isNotEqualTo(second.id());
        assertThat(second.verdict()).isEqualTo("AC");
        assertThat(second.originalFileName()).isEqualTo("RenamedFlush.java");
        assertThat(second.entryClassName()).isEqualTo("RenamedFlush");
    }

    private SubmissionResponse submit(String exerciseId, String sourceCode) throws Exception {
        return submit(exerciseId, "Main.java", sourceCode);
    }

    private SubmissionResponse submit(String exerciseId, String originalFileName, String sourceCode) throws Exception {
        Map<String, String> payload = originalFileName == null
            ? Map.of("language", "JAVA", "sourceCode", sourceCode)
            : Map.of("language", "JAVA", "originalFileName", originalFileName, "sourceCode", sourceCode);
        String body = objectMapper.writeValueAsString(payload);
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
        return characterSolution("Main");
    }

    private String characterSolution(String className) {
        return """
            import java.io.*;
            import java.nio.charset.StandardCharsets;
            import java.util.Locale;

            public class %s {
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
            """.formatted(className);
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
