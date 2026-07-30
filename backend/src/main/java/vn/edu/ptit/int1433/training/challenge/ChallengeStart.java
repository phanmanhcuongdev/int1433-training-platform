package vn.edu.ptit.int1433.training.challenge;

import vn.edu.ptit.int1433.training.entity.ChallengeSession;

public record ChallengeStart(
    ChallengeSession session,
    String plaintextToken
) {
}
