package vn.edu.ptit.int1433.training.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.edu.ptit.int1433.training.dto.ExerciseDetailResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseEvaluationResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseFilterResponse;
import vn.edu.ptit.int1433.training.dto.ExerciseSummaryResponse;
import vn.edu.ptit.int1433.training.dto.PaginatedResponse;
import vn.edu.ptit.int1433.training.entity.ExerciseLevel;
import vn.edu.ptit.int1433.training.entity.ExerciseStatus;
import vn.edu.ptit.int1433.training.entity.SourceLabel;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.exception.InvalidFilterException;
import vn.edu.ptit.int1433.training.mapper.ExerciseMapper;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;

@Service
@Transactional(readOnly = true)
public class ExerciseService {
    private static final int MAX_PAGE_SIZE = 100;

    private final ExerciseRepository repository;
    private final ExerciseMapper mapper;

    public ExerciseService(ExerciseRepository repository, ExerciseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PaginatedResponse<ExerciseSummaryResponse> list(
        String q,
        String technology,
        String level,
        String sourceLabel,
        String status,
        int page,
        int size,
        String sort
    ) {
        if (page < 0) {
            throw new InvalidFilterException("page must be greater than or equal to 0");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidFilterException("size must be between 1 and " + MAX_PAGE_SIZE);
        }

        PageRequest pageRequest = PageRequest.of(page, size, parseSort(sort));
        Page<ExerciseSummaryResponse> result = repository.search(
            normalizeSearch(q),
            blankToNull(technology),
            parseEnum(level, ExerciseLevel.class, "level"),
            parseEnum(sourceLabel, SourceLabel.class, "sourceLabel"),
            parseEnum(status, ExerciseStatus.class, "status"),
            pageRequest
        ).map(mapper::toSummary);

        return new PaginatedResponse<>(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }

    public ExerciseDetailResponse getById(String id) {
        return repository.findDetailById(id)
            .map(mapper::toDetail)
            .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    public ExerciseEvaluationResponse getEvaluation(String id) {
        return repository.findDetailById(id)
            .map(mapper::toEvaluation)
            .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    public ExerciseFilterResponse filters() {
        return new ExerciseFilterResponse(
            repository.findDistinctTechnologies(),
            repository.findDistinctLevels().stream().map(Enum::name).toList(),
            repository.findDistinctSourceLabels().stream().map(Enum::name).toList(),
            repository.findDistinctStatuses().stream().map(Enum::name).toList()
        );
    }

    private String normalizeSearch(String q) {
        if (!StringUtils.hasText(q)) {
            return null;
        }
        return "%" + q.trim().toLowerCase() + "%";
    }

    private String blankToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private Sort parseSort(String sort) {
        if (!StringUtils.hasText(sort) || "displayOrder,id".equals(sort)) {
            return Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.asc("id"));
        }
        if ("title".equals(sort)) {
            return Sort.by(Sort.Order.asc("title"), Sort.Order.asc("id"));
        }
        if ("technology".equals(sort)) {
            return Sort.by(Sort.Order.asc("technology"), Sort.Order.asc("displayOrder"), Sort.Order.asc("id"));
        }
        throw new InvalidFilterException("Unsupported sort: " + sort);
    }

    private <T extends Enum<T>> T parseEnum(String value, Class<T> enumType, String fieldName) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, value.trim());
        } catch (IllegalArgumentException exception) {
            List<String> allowed = List.of(enumType.getEnumConstants()).stream().map(Enum::name).toList();
            throw new InvalidFilterException("Invalid " + fieldName + ": " + value + ". Allowed values: " + String.join(", ", allowed));
        }
    }
}
