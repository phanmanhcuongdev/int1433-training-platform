package vn.edu.ptit.int1433.training.runner;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import vn.edu.ptit.int1433.training.entity.Exercise;
import vn.edu.ptit.int1433.training.entity.Verdict;

@Service
public class DockerJavaCodeRunner implements JavaCodeRunner, InitializingBean {
    private final RunnerProperties properties;
    private final Path workspaceRoot;

    public DockerJavaCodeRunner(RunnerProperties properties) {
        this.properties = properties;
        this.workspaceRoot = Paths.get(properties.workspaceRoot()).toAbsolutePath().normalize();
    }

    @Override
    public void afterPropertiesSet() {
        try {
            Files.createDirectories(workspaceRoot);
            trySetOpenPermissions(workspaceRoot);
            cleanupStaleWorkspaces();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to prepare Java runner workspace root", exception);
        }
    }

    @Override
    public RunnerResult judge(Exercise exercise, JavaSourceSubmission source) {
        if (!runnerImageAvailable()) {
            return new RunnerResult(Verdict.INTERNAL_ERROR, "RUNNER_IMAGE_UNAVAILABLE", "Runner image chưa sẵn sàng trên máy chủ.", "", "", List.of());
        }

        Path workspace = null;
        try {
            workspace = createWorkspace();
            trySetOpenPermissions(workspace);
            Path sourceFile = workspace.resolve(source.originalFileName()).toAbsolutePath().normalize();
            if (!sourceFile.startsWith(workspace)) {
                throw new IOException("Runner source file escaped workspace");
            }
            Files.writeString(sourceFile, source.sourceCode(), StandardCharsets.UTF_8);
            trySetOpenPermissions(sourceFile);

            ProcessResult compile = runDocker(workspace, List.of("javac", "-encoding", "UTF-8", source.originalFileName()), new byte[0], properties.compileTimeoutMs());
            if (compile.timedOut()) {
                return new RunnerResult(Verdict.TLE, "COMPILE_TIMEOUT", "Biên dịch quá thời gian.", compile.combinedOutput(), "", List.of());
            }
            if (compile.exitCode() != 0) {
                return new RunnerResult(Verdict.CE, "COMPILE_ERROR", "Không biên dịch được bằng javac 21.", compile.combinedOutput(), "", List.of());
            }

            return switch (exercise.getId()) {
                case "fnd-character-flush-001" -> judgeCharacter(workspace, source.entryClassName());
                case "fnd-data-order-001" -> judgeDataOrder(workspace, source.entryClassName());
                default -> new RunnerResult(Verdict.INTERNAL_ERROR, "UNKNOWN_JAVA_GRADER", "Bài này chưa có Java grader.", "", "", List.of());
            };
        } catch (IOException exception) {
            return new RunnerResult(Verdict.INTERNAL_ERROR, "RUNNER_IO_ERROR", "Runner không tạo được workspace.", exception.getMessage(), "", List.of());
        } finally {
            if (workspace != null) {
                cleanup(workspace);
            }
        }
    }

    private Path createWorkspace() throws IOException {
        Files.createDirectories(workspaceRoot);
        Path workspace = Files.createTempDirectory(workspaceRoot, "submission-").toAbsolutePath().normalize();
        if (!workspace.startsWith(workspaceRoot)) {
            throw new IOException("Runner workspace escaped configured root");
        }
        return workspace;
    }

    private RunnerResult judgeCharacter(Path workspace, String entryClassName) throws IOException {
        List<TextCase> cases = List.of(
            new TextCase("hello\n", "HELLO"),
            new TextCase("  Lap   trinh  mang  \n", "LAP TRINH MANG"),
            new TextCase("xin Chào mạng\n", "XIN CHÀO MẠNG"),
            new TextCase("   \n", ""),
            new TextCase("no-newline", "NO-NEWLINE")
        );
        List<RunnerTestResult> results = new ArrayList<>();
        String runtime = "";
        for (int index = 0; index < cases.size(); index += 1) {
            ProcessResult run = runDocker(workspace, List.of("java", entryClassName), cases.get(index).input().getBytes(StandardCharsets.UTF_8), properties.runTimeoutMs());
            runtime = run.combinedOutput();
            RunnerTestResult result = classifyText(index + 1, run, cases.get(index).expected());
            results.add(result);
            if (result.verdict() != Verdict.AC) {
                return aggregate(result.verdict(), result.diagnosticCode(), result.publicMessage(), runtime, results);
            }
        }
        return aggregate(Verdict.AC, "AC", "Tất cả hidden test đều đúng.", runtime, results);
    }

