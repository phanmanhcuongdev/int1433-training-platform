package vn.edu.ptit.int1433.training.runner;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "int1433.runner")
public record RunnerProperties(
    String image,
    long compileTimeoutMs,
    long runTimeoutMs,
    int memoryMb,
    int pidsLimit,
    double cpus,
    int outputLimitBytes
) {
}
