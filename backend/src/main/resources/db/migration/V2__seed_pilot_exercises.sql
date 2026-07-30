insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement,
    request_format, response_format, submission_format, estimated_time_minutes,
    display_order, server_contract, timeout_config
) values
(
    'fnd-character-flush-001',
    'Character Stream Flush Drill',
    'Luyện hành vi Reader/Writer trong Java trước khi chuyển sang character stream qua socket.',
    'DRAFT',
    'EXAM',
    'Java I/O',
    null,
    null,
    null,
    'EASY',
    'L0',
    'EXTENDED',
    'Luyện một contract character stream theo dòng ở môi trường cục bộ. Bài này là prerequisite cho TCP character stream và không kết nối tới server thi thật.',
    'Đọc một dòng đầu vào, chuyển nội dung sang chữ hoa, ghi lại dòng đã xử lý, sau đó gọi flush() cho writer.',
    null,
    null,
    null,
    15,
    10,
    null,
    null
),
(
    'tcp-character-001',
    'TCP Character Stream Word Normalization',
    'Luyện contract TCP character stream gồm request, response, xử lý payload và submission.',
    'DRAFT',
    'EXAM',
    'TCP',
    'TCP',
    'Socket',
    'Character Stream',
    'EASY',
    'L2',
    'OBSERVED',
    'Viết Java TCP client cho mock server tại <HOST>:<PORT>. Client gửi studentCode và qCode, nhận một dòng text, chuẩn hóa từng từ, rồi gửi lại một dòng kết quả cho mock server. Đây là bài luyện tập, không phải đề thi chính thức của PTIT.',
    'Viết hoa chữ cái đầu của mỗi từ và chuyển phần còn lại thành chữ thường.',
    '{studentCode};{qCode}',
    '{rawText}',
    '{normalizedText}',
    25,
    20,
    '{"host":"<HOST>","port":"<PORT>","request":"studentCode;qCode","response":"one text line","submission":"one text line"}'::jsonb,
    '{"read_ms":5000}'::jsonb
),
(
    'udp-string-request-id-001',
    'UDP String RequestId Echo',
    'Luyện giữ nguyên requestId trong contract UDP string.',
    'DRAFT',
    'EXAM',
    'UDP',
    'UDP',
    'Datagram',
    'String',
    'EASY',
    'L2',
    'OBSERVED',
    'Viết Java UDP client cho mock server tại <HOST>:<PORT>. Server trả về requestId và payload dạng chuỗi. Client gửi kết quả đã xử lý kèm đúng requestId. Đây là bài luyện tập, không phải đề thi chính thức của PTIT.',
    'Đảo ngược payload string và giữ nguyên requestId nhận được từ server response.',
    ';{studentCode};{qCode}',
    '{requestId};{data}',
    '{requestId};{reversedData}',
    25,
    30,
    '{"host":"<HOST>","port":"<PORT>","request":";studentCode;qCode","response":"requestId;data","submission":"requestId;processedData"}'::jsonb,
    '{"read_ms":5000}'::jsonb
);

insert into exercise_tags (exercise_id, tag) values
('fnd-character-flush-001', 'foundation'),
('fnd-character-flush-001', 'character-stream'),
('fnd-character-flush-001', 'flush'),
('tcp-character-001', 'tcp'),
('tcp-character-001', 'character-stream'),
('tcp-character-001', 'mock-contract'),
('udp-string-request-id-001', 'udp'),
('udp-string-request-id-001', 'request-id'),
('udp-string-request-id-001', 'string');

insert into exercise_common_failures (exercise_id, failure_code, display_order) values
('fnd-character-flush-001', 'missing_flush', 1),
('fnd-character-flush-001', 'missing_newline', 2),
('fnd-character-flush-001', 'readline_blocking', 3),
('tcp-character-001', 'missing_newline', 1),
('tcp-character-001', 'missing_flush', 2),
('tcp-character-001', 'wrong_request_format', 3),
('tcp-character-001', 'console_output_used_as_submission', 4),
('udp-string-request-id-001', 'missing_request_id', 1),
('udp-string-request-id-001', 'using_full_buffer_instead_of_packet_length', 2),
('udp-string-request-id-001', 'wrong_udp_request_prefix', 3),
('udp-string-request-id-001', 'stale_port_assumption', 4);

insert into exercise_hints (exercise_id, content, display_order) values
('fnd-character-flush-001', 'BufferedWriter có thể giữ dữ liệu trong bộ đệm cho tới khi flush() được gọi.', 1),
('tcp-character-001', 'Gửi dữ liệu qua socket. Nội dung in ra console không phải submission.', 1),
('udp-string-request-id-001', 'Khi chuyển byte sang text, dùng độ dài packet thực tế thay vì toàn bộ receive buffer.', 1);

insert into exercise_learning_objectives (exercise_id, objective, display_order) values
('fnd-character-flush-001', 'Hiểu vì sao output character stream có buffer cần được flush.', 1),
('fnd-character-flush-001', 'Nhận diện tình huống blocking trong input theo dòng.', 2),
('tcp-character-001', 'Tuân thủ contract TCP line-based gồm request, response và submission.', 1),
('tcp-character-001', 'Tách submission qua socket khỏi console output.', 2),
('udp-string-request-id-001', 'Giữ nguyên requestId qua cặp UDP response/submission.', 1),
('udp-string-request-id-001', 'Dùng datagram packet length thay vì toàn bộ receive buffer.', 2);

insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values
('fnd-character-flush-001', 'Java Reader/Writer', 1),
('fnd-character-flush-001', 'BufferedReader', 2),
('fnd-character-flush-001', 'BufferedWriter', 3);

insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values
('tcp-character-001', 'CLAIM-TCP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern TCP character stream trong research snapshot.'),
('tcp-character-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Các lỗi giao thức thường gặp.'),
('udp-string-request-id-001', 'CLAIM-UDP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern UDP requestId trong research snapshot.'),
('udp-string-request-id-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Các lỗi giao thức thường gặp.');
