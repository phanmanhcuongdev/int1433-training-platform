package vn.edu.ptit.int1433.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodeSubmissionRequest(
    @NotBlank String language,
    @Size(max = 255) String originalFileName,
    @NotBlank @Size(max = 20480) String sourceCode
) {
}
