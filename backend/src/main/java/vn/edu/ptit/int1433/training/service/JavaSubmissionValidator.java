package vn.edu.ptit.int1433.training.service;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.edu.ptit.int1433.training.exception.SubmissionValidationException;
import vn.edu.ptit.int1433.training.runner.JavaSourceSubmission;

@Component
public class JavaSubmissionValidator {
    private static final Set<String> JAVA_KEYWORDS = Set.of(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
        "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
        "volatile", "while", "true", "false", "null", "_"
    );
    private static final Set<String> CLASS_MODIFIERS = Set.of(
        "public", "abstract", "final", "strictfp", "sealed", "non-sealed"
    );

    public JavaSourceSubmission validate(String originalFileName, String sourceCode) {
        String safeFileName = sanitizeFilename(originalFileName);
        String basename = basename(safeFileName);
        if (!isJavaIdentifier(basename)) {
            throw new SubmissionValidationException("Tên file không phải là tên lớp Java hợp lệ.");
        }
        if (declaresPackage(sourceCode)) {
            throw new SubmissionValidationException("Không được khai báo package trong bài một file.");
        }

        List<String> publicClasses = topLevelPublicClasses(sourceCode);
        if (publicClasses.isEmpty()) {
            throw new SubmissionValidationException("Không tìm thấy top-level public class.");
        }
        if (publicClasses.size() > 1) {
            throw new SubmissionValidationException("Chỉ được khai báo một top-level public class.");
        }

        String entryClassName = publicClasses.get(0);
        if (!entryClassName.equals(basename)) {
            throw new SubmissionValidationException("Tên public class " + entryClassName + " không trùng với tên file " + safeFileName + ".");
        }
        if (!hasMainMethod(sourceCode)) {
            throw new SubmissionValidationException("Không tìm thấy public static void main(String[] args).");
        }
        return new JavaSourceSubmission(safeFileName, entryClassName, sourceCode);
    }

    public JavaSourceSubmission validateOrInferFilename(String originalFileName, String sourceCode) {
        if (StringUtils.hasText(originalFileName)) {
            return validate(originalFileName, sourceCode);
        }
        List<String> publicClasses = topLevelPublicClasses(sourceCode);
        if (publicClasses.isEmpty()) {
            throw new SubmissionValidationException("Không tìm thấy top-level public class.");
        }
        if (publicClasses.size() > 1) {
            throw new SubmissionValidationException("Chỉ được khai báo một top-level public class.");
        }
        return validate(publicClasses.get(0) + ".java", sourceCode);
    }

