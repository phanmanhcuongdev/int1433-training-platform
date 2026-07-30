package vn.edu.ptit.int1433.training.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.edu.ptit.int1433.training.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ExerciseNotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "EXERCISE_NOT_FOUND", exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFilter(InvalidFilterException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_FILTER", exception.getMessage(), request);
    }

    @ExceptionHandler({
        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Request parameters are invalid", request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabase(DataAccessException exception, HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "Database operation failed", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error", request);
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String code, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ErrorResponse(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            code,
            message,
            request.getRequestURI()
        ));
    }
}
