package vn.edu.ptit.int1433.training.challenge.soap;

import org.springframework.stereotype.Component;
import vn.edu.ptit.int1433.training.challenge.ChallengeHandler;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.entity.ChallengeSession;

@Component
public class SoapFactorizationHandler implements ChallengeHandler {
    private final ChallengeProperties properties;

    public SoapFactorizationHandler(ChallengeProperties properties) {
        this.properties = properties;
    }

    @Override
    public String graderKey() {
        return "net.soap.data_factorization.v1";
    }

    @Override
    public void start(ChallengeSession session, String plaintextToken) {
        session.setEndpointMetadata("http://" + properties.publicHost() + ":" + properties.soapPort() + "/ws");
    }
}