    private String sanitizeFilename(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            throw new SubmissionValidationException("Tên file không phải là tên lớp Java hợp lệ.");
        }
        String trimmed = originalFileName.trim();
        if (trimmed.contains("/") || trimmed.contains("\\") || trimmed.contains("..")) {
            throw new SubmissionValidationException("Tên file không phải là tên lớp Java hợp lệ.");
        }
        if (!trimmed.endsWith(".java")) {
            throw new SubmissionValidationException("Chỉ chấp nhận một file .java.");
        }
        return trimmed;
    }

    private String basename(String fileName) {
        return fileName.substring(0, fileName.length() - ".java".length());
    }

    private boolean isJavaIdentifier(String value) {
        if (value.isEmpty() || JAVA_KEYWORDS.contains(value)) {
            return false;
        }
        int offset = 0;
        int first = value.codePointAt(offset);
        if (!Character.isJavaIdentifierStart(first)) {
            return false;
        }
        offset += Character.charCount(first);
        while (offset < value.length()) {
            int codePoint = value.codePointAt(offset);
            if (!Character.isJavaIdentifierPart(codePoint)) {
                return false;
            }
            offset += Character.charCount(codePoint);
        }
        return true;
    }

    private boolean declaresPackage(String sourceCode) {
        String masked = maskCommentsAndLiterals(sourceCode);
        return java.util.regex.Pattern.compile("(?m)^\\s*package\\s+[\\p{javaJavaIdentifierStart}][\\p{javaJavaIdentifierPart}.]*\\s*;")
            .matcher(masked)
            .find();
    }

    private List<String> topLevelPublicClasses(String sourceCode) {
        String masked = maskCommentsAndLiterals(sourceCode);
        java.util.ArrayList<String> classes = new java.util.ArrayList<>();
        int depth = 0;
        for (int i = 0; i < masked.length(); i += 1) {
            char ch = masked.charAt(i);
            if (ch == '{') {
                depth += 1;
                continue;
            }
            if (ch == '}') {
                depth = Math.max(0, depth - 1);
                continue;
            }
            if (depth == 0 && isDeclarationStart(masked, i)) {
                ClassDeclaration declaration = readClassDeclaration(masked, i);
                if (declaration != null && declaration.isPublic() && !declaration.className().isEmpty()) {
                    classes.add(declaration.className());
                    i = declaration.endIndex();
                }
            }
        }
        return classes;
    }

    private boolean isDeclarationStart(String value, int index) {
        return startsKeyword(value, index, "public") ||
            startsKeyword(value, index, "abstract") ||
            startsKeyword(value, index, "final") ||
            startsKeyword(value, index, "strictfp") ||
            startsKeyword(value, index, "sealed") ||
            startsKeyword(value, index, "non-sealed") ||
            value.charAt(index) == '@';
    }

    private ClassDeclaration readClassDeclaration(String value, int index) {
        int cursor = index;
        boolean publicModifier = false;
        while (cursor < value.length()) {
            cursor = skipWhitespace(value, cursor);
            while (cursor < value.length() && value.charAt(cursor) == '@') {
                cursor = skipAnnotation(value, cursor);
                cursor = skipWhitespace(value, cursor);
            }
            String token = readModifierToken(value, cursor);
            if (token.isEmpty()) {
                break;
            }
            if ("class".equals(token)) {
                int classNameStart = skipWhitespace(value, cursor + token.length());
                String className = readIdentifier(value, classNameStart);
                return new ClassDeclaration(publicModifier, className, classNameStart + className.length());
            }
            if (!CLASS_MODIFIERS.contains(token)) {
                break;
            }
            publicModifier = publicModifier || "public".equals(token);
            cursor += token.length();
        }
        return null;
    }

    private int skipAnnotation(String value, int index) {
        int cursor = index + 1;
        String annotationName = readQualifiedIdentifier(value, cursor);
        cursor += annotationName.length();
        cursor = skipWhitespace(value, cursor);
        if (cursor < value.length() && value.charAt(cursor) == '(') {
            int depth = 1;
            cursor += 1;
            while (cursor < value.length() && depth > 0) {
                char ch = value.charAt(cursor);
                if (ch == '(') {
                    depth += 1;
                } else if (ch == ')') {
                    depth -= 1;
                }
                cursor += 1;
            }
        }
        return cursor;
    }

    private String readModifierToken(String value, int index) {
        if (startsKeyword(value, index, "non-sealed")) {
            return "non-sealed";
        }
        return readIdentifier(value, index);
    }

    private String readQualifiedIdentifier(String value, int index) {
        int cursor = index;
        String first = readIdentifier(value, cursor);
        if (first.isEmpty()) {
            return "";
        }
        cursor += first.length();
        while (cursor < value.length() && value.charAt(cursor) == '.') {
            String next = readIdentifier(value, cursor + 1);
            if (next.isEmpty()) {
                break;
            }
            cursor += 1 + next.length();
        }
        return value.substring(index, cursor);
    }

    private boolean hasMainMethod(String sourceCode) {
        String masked = maskCommentsAndLiterals(sourceCode);
        return java.util.regex.Pattern.compile(
            "\\bpublic\\s+static\\s+void\\s+main\\s*\\(\\s*String\\s*\\[\\s*]\\s+[\\p{javaJavaIdentifierStart}][\\p{javaJavaIdentifierPart}]*\\s*\\)(?:\\s+throws\\b[^\\{;]*)?\\s*\\{"
        ).matcher(masked).find();
    }

    private boolean startsKeyword(String value, int index, String keyword) {
        int end = index + keyword.length();
        if (index < 0 || end > value.length() || !value.regionMatches(index, keyword, 0, keyword.length())) {
            return false;
        }
        return (index == 0 || !Character.isJavaIdentifierPart(value.charAt(index - 1))) &&
            (end == value.length() || !Character.isJavaIdentifierPart(value.charAt(end)));
    }

    private int skipWhitespace(String value, int index) {
        int cursor = index;
        while (cursor < value.length() && Character.isWhitespace(value.charAt(cursor))) {
            cursor += 1;
        }
        return cursor;
    }

    private String readIdentifier(String value, int index) {
        if (index >= value.length()) {
            return "";
        }
        int first = value.codePointAt(index);
        if (!Character.isJavaIdentifierStart(first)) {
            return "";
        }
        int cursor = index + Character.charCount(first);
        while (cursor < value.length()) {
            int codePoint = value.codePointAt(cursor);
            if (!Character.isJavaIdentifierPart(codePoint)) {
                break;
            }
            cursor += Character.charCount(codePoint);
        }
        return value.substring(index, cursor);
    }

    private String maskCommentsAndLiterals(String sourceCode) {
        StringBuilder result = new StringBuilder(sourceCode.length());
        int i = 0;
        while (i < sourceCode.length()) {
            char ch = sourceCode.charAt(i);
            char next = i + 1 < sourceCode.length() ? sourceCode.charAt(i + 1) : '\0';
            if (ch == '/' && next == '/') {
                result.append("  ");
                i += 2;
                while (i < sourceCode.length() && sourceCode.charAt(i) != '\n') {
                    result.append(' ');
                    i += 1;
                }
                continue;
            }
            if (ch == '/' && next == '*') {
                result.append("  ");
                i += 2;
                while (i < sourceCode.length()) {
                    char current = sourceCode.charAt(i);
                    char following = i + 1 < sourceCode.length() ? sourceCode.charAt(i + 1) : '\0';
                    result.append(current == '\n' ? '\n' : ' ');
                    i += 1;
                    if (current == '*' && following == '/') {
                        result.append(' ');
                        i += 1;
                        break;
                    }
                }
                continue;
            }
            if (ch == '"' || ch == '\'') {
                char quote = ch;
                result.append(' ');
                i += 1;
                while (i < sourceCode.length()) {
                    char current = sourceCode.charAt(i);
                    result.append(current == '\n' ? '\n' : ' ');
                    i += 1;
                    if (current == '\\' && i < sourceCode.length()) {
                        result.append(sourceCode.charAt(i) == '\n' ? '\n' : ' ');
                        i += 1;
                    } else if (current == quote) {
                        break;
                    }
                }
                continue;
            }
            result.append(ch);
            i += 1;
        }
        return result.toString();
    }

    private record ClassDeclaration(boolean isPublic, String className, int endIndex) {}
}
