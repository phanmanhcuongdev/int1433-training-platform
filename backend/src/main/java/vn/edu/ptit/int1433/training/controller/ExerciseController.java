package vn.edu.ptit.int1433.training.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.ptit.int1433.training.dto.ExerciseDetailResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseFilterResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseSummaryResponse;
import vn.edu.ptit.int1433.training.dto.PaginatedResponse;
import vn.edu.ptit.int1433.training.service.ExerciseService;

@Validated
@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {
    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping
    public PaginatedResponse<ExerciseSummaryResponse> list(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String technology,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) String sourceLabel,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
        @RequestParam(defaultValue = "displayOrder,id") String sort
    ) {
        return service.list(q, technology, level, sourceLabel, status, page, size, sort);
    }

    @GetMapping("/{id}")
    public ExerciseDetailResponse detail(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/filters")
    public ExerciseFilterResponse filters() {
        return service.filters();
    }
}
