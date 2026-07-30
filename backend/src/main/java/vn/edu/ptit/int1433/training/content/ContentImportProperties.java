package vn.edu.ptit.int1433.training.content;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "int1433.content")
public record ContentImportProperties(
    String root
) {
}
