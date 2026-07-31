# Checkpoint 0008 - Dynamic Java Entry Class

Date: 2026-07-31

Version: `0.6.0`

## Purpose

Replace the artificial `<exercise-id>.java` plus `public class Main` submission contract with the natural Java rule: the file basename must match the single top-level public class.

## Completed

- `JAVA_CODE` uploads accept one UTF-8 `.java` file whose basename is a valid Java identifier.
- Backend rejects path-like filenames, package declarations, missing public class, multiple top-level public classes, filename/class mismatches and missing `public static void main(String[] args)`.
- Java submissions persist `originalFileName`, `entryClassName`, source text and SHA-256.
- Runner writes the validated original filename, compiles it with `javac -encoding UTF-8 <filename>` and runs `java <entryClassName>` using argument lists.
- Inline source resubmission includes an editable filename field and creates a new submission.
- Historical `Main.java`/`Main` submissions remain readable.

## Verification

```bash
npm run check
git diff --check
```
