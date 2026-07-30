package vn.edu.ptit.int1433.training.challenge;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "int1433.challenge")
public record ChallengeProperties(
    String host,
    String publicHost,
    String bindAddress,
    int tcpPortMin,
    int tcpPortMax,
    int udpPortMin,
    int udpPortMax,
    int rmiRegistryPort,
    int soapPort,
    int maxActiveSessionsPerParticipant,
    String starterAssetRoot
) {
}
