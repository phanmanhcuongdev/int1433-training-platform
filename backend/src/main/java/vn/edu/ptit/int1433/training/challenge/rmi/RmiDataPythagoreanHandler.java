package vn.edu.ptit.int1433.training.challenge.rmi;

import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeHandler;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;

@Component
public class RmiDataPythagoreanHandler implements ChallengeHandler {
    private final ChallengeProperties properties;

    public RmiDataPythagoreanHandler(ChallengeProperties properties) {
        this.properties = properties;
    }

    @Override
    public String graderKey() {
        return "net.rmi.data_pythagorean.v1";
    }

    @Override
    public void start(ChallengeSession session, String plaintextToken) {
        session.setPortMetadata(properties.rmiRegistryPort());
        session.setEndpointMetadata("Int1433DataService");
    }
}