    private RunnerResult judgeDataOrder(Path workspace, String entryClassName) throws IOException {
        List<DataCase> cases = List.of(
            new DataCase(12, 18, 5, 1.5d),
            new DataCase(7, 13, 100, 2.25d),
            new DataCase(21, 21, 0, -1.0d)
        );
        List<RunnerTestResult> results = new ArrayList<>();
        String runtime = "";
        for (int index = 0; index < cases.size(); index += 1) {
            byte[] input = dataInput(cases.get(index));
            ProcessResult run = runDocker(workspace, List.of("java", entryClassName), input, properties.runTimeoutMs());
            runtime = run.stderrString();
            RunnerTestResult result = classifyBinary(index + 1, run, cases.get(index));
            results.add(result);
            if (result.verdict() != Verdict.AC) {
                return aggregate(result.verdict(), result.diagnosticCode(), result.publicMessage(), runtime, results);
            }
        }
        return aggregate(Verdict.AC, "AC", "Tất cả hidden test đều đúng.", runtime, results);
    }

    private RunnerResult aggregate(Verdict verdict, String code, String message, String runtime, List<RunnerTestResult> tests) {
        return new RunnerResult(verdict, code, message, "", runtime, tests);
    }

    private RunnerTestResult classifyText(int index, ProcessResult run, String expected) {
        if (run.timedOut()) return new RunnerTestResult(index, Verdict.TLE, (int) run.elapsed().toMillis(), "RUN_TIMEOUT", "Chương trình chạy quá thời gian.");
        if (run.exitCode() != 0) return new RunnerTestResult(index, Verdict.RE, (int) run.elapsed().toMillis(), "RUNTIME_ERROR", "Chương trình kết thúc với lỗi runtime.");
        String actual = normalizeLine(run.stdoutString());
        if (!expected.equals(actual)) return new RunnerTestResult(index, Verdict.WA, (int) run.elapsed().toMillis(), "WRONG_OUTPUT", "Output không khớp hidden test.");
        return new RunnerTestResult(index, Verdict.AC, (int) run.elapsed().toMillis(), "AC", "Đúng.");
    }

