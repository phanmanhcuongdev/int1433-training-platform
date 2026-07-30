# Contributing

This project is an unofficial INT1433 training platform. Contributions should make the material easier to learn, practice, debug, or review.

## Rules

- Do not present this project as an official PTIT exam system.
- Do not hard-code old IPs or ports as current rules.
- Every Exam Track exercise must include traceability through `source_claim_ids` or be clearly marked as `EXTENDED`.
- Distinguish `OBSERVED`, `STRONG_PATTERN`, `EXTENDED`, and `CHALLENGE`.
- Do not commit secrets, credentials, `.env` files, build output, or dependency folders.
- Do not create an exercise that only changes numbers, names, or variable labels from another exercise.
- Commit messages should be clear and scoped.

## Content Review Checklist

- The exercise has both payload logic and protocol contract.
- Console output is not described as submission.
- The exercise avoids real PTIT server IP/port assumptions.
- Common failures include protocol mistakes, not only algorithm mistakes.
- Draft exercises use `status: "DRAFT"`.

