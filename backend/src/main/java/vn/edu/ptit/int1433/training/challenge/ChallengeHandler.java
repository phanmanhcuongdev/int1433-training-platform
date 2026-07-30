package vn.edu.ptit.int1433.training.challenge;

import vn.edu.ptit.int1433.training.entity.ChallengeSession;

public interface ChallengeHandler {
    String graderKey();
    void start(ChallengeSession session, String plaintextToken);
}
