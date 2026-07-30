package vn.edu.ptit.int1433.training.challenge.rmi;

import jakarta.annotation.PostConstruct;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;

@Configuration
@ConditionalOnExpression("'${app.command:}' == ''")
public class RmiRegistryConfig {
    private final ChallengeProperties properties;
    private final RmiDataServiceImpl service;

    public RmiRegistryConfig(ChallengeProperties properties, RmiDataServiceImpl service) {
        this.properties = properties;
        this.service = service;
    }

    @PostConstruct
    void startRegistry() throws Exception {
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(properties.rmiRegistryPort());
        } catch (java.rmi.server.ExportException alreadyRunning) {
            registry = LocateRegistry.getRegistry(properties.rmiRegistryPort());
        }
        registry.rebind("Int1433DataService", service);
    }
}
