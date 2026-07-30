# 04 - Exercise Schema

## Schema De Xuat

Bat buoc:

- `id`
- `title`
- `protocol`
- `technology`
- `difficulty`
- `level`
- `source_label`
- `source_claim_ids`
- `statement`
- `server_contract`
- `request_format`
- `response_format`
- `processing_requirement`
- `submission_format`
- `common_failures`
- `tags`
- `estimated_time`

Optional:

- `transport`
- `stream_type`
- `prerequisites`
- `timeout`
- `starter_code`
- `hidden_cases`
- `hints`
- `solution`
- `explanation`
- `extensions`
- `judge`

Field phuc vu website:

- `id`, `title`, `protocol`, `technology`, `difficulty`, `level`, `tags`, `estimated_time`, `statement`, `hints`, `solution`.

Field phuc vu judge:

- `server_contract`, `request_format`, `response_format`, `submission_format`, `timeout`, `hidden_cases`, `judge`.

Field phuc vu traceability:

- `source_label`, `source_claim_ids`, `source_files`, `evidence_notes`.

## Validation Rules

- `source_label` phai la mot trong `OBSERVED`, `STRONG_PATTERN`, `EXTENDED`, `CHALLENGE`.
- Bai Exam Track bat buoc co `source_claim_ids`.
- Neu `technology=RMI`, bat buoc co `service_name`, `interface_package`.
- Neu `technology=WebService`, bat buoc co `wsdl`, `request_method`, `submit_method`.
- Neu `protocol=UDP`, bat buoc mo ta `request_id`.
- Khong hard-code IP/port hien hanh trong `statement`; dung placeholder `<HOST>`, `<PORT>` hoac config mock.
- `solution` co the an trong UI/mock exam.

## YAML Example - UDP String

```yaml
id: udp-string-001
title: Normalize Words With RequestId
track: Exam
protocol: UDP
transport: Datagram
stream_type: String
technology: UDP
difficulty: Easy
level: L2
source_label: OBSERVED
source_claim_ids:
  - CLAIM-UDP-001
  - CLAIM-FAILURE-002
source_files:
  - INT1433_exam_research/04_EXAM_PATTERNS.md
prerequisites:
  - Java DatagramSocket
  - String split/join
statement: >
  Build a UDP client. The mock server sends a requestId and a lowercase text.
  Submit the normalized text with the same requestId.
server_contract:
  host: "<HOST>"
  port: "<PORT>"
  request_id:
    type: prefix
    format: "requestId;data"
request_format: ";{studentCode};{qCode}"
response_format: "{requestId};{rawText}"
processing_requirement: "Capitalize the first letter of each word and lowercase the rest."
submission_format: "{requestId};{normalizedText}"
timeout:
  connect_ms: null
  read_ms: 5000
starter_code:
  java: "starters/udp-string/Main.java"
hidden_cases:
  - multiple_spaces
  - leading_trailing_spaces
  - single_word
common_failures:
  - missing_request_id
  - using_full_buffer_instead_of_packet_length
  - wrong_delimiter
hints:
  - "Use packet.getLength() when converting bytes to String."
solution:
  java: "solutions/udp-string-001/Main.java"
explanation: "The server maps the answer by requestId, not by console output."
tags:
  - udp
  - request-id
  - string
estimated_time: 25m
judge:
  verdicts:
    - AC
    - WA
    - Protocol Error
    - Timeout
```

## Traceability

Moi bai co the sinh trang "Evidence" hien:

- Claim ID nao lien quan.
- Bai nay la OBSERVED hay EXTENDED.
- Neu EXTENDED/CHALLENGE, noi ro khac de thi o dau.

