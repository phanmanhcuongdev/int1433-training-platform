package vn.edu.ptit.int1433.training.dto;

import java.util.List;

public record PaginatedResponse<T>(
    List<T> items,
    int page,
    int size,
    long totalItems,
    int totalPages
) {
}
