package vn.edu.ptit.int1433.training.content;

import java.util.List;
import java.util.Map;

public record ContentImportResult(
    int created,
    int updated,
    int unchanged,
    int deleted,
    List<String> databaseOnlyRecords,
    Map<String, List<String>> changedFields,
    List<String> errors,
    boolean dryRun
) {
    public boolean success() {
        return errors.isEmpty();
    }

    public String toConsoleString() {
        StringBuilder builder = new StringBuilder();
        builder.append(dryRun ? "DRY RUN\n" : "IMPORT\n");
        builder.append("CREATED=").append(created).append('\n');
        builder.append("UPDATED=").append(updated).append('\n');
        builder.append("UNCHANGED=").append(unchanged).append('\n');
        builder.append("DELETED=").append(deleted).append('\n');
        if (!databaseOnlyRecords.isEmpty()) {
            builder.append("DATABASE_ONLY=").append(databaseOnlyRecords).append('\n');
        }
        changedFields.forEach((id, fields) -> builder.append(id).append(" changed fields: ").append(fields).append('\n'));
        errors.forEach(error -> builder.append("ERROR: ").append(error).append('\n'));
        return builder.toString();
    }
}
