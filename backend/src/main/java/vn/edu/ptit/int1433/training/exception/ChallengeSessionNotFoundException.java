package vn.edu.ptit.int1433.training.exception;

import java.util.UUID;

public class ChallengeSessionNotFoundException extends RuntimeException {
    public ChallengeSessionNotFoundException(UUID id) {
        super("Challenge session " + id + " was not found");
    }
}
