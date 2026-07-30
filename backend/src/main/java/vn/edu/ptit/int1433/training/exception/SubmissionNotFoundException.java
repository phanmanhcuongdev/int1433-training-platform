package vn.edu.ptit.int1433.training.exception;

import java.util.UUID;

public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException(UUID id) {
        super("Submission " + id + " was not found");
    }
}
