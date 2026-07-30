package vn.edu.ptit.int1433.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodeSubmissionRequest(
    @NotBlank String language,
    @NotBlank @Size(max = 20000) String sourceCode
) {
}
