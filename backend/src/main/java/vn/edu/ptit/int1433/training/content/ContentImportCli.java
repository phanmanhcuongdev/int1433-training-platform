package vn.edu.ptit.int1433.training.content;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ContentImportCli implements ApplicationRunner {
    private final Environment environment;
    private final ContentImportService service;
    private final ConfigurableApplicationContext context;

    public ContentImportCli(Environment environment, ContentImportService service, ConfigurableApplicationContext context) {
        this.environment = environment;
        this.service = service;
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) {
        String command = environment.getProperty("app.command", "");
        if (!"content-import".equals(command) && !"content-check".equals(command)) {
            return;
        }
        boolean dryRun = environment.getProperty("content.import.dry-run", Boolean.class, true);
        boolean allowDelete = environment.getProperty("content.import.allow-delete", Boolean.class, false);
        ContentImportResult result = service.run("content-check".equals(command) || dryRun, allowDelete);
        System.out.println(result.toConsoleString());
        int exitCode = result.success() ? 0 : 1;
        SpringApplication.exit(context, () -> exitCode);
        System.exit(exitCode);
    }
}
