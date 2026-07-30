package vn.edu.ptit.int1433.training.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("dev")
@EnableConfigurationProperties(CorsProperties.class)
public class DevCorsConfig implements WebMvcConfigurer {
    private final CorsProperties properties;

    public DevCorsConfig(CorsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(properties.getAllowedOrigins().toArray(String[]::new))
            .allowedMethods("GET", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
    }
}
