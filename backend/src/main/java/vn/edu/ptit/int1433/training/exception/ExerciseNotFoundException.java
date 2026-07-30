package vn.edu.ptit.int1433.training.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(String id) {
        super("Exercise " + id + " was not found");
    }
}
