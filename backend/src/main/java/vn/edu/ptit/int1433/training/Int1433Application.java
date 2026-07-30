package vn.edu.ptit.int1433.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Int1433Application {
    public static void main(String[] args) {
        SpringApplication.run(Int1433Application.class, args);
    }
}
