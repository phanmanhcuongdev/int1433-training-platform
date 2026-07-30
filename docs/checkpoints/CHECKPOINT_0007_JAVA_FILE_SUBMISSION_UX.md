# Checkpoint 0007 - Java File Submission UX

Date: 2026-07-30

Version: `0.5.2`

## Purpose

Improve the `JAVA_CODE` practice workflow without changing the judge architecture or the `NETWORK_CHALLENGE` flow.

## Completed

- Practice pages for Java code exercises now show the full statement, objectives, processing requirements, input/output contract, examples, hints and common failures on the same page as submission.
- Initial Java submission uses one uploaded UTF-8 `.java` file instead of a textarea.
- Required filename is derived consistently as `<exercise-id>.java`.
- Backend repeats critical validation: one file, exact filename, `.java`, non-empty, UTF-8, maximum 20 KB and `public class Main`.
- Submission records persist original filename, source SHA-256 and source text.
- Submission detail shows verdict, outputs, hidden test summary and submitted source.
- Submitted source can be copied, edited and resubmitted as a new submission.

## Explicit Non-Goals

- No "Chạy thử" button.
- No custom input runner.
- No browser IDE beyond a simple source editor for resubmission.
- No change to TCP/UDP/RMI/SOAP network challenge behavior.

## Verification

```bash
npm run validate
npm run content:import:dry
npm run content:import
npm run content:check
npm run starters:check
npm run frontend:build
npm run backend:test
npm run check
git diff --check
```

## Next Allowed Tasks

- Add focused frontend component tests for upload validation and resubmission UI.
- Improve source viewer styling.
- Add more public examples to exercise content where useful.