    private RunnerTestResult classifyBinary(int index, ProcessResult run, DataCase dataCase) {
        if (run.timedOut()) return new RunnerTestResult(index, Verdict.TLE, (int) run.elapsed().toMillis(), "RUN_TIMEOUT", "Chương trình chạy quá thời gian.");
        if (run.exitCode() != 0) return new RunnerTestResult(index, Verdict.RE, (int) run.elapsed().toMillis(), "RUNTIME_ERROR", "Chương trình kết thúc với lỗi runtime.");
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(run.stdout()));
            int gcd = in.readInt();
            long lcm = in.readLong();
            long total = in.readLong();
            double doubled = in.readDouble();
            if (gcd == gcd(dataCase.a(), dataCase.b()) && lcm == lcm(dataCase.a(), dataCase.b()) && total == dataCase.c() + dataCase.a() + dataCase.b() && Double.compare(doubled, dataCase.d() * 2.0d) == 0) {
                return new RunnerTestResult(index, Verdict.AC, (int) run.elapsed().toMillis(), "AC", "Đúng.");
            }
            return new RunnerTestResult(index, Verdict.WA, (int) run.elapsed().toMillis(), "WRONG_BINARY_OUTPUT", "Binary output sai giá trị hoặc sai thứ tự.");
        } catch (IOException exception) {
            return new RunnerTestResult(index, Verdict.WA, (int) run.elapsed().toMillis(), "MALFORMED_BINARY_OUTPUT", "Output không đọc được theo DataInputStream contract.");
        }
    }

    private byte[] dataInput(DataCase dataCase) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(bytes));
        out.writeInt(dataCase.a());
        out.writeInt(dataCase.b());
        out.writeLong(dataCase.c());
        out.writeDouble(dataCase.d());
        out.flush();
        return bytes.toByteArray();
    }

    private ProcessResult runDocker(Path workspace, List<String> command, byte[] stdin, long timeoutMs) throws IOException {
        String containerName = "int1433-runner-" + UUID.randomUUID();
        List<String> args = new ArrayList<>(List.of(
            "docker", "run", "--rm", "-i",
            "--name", containerName,
            "--pull", "never",
            "--network", "none",
            "--memory", properties.memoryMb() + "m",
            "--pids-limit", String.valueOf(properties.pidsLimit()),
            "--cpus", String.valueOf(properties.cpus()),
            "--read-only",
            "--cap-drop", "ALL",
            "--security-opt", "no-new-privileges",
            "--user", "10001:10001",
            "--tmpfs", "/tmp:rw,noexec,nosuid,size=64m",
            "-v", workspace.toAbsolutePath().normalize() + ":/workspace:rw",
            "-w", "/workspace",
            properties.image()
        ));
        args.addAll(command);

        long started = System.nanoTime();
        Process process = new ProcessBuilder(args).start();
        ExecutorService collectors = Executors.newFixedThreadPool(2);
        Future<byte[]> stdoutFuture = collectors.submit(new BoundedStreamCollector(process.getInputStream(), properties.outputLimitBytes()));
        Future<byte[]> stderrFuture = collectors.submit(new BoundedStreamCollector(process.getErrorStream(), properties.outputLimitBytes()));
        try {
            process.getOutputStream().write(stdin);
        } catch (IOException ignored) {
            // The container may have exited before consuming stdin.
        } finally {
            try {
                process.getOutputStream().close();
            } catch (IOException ignored) {
            }
        }
        boolean finished;
        try {
            finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            finished = false;
        }
        Duration elapsed = Duration.ofNanos(System.nanoTime() - started);
        if (!finished) {
            process.destroyForcibly();
            forceRemoveContainer(containerName);
        }
        byte[] stdout = collect(stdoutFuture);
        byte[] stderr = collect(stderrFuture);
        collectors.shutdownNow();
        if (!finished) {
            return new ProcessResult(-1, true, stdout, stderr, elapsed);
        }
        return new ProcessResult(process.exitValue(), false, stdout, stderr, elapsed);
    }

    private boolean runnerImageAvailable() {
        try {
            Process process = new ProcessBuilder("docker", "image", "inspect", properties.image()).start();
            return process.waitFor(3, TimeUnit.SECONDS) && process.exitValue() == 0;
        } catch (IOException exception) {
            return false;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void forceRemoveContainer(String containerName) {
        try {
            Process process = new ProcessBuilder("docker", "rm", "-f", containerName).start();
            process.waitFor(3, TimeUnit.SECONDS);
        } catch (IOException ignored) {
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private byte[] collect(Future<byte[]> future) throws IOException {
        try {
            return future.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return new byte[0];
        } catch (ExecutionException exception) {
            throw new IOException("Unable to collect runner output", exception);
        } catch (java.util.concurrent.TimeoutException exception) {
            return new byte[0];
        }
    }

    private void trySetOpenPermissions(Path workspace) {
        try {
            Files.setPosixFilePermissions(workspace, Set.of(
                PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE, PosixFilePermission.OTHERS_EXECUTE
            ));
        } catch (IOException ignored) {
            // Docker will report a compile error if the mounted workspace cannot be written.
        }
    }

    private void cleanup(Path workspace) {
        try {
            Files.walk(workspace)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        } catch (IOException ignored) {
        }
    }

    private void cleanupStaleWorkspaces() throws IOException {
        Instant cutoff = Instant.now().minus(Duration.ofHours(6));
        if (!Files.isDirectory(workspaceRoot)) {
            return;
        }
        try (var children = Files.list(workspaceRoot)) {
            for (Path child : children.filter(Files::isDirectory).toList()) {
                if (!child.getFileName().toString().startsWith("submission-")) {
                    continue;
                }
                try {
                    Instant modified = Files.getLastModifiedTime(child).toInstant();
                    if (modified.isBefore(cutoff)) {
                        cleanup(child);
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String normalizeLine(String value) {
        return value.replace("\r\n", "\n").replace('\r', '\n').stripTrailing().lines().findFirst().orElse("");
    }

    private int gcd(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int t = x % y;
            x = y;
            y = t;
        }
        return x;
    }

    private long lcm(int a, int b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).divide(BigDecimal.valueOf(gcd(a, b))).longValue();
    }

    private record TextCase(String input, String expected) {}
    private record DataCase(int a, int b, long c, double d) {}
    private record BoundedStreamCollector(InputStream stream, int limit) implements Callable<byte[]> {
        @Override
        public byte[] call() throws IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(Math.min(limit, 8192));
            byte[] buffer = new byte[4096];
            int total = 0;
            int read;
            while ((read = stream.read(buffer)) != -1) {
                int remaining = limit - total;
                if (remaining > 0) {
                    int kept = Math.min(remaining, read);
                    bytes.write(buffer, 0, kept);
                    total += kept;
                }
            }
            return bytes.toByteArray();
        }
    }
    private record ProcessResult(int exitCode, boolean timedOut, byte[] stdout, byte[] stderr, Duration elapsed) {
        public String stdoutString() { return new String(stdout, StandardCharsets.UTF_8); }
        public String stderrString() { return new String(stderr, StandardCharsets.UTF_8); }
        public String combinedOutput() { return (stdoutString() + stderrString()).strip(); }
    }
}
