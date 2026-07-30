-- Generated from content/exercises/*.json by scripts/generate-seed-migration.mjs.
-- Do not edit committed Flyway migrations after release.
delete from exercise_tags where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_common_failures where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_hints where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_learning_objectives where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_prerequisites where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_sources where exercise_id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercises where id not in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');

insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'fnd-character-flush-001', 'Character Stream và flush()', 'Nộp một lớp Java đọc một dòng UTF-8 từ stdin, chuẩn hóa khoảng trắng và chữ hoa/thường, ghi đúng một dòng ra stdout rồi flush.', 'REVIEWED',
    'EXAM', 'Java I/O', null, 'stdin/stdout',
    'Character Stream', 'EASY', 'L0', 'EXTENDED',
    'Viết chương trình Java một file. Chương trình đọc tối đa một dòng UTF-8 từ stdin, bỏ khoảng trắng thừa ở hai đầu, gom nhiều khoảng trắng liên tiếp thành một dấu cách, chuyển toàn bộ chữ cái sang chữ hoa theo Unicode mặc định, in đúng một dòng kết quả ra stdout và gọi flush(). Bài này luyện lỗi buffer/flush trước khi làm TCP character stream. Đây không phải đề thi chính thức của PTIT.', 'trim, gom khoảng trắng bằng một dấu cách, chuyển sang chữ hoa và in một dòng.', 'Một dòng UTF-8 trên stdin. Có thể không có ký tự newline cuối file.',
    'Không có server response.', 'Một file Java chứa public class Main.', 20, 10,
    '{"mode":"JAVA_CODE","entrypoint":"public class Main","input":"stdin UTF-8","output":"stdout UTF-8 one line"}'::jsonb, '{"compile_ms":10000,"run_ms":3000}'::jsonb, 'JAVA_CODE', 'java.fnd.character_flush.v1',
    3000, 256, null,
    null, null, '["JAVA"]'::jsonb,
    '{"AC":"Mọi hidden test đúng.","WA":"Chương trình chạy xong nhưng output sai.","CE":"Không biên dịch được bằng javac 21.","RE":"Runtime exception hoặc exit code khác 0.","TLE":"Quá thời gian chạy."}'::jsonb, '["Không đọc quá một dòng đầu tiên.","Không yêu cầu package.","Không dùng thư viện ngoài JDK 21.","Output stderr không được tính là đáp án."]'::jsonb, '[{"input":"hello","output":"HELLO"},{"input":"  Lap   trinh  mang  ","output":"LAP TRINH MANG"},{"input":"xin Chào mạng","output":"XIN CHÀO MẠNG"}]'::jsonb, 'Bài mở rộng từ lỗi flush/blocking thường gặp trong research; không phải đề thi chính thức.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'fnd-data-order-001', 'Thứ tự primitive trong Data Stream', 'Nộp Java code đọc primitive nhị phân bằng DataInputStream và ghi kết quả nhị phân bằng DataOutputStream đúng thứ tự.', 'REVIEWED',
    'EXAM', 'Java I/O', null, 'stdin/stdout',
    'Data Stream', 'MEDIUM', 'L1', 'EXTENDED',
    'Viết chương trình Java đọc một byte stream nhị phân từ stdin bằng DataInputStream theo thứ tự: int a, int b, long c, double d. Chương trình ghi ra stdout bằng DataOutputStream theo thứ tự: int gcd(a,b), long lcm(a,b), long c + a + b, double d * 2.0. Đây là bài luyện binary contract; không được chuyển input thành text.', 'Tính gcd, lcm, tổng long và double nhân đôi từ primitive đã đọc.', 'int a, int b, long c, double d theo chuẩn DataInputStream của Java.',
    'Không có server response.', 'int gcd, long lcm, long total, double doubled theo chuẩn DataOutputStream.', 30, 20,
    '{"mode":"JAVA_CODE","entrypoint":"public class Main","input":"stdin binary primitives","output":"stdout binary primitives"}'::jsonb, '{"compile_ms":10000,"run_ms":3000}'::jsonb, 'JAVA_CODE', 'java.fnd.data_order.v1',
    3000, 256, null,
    null, null, '["JAVA"]'::jsonb,
    '{"AC":"Mọi hidden test đúng cả giá trị và thứ tự primitive.","WA":"Protocol binary đúng nhưng giá trị sai.","CE":"Không biên dịch được.","RE":"Runtime exception hoặc stream lỗi.","TLE":"Quá thời gian chạy."}'::jsonb, '["1 <= a,b <= 100000","Giá trị lcm nằm trong long.","Không in text ra stdout.","Không dùng package."]'::jsonb, '[{"input":"a=12,b=18,c=5,d=1.5","output":"gcd=6,lcm=36,total=35,doubled=3.0"},{"input":"a=7,b=13,c=100,d=2.25","output":"gcd=1,lcm=91,total=120,doubled=4.5"},{"input":"a=21,b=21,c=0,d=-1.0","output":"gcd=21,lcm=21,total=42,doubled=-2.0"}]'::jsonb, 'Bài mở rộng để củng cố Data Stream trước khi vào TCP Data Stream; không phải đề thi chính thức.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'tcp-byte-prime-sum-001', 'TCP Byte Stream — Tổng các số nguyên tố', 'Bắt đầu phiên TCP byte stream, nhận payload nhị phân bị phân mảnh, tính số lượng và tổng các số nguyên tố rồi gửi kết quả.', 'REVIEWED',
    'EXAM', 'TCP', 'TCP', 'Socket',
    'Byte Stream', 'MEDIUM', 'L3', 'OBSERVED',
    'Bấm bắt đầu phiên để nhận host, port, token và qCode. Client Java của bạn kết nối TCP, gửi request token;qCode bằng UTF-8 có newline, nhận danh sách số nguyên được frame bằng 4 byte độ dài rồi payload byte. Server cố ý chia payload thành nhiều lần ghi, vì vậy client phải đọc đủ số byte theo frame, không giả định một lần read() là đủ.', 'Đếm số nguyên tố và tính tổng các số nguyên tố trong payload.', '{token};{qCode}\n',
    '4 byte length big-endian + UTF-8 string n;v1,v2,...,vn', '{primeCount};{primeSum}\n', 45, 30,
    '{"request":"token;qCode\\n","response":"int length, then UTF-8 payload: n;v1,v2,...,vn","submission":"primeCount;primeSum\\n","fragmented_response":true}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.tcp.byte_prime_sum.v1',
    5000, null, 600,
    null, 'assets/starters/tcp-byte-prime-sum-001', '["JAVA"]'::jsonb,
    '{"AC":"Request, read frame và submission đúng.","WA":"Protocol đúng nhưng prime count/sum sai.","PROTOCOL_ERROR":"Sai token/qCode, không đọc đúng frame hoặc format sai.","TLE":"Không kết nối hoặc không gửi kết quả trước timeout."}'::jsonb, '["5 <= n <= 40","0 <= vi <= 100000","Payload có thể bị fragment thành nhiều chunk."]'::jsonb, '[{"payload":"5;2,4,5,9,11","submission":"3;18"},{"payload":"4;1,6,8,10","submission":"0;0"},{"payload":"3;97,100,101","submission":"2;198"}]'::jsonb, 'Pattern TCP và lỗi partial read được rút từ research; host/port là của platform luyện tập, không phải server thi PTIT.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'tcp-data-gcd-lcm-001', 'TCP Data Stream — GCD, LCM, tổng và tích', 'Client TCP dùng DataInputStream/DataOutputStream, đọc primitive đúng thứ tự và gửi lại bốn kết quả primitive đúng thứ tự.', 'REVIEWED',
    'EXAM', 'TCP', 'TCP', 'Socket',
    'Data Stream', 'MEDIUM', 'L3', 'OBSERVED',
    'Client kết nối TCP, gửi token và qCode qua DataOutputStream bằng writeUTF(token), writeUTF(qCode), flush. Server gửi int a, int b, int c, int d. Client gửi lại theo đúng thứ tự: int gcd(a,b), long lcm(a,b), int sum(a,b,c,d), long product(a,b,c,d).', 'Tính gcd/lcm của a,b; tổng và tích của bốn số.', 'DataOutputStream.writeUTF(token); writeUTF(qCode); flush()',
    'DataInputStream.readInt() bốn lần.', 'writeInt(gcd); writeLong(lcm); writeInt(sum); writeLong(product); flush()', 45, 40,
    '{"request":"writeUTF(token), writeUTF(qCode)","response":"readInt a,b,c,d","submission":"writeInt gcd, writeLong lcm, writeInt sum, writeLong product"}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.tcp.data_gcd_lcm.v1',
    5000, null, 600,
    null, 'assets/starters/tcp-data-gcd-lcm-001', '["JAVA"]'::jsonb,
    '{"AC":"Protocol và giá trị đúng.","WA":"Primitive sequence đúng nhưng giá trị sai.","PROTOCOL_ERROR":"Sai thứ tự primitive hoặc dùng stream không đúng.","TLE":"Không gửi đủ dữ liệu trước timeout."}'::jsonb, '["1 <= a,b,c,d <= 5000","product nằm trong long","Sai thứ tự primitive là Protocol Error."]'::jsonb, '[{"payload":"12,18,3,4","submission":"6,36,37,2592"},{"payload":"7,13,2,5","submission":"1,91,27,910"},{"payload":"21,21,1,2","submission":"21,21,45,882"}]'::jsonb, 'Pattern Data Stream được dùng để luyện kỹ năng thi; không cố định host/port theo PTIT.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'tcp-character-normalize-001', 'TCP Character Stream — Chuẩn hóa từ', 'Client TCP line-based nhận một dòng text, chuẩn hóa từng từ và gửi lại một dòng kết quả có newline.', 'REVIEWED',
    'EXAM', 'TCP', 'TCP', 'Socket',
    'Character Stream', 'EASY', 'L2', 'OBSERVED',
    'Client kết nối TCP, gửi request line token;qCode, nhận một dòng text, chuẩn hóa mỗi từ thành chữ cái đầu viết hoa và phần còn lại viết thường, rồi gửi lại đúng một dòng. Newline và flush là bắt buộc để server quan sát submission.', 'Gom khoảng trắng, viết hoa chữ cái đầu mỗi từ và viết thường phần còn lại.', '{token};{qCode}\n',
    '{rawText}\n', '{normalizedText}\n', 35, 50,
    '{"request":"token;qCode\\n","response":"raw text line","submission":"normalized text line"}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.tcp.character_normalize.v1',
    5000, null, 600,
    null, 'assets/starters/tcp-character-normalize-001', '["JAVA"]'::jsonb,
    '{"AC":"Submission line đúng.","WA":"Format line đúng nhưng nội dung chuẩn hóa sai.","PROTOCOL_ERROR":"Sai request hoặc gửi nhiều dòng không hợp lệ.","TLE":"Không gửi newline/flush hoặc quá hạn."}'::jsonb, '["Text có thể chứa tiếng Việt Unicode.","Khoảng trắng lặp phải được gom.","Thiếu newline/flush có thể thành TLE."]'::jsonb, '[{"payload":"nGUYEN   vAn a","submission":"Nguyen Van A"},{"payload":"lẬp TRÌNH   mẠng","submission":"Lập Trình Mạng"},{"payload":"  tcp  udp ","submission":"Tcp Udp"}]'::jsonb, 'Pattern TCP character stream dựa trên research snapshot; server là mock server của platform.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'tcp-object-product-001', 'TCP Object Stream — Chuẩn hóa Product', 'Client TCP Object Stream nhận Product Serializable đúng package, chỉnh dữ liệu và gửi lại object theo contract.', 'REVIEWED',
    'EXAM', 'TCP', 'TCP', 'Socket',
    'Object Stream', 'HARD', 'L4', 'OBSERVED',
    'Client kết nối TCP Object Stream. Tạo ObjectOutputStream trước, gửi token và qCode bằng writeUTF rồi flush. Server trả về object Product thuộc package vn.edu.ptit.int1433.training.contract. Client chuẩn hóa name, làm tròn price về 2 chữ số thập phân, đặt normalized=true và gửi lại Product.', 'Gom khoảng trắng name, viết hoa chữ cái đầu mỗi từ, làm tròn price 2 chữ số, set normalized=true.', 'writeUTF(token); writeUTF(qCode); flush()',
    'Product(id:String, name:String, price:double, normalized:boolean)', 'writeObject(product); flush()', 60, 60,
    '{"request":"ObjectOutputStream.writeUTF(token), writeUTF(qCode), flush","response":"Product object","submission":"Product object","class":"vn.edu.ptit.int1433.training.contract.Product"}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.tcp.object_product.v1',
    5000, null, 600,
    null, 'assets/starters/tcp-object-product-001', '["JAVA"]'::jsonb,
    '{"AC":"Object contract và dữ liệu đúng.","WA":"Object đúng kiểu nhưng dữ liệu xử lý sai.","PROTOCOL_ERROR":"Sai package, serialVersionUID, request order hoặc object type.","TLE":"Deadlock stream hoặc quá hạn."}'::jsonb, '["Product phải Serializable.","serialVersionUID = 1433001L.","Sai package hoặc field type là Protocol Error."]'::jsonb, '[{"payload":"name=''  usb   cable '', price=12.345","submission":"name=''Usb Cable'', price=12.35, normalized=true"},{"payload":"name=''java BOOK'', price=50.0","submission":"name=''Java Book'', price=50.0, normalized=true"},{"payload":"name=''mạng máy tính'', price=19.994","submission":"name=''Mạng Máy Tính'', price=19.99, normalized=true"}]'::jsonb, 'Bài luyện Object Stream theo pattern exam contract; không dùng object từ server PTIT thật.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'udp-string-request-id-001', 'UDP String — Giữ nguyên requestId', 'Client UDP nhận requestId;payload, xử lý payload và gửi lại requestId;answer với đúng datagram length.', 'REVIEWED',
    'EXAM', 'UDP', 'UDP', 'Datagram',
    'String', 'EASY', 'L2', 'OBSERVED',
    'Bắt đầu phiên để nhận host, port, token và qCode. Client gửi datagram token;qCode. Server trả về requestId;payload. Client đảo ngược payload theo ký tự Unicode và gửi requestId;answer. Server chỉ chấm datagram thật nhận được, không chấm console output.', 'Đảo ngược payload và giữ nguyên requestId.', '{token};{qCode}',
    '{requestId};{payload}', '{requestId};{reversedPayload}', 35, 70,
    '{"request":"token;qCode","response":"requestId;payload","submission":"requestId;reversedPayload","stateless_datagram":true}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.udp.string_request_id.v1',
    5000, null, 600,
    null, 'assets/starters/udp-string-request-id-001', '["JAVA"]'::jsonb,
    '{"AC":"requestId và answer đúng.","WA":"requestId đúng nhưng answer sai.","PROTOCOL_ERROR":"Thiếu/sai requestId, sai delimiter hoặc dùng requestId phiên khác.","TLE":"Không nhận được submission trước timeout."}'::jsonb, '["requestId thay đổi theo phiên.","Không dùng toàn bộ receive buffer.","Sai delimiter là Protocol Error."]'::jsonb, '[{"payload":"abc","submission":"requestId;cba"},{"payload":"lập trình","submission":"requestId;hnìrt pậl"},{"payload":"A  B","submission":"requestId;B  A"}]'::jsonb, 'Pattern UDP requestId dựa trên research; requestId/host/port trong platform không đại diện cho kỳ thi hiện hành.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'udp-object-product-001', 'UDP Object — Sửa dữ liệu Product', 'Client UDP nhận datagram gồm 8 byte requestId và Product serialized, sửa object rồi gửi lại requestId kèm object hợp lệ.', 'REVIEWED',
    'EXAM', 'UDP', 'UDP', 'Datagram',
    'Object', 'HARD', 'L4', 'OBSERVED',
    'Client gửi token;qCode bằng UDP. Server trả một datagram: 8 byte requestId, sau đó là object Product serialized bằng Java. Client phải giữ nguyên đúng 8 byte đầu, sửa Product theo yêu cầu và gửi lại một datagram gồm requestId + object serialized. Server deserialize bằng allowlist class contract.', 'Giảm quantity còn một nửa làm tròn xuống, chuẩn hóa name và set normalized=true.', '{token};{qCode}',
    'byte[8] requestId + Product object bytes', 'byte[8] sameRequestId + serialized Product', 60, 80,
    '{"request":"UTF-8 token;qCode","response":"8 raw bytes requestId + serialized Product","submission":"same 8 requestId bytes + serialized Product","max_datagram_bytes":4096}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.udp.object_product.v1',
    5000, null, 600,
    null, 'assets/starters/udp-object-product-001', '["JAVA"]'::jsonb,
    '{"AC":"requestId bytes và Product đúng.","WA":"Object đúng contract nhưng dữ liệu sai.","PROTOCOL_ERROR":"Sai requestId, datagram format, class hoặc package.","TLE":"Không gửi datagram hợp lệ trước timeout."}'::jsonb, '["Datagram tối đa 4096 byte.","Product package cố định.","Deserializer chỉ cho phép Product và JDK core types cần thiết."]'::jsonb, '[{"payload":"name=''  mouse '', quantity=5","submission":"name=''Mouse'', quantity=2, normalized=true"},{"payload":"name=''usb hub'', quantity=8","submission":"name=''Usb Hub'', quantity=4, normalized=true"},{"payload":"name=''cáp mạng'', quantity=1","submission":"name=''Cáp Mạng'', quantity=0, normalized=true"}]'::jsonb, 'Bài luyện UDP object theo pattern research; không deserialize arbitrary class ngoài allowlist.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'rmi-data-pythagorean-001', 'RMI DataService — Bộ ba Pythagore', 'Client Java RMI lookup DataService, request dữ liệu phiên và submit bộ ba Pythagore đã chuẩn hóa.', 'REVIEWED',
    'EXAM', 'RMI', 'RMI', 'Java RMI Registry',
    'DataService', 'HARD', 'L5', 'OBSERVED',
    'Bắt đầu phiên để nhận registry host, registry port, service name, token và qCode. Client Java lookup DataService, gọi request(token, qCode) để nhận mảng số nguyên, tìm mọi bộ ba Pythagore a,b,c trong đó a*a + b*b = c*c, a < b < c, rồi submit danh sách bộ ba đã sort.', 'Tìm mọi bộ ba Pythagore từ values, sort từng bộ ba tăng dần và sort danh sách theo a,b,c.', 'int[] request(String token, String qCode) throws RemoteException',
    'int[] values', 'boolean submit(String token, String qCode, List<int[]> triples) throws RemoteException', 60, 90,
    '{"registry_host":"<HOST>","registry_port":"<RMI_REGISTRY_PORT>","service_name":"DataService_{sessionId}","interface":"vn.edu.ptit.int1433.training.contract.rmi.DataService"}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.rmi.data_pythagorean.v1',
    5000, null, 600,
    null, 'assets/starters/rmi-data-pythagorean-001', '["JAVA"]'::jsonb,
    '{"AC":"Request và submit đúng qua RMI.","WA":"RMI contract đúng nhưng danh sách bộ ba sai.","PROTOCOL_ERROR":"Sai service, interface, token hoặc qCode.","TLE":"Không submit trước khi phiên hết hạn."}'::jsonb, '["RMI Java-only.","Sai service name là Protocol Error.","Không gửi bộ ba chứa số ngoài payload."]'::jsonb, '[{"payload":"[3,4,5,6]","submission":"[[3,4,5]]"},{"payload":"[5,12,13,8,15,17]","submission":"[[5,12,13],[8,15,17]]"},{"payload":"[1,2,3]","submission":"[]"}]'::jsonb, 'RMI là Java-specific trong Exam Track; service name/port là của platform luyện tập.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();
insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    'ws-data-factorization-001', 'Web Service DataService — Phân tích thừa số nguyên tố', 'Client SOAP gọi request để nhận số nguyên và submit danh sách thừa số nguyên tố theo thứ tự tăng dần.', 'REVIEWED',
    'EXAM', 'SOAP Web Service', 'SOAP', 'HTTP',
    'DataService', 'HARD', 'L5', 'OBSERVED',
    'Bắt đầu phiên để nhận endpoint SOAP, WSDL URL, token và qCode. Client dùng generated client hoặc SOAP request thủ công để gọi request(token, qCode), nhận n, phân tích n thành thừa số nguyên tố tăng dần và gọi submit(token, qCode, factors). Đây là SOAP Web Service, không thay bằng REST.', 'Trả về danh sách thừa số nguyên tố của n theo thứ tự tăng dần, có lặp.', 'request(token, qCode) -> int n',
    'Một số nguyên n.', 'submit(token, qCode, int[] factors) -> boolean', 60, 100,
    '{"wsdl":"http://<HOST>:<SOAP_PORT>/ws/ws-data-factorization-001/{sessionId}?wsdl","endpoint":"http://<HOST>:<SOAP_PORT>/ws/ws-data-factorization-001/{sessionId}","service_name":"DataService","port_name":"DataServicePort"}'::jsonb, '{"session_ttl_seconds":600,"io_ms":5000}'::jsonb, 'NETWORK_CHALLENGE', 'net.soap.data_factorization.v1',
    5000, null, 600,
    null, 'assets/starters/ws-data-factorization-001', '["JAVA"]'::jsonb,
    '{"AC":"SOAP request/submit đúng và factors đúng.","WA":"SOAP contract đúng nhưng factors sai.","PROTOCOL_ERROR":"Sai endpoint, method, namespace, token hoặc qCode.","TLE":"Không submit trước khi phiên hết hạn."}'::jsonb, '["2 <= n <= 2000000000","SOAP endpoint theo phiên.","Không dùng REST endpoint để nộp bài."]'::jsonb, '[{"payload":"84","submission":"[2,2,3,7]"},{"payload":"97","submission":"[97]"},{"payload":"360","submission":"[2,2,2,3,3,5]"}]'::jsonb, 'Bài giữ SOAP cho Exam Track; endpoint là mock service của platform, không phải service thi thật.'
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();

delete from exercise_tags where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_common_failures where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_hints where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_learning_objectives where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_prerequisites where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
delete from exercise_sources where exercise_id in ('fnd-character-flush-001', 'fnd-data-order-001', 'tcp-byte-prime-sum-001', 'tcp-data-gcd-lcm-001', 'tcp-character-normalize-001', 'tcp-object-product-001', 'udp-string-request-id-001', 'udp-object-product-001', 'rmi-data-pythagorean-001', 'ws-data-factorization-001');
insert into exercise_tags (exercise_id, tag) values ('fnd-character-flush-001', 'foundation');
insert into exercise_tags (exercise_id, tag) values ('fnd-character-flush-001', 'character-stream');
insert into exercise_tags (exercise_id, tag) values ('fnd-character-flush-001', 'flush');
insert into exercise_tags (exercise_id, tag) values ('fnd-character-flush-001', 'java-code');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-character-flush-001', 'missing_flush', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-character-flush-001', 'missing_newline', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-character-flush-001', 'readline_blocking', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-character-flush-001', 'wrong_encoding', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-character-flush-001', 'extra_debug_stdout', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-character-flush-001', 'Dùng readLine() một lần là đủ cho bài này.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-character-flush-001', 'Gom khoảng trắng sau khi trim để tránh sinh thêm khoảng trắng ở đầu hoặc cuối.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-character-flush-001', 'Gọi writer.flush() sau khi ghi newline.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-character-flush-001', 'Dùng BufferedReader và BufferedWriter đúng vai trò.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-character-flush-001', 'Hiểu vì sao writer có buffer cần flush trước khi chương trình kết thúc hoặc trước khi chờ phản hồi.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-character-flush-001', 'Phân biệt stdout được grader đọc với log debug không cần thiết.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-character-flush-001', 'Java cơ bản', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-character-flush-001', 'BufferedReader', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-character-flush-001', 'BufferedWriter', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-character-flush-001', 'UTF-8 text', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('fnd-character-flush-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Bài mở rộng từ lỗi flush/blocking thường gặp trong research; không phải đề thi chính thức.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('fnd-character-flush-001', 'CLAIM-PLATFORM-FOUNDATION-001', 'docs/research/platform/01_EXERCISE_TAXONOMY.md', 'Bài mở rộng từ lỗi flush/blocking thường gặp trong research; không phải đề thi chính thức.');
insert into exercise_tags (exercise_id, tag) values ('fnd-data-order-001', 'foundation');
insert into exercise_tags (exercise_id, tag) values ('fnd-data-order-001', 'data-stream');
insert into exercise_tags (exercise_id, tag) values ('fnd-data-order-001', 'binary-contract');
insert into exercise_tags (exercise_id, tag) values ('fnd-data-order-001', 'java-code');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-data-order-001', 'wrong_primitive_order', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-data-order-001', 'text_output_for_binary_contract', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-data-order-001', 'integer_overflow', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-data-order-001', 'missing_flush', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('fnd-data-order-001', 'extra_debug_stdout', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-data-order-001', 'DataInputStream/DataOutputStream dùng big-endian theo chuẩn Java.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-data-order-001', 'Ghi đúng loại primitive; writeInt khác writeLong.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('fnd-data-order-001', 'Flush DataOutputStream sau khi ghi đủ bốn giá trị.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-data-order-001', 'Đọc/ghi primitive theo đúng thứ tự nhị phân.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-data-order-001', 'Nhận diện lỗi đổi thứ tự read/write trong Data Stream.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('fnd-data-order-001', 'Không trộn text stream với binary stream.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-data-order-001', 'DataInputStream', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-data-order-001', 'DataOutputStream', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-data-order-001', 'Ước chung lớn nhất', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('fnd-data-order-001', 'Bội chung nhỏ nhất', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('fnd-data-order-001', 'CLAIM-PLATFORM-FOUNDATION-002', 'docs/research/platform/01_EXERCISE_TAXONOMY.md', 'Bài mở rộng để củng cố Data Stream trước khi vào TCP Data Stream; không phải đề thi chính thức.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('fnd-data-order-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Bài mở rộng để củng cố Data Stream trước khi vào TCP Data Stream; không phải đề thi chính thức.');
insert into exercise_tags (exercise_id, tag) values ('tcp-byte-prime-sum-001', 'tcp');
insert into exercise_tags (exercise_id, tag) values ('tcp-byte-prime-sum-001', 'byte-stream');
insert into exercise_tags (exercise_id, tag) values ('tcp-byte-prime-sum-001', 'partial-read');
insert into exercise_tags (exercise_id, tag) values ('tcp-byte-prime-sum-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-byte-prime-sum-001', 'partial_read_assumption', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-byte-prime-sum-001', 'wrong_request_format', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-byte-prime-sum-001', 'missing_newline', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-byte-prime-sum-001', 'missing_flush', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-byte-prime-sum-001', 'wrong_prime_logic', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-byte-prime-sum-001', 'Đọc đủ 4 byte length trước.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-byte-prime-sum-001', 'Lặp read cho tới khi đủ length byte.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-byte-prime-sum-001', 'Gửi kết quả dạng text line và flush.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-byte-prime-sum-001', 'Đọc byte stream theo frame độ dài.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-byte-prime-sum-001', 'Xử lý partial read.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-byte-prime-sum-001', 'Phân biệt lỗi protocol với lỗi tính toán.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-byte-prime-sum-001', 'Socket', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-byte-prime-sum-001', 'InputStream', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-byte-prime-sum-001', 'OutputStream', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-byte-prime-sum-001', 'Số nguyên tố', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-byte-prime-sum-001', 'CLAIM-TCP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern TCP và lỗi partial read được rút từ research; host/port là của platform luyện tập, không phải server thi PTIT.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-byte-prime-sum-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Pattern TCP và lỗi partial read được rút từ research; host/port là của platform luyện tập, không phải server thi PTIT.');
insert into exercise_tags (exercise_id, tag) values ('tcp-data-gcd-lcm-001', 'tcp');
insert into exercise_tags (exercise_id, tag) values ('tcp-data-gcd-lcm-001', 'data-stream');
insert into exercise_tags (exercise_id, tag) values ('tcp-data-gcd-lcm-001', 'primitive-order');
insert into exercise_tags (exercise_id, tag) values ('tcp-data-gcd-lcm-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-data-gcd-lcm-001', 'wrong_primitive_order', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-data-gcd-lcm-001', 'missing_flush', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-data-gcd-lcm-001', 'text_stream_used_for_data_stream', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-data-gcd-lcm-001', 'integer_overflow', 4);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-data-gcd-lcm-001', 'Không dùng PrintWriter cho bài này.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-data-gcd-lcm-001', 'Server phân biệt writeInt và writeLong.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-data-gcd-lcm-001', 'Gửi đủ bốn primitive rồi flush.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-data-gcd-lcm-001', 'Tuân thủ thứ tự primitive trong Data Stream.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-data-gcd-lcm-001', 'Tách lỗi sai thứ tự write với lỗi sai giá trị.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-data-gcd-lcm-001', 'Flush sau request và submission.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-data-gcd-lcm-001', 'Socket', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-data-gcd-lcm-001', 'DataInputStream', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-data-gcd-lcm-001', 'DataOutputStream', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-data-gcd-lcm-001', 'GCD/LCM', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-data-gcd-lcm-001', 'CLAIM-TCP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern Data Stream được dùng để luyện kỹ năng thi; không cố định host/port theo PTIT.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-data-gcd-lcm-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Pattern Data Stream được dùng để luyện kỹ năng thi; không cố định host/port theo PTIT.');
insert into exercise_tags (exercise_id, tag) values ('tcp-character-normalize-001', 'tcp');
insert into exercise_tags (exercise_id, tag) values ('tcp-character-normalize-001', 'character-stream');
insert into exercise_tags (exercise_id, tag) values ('tcp-character-normalize-001', 'flush');
insert into exercise_tags (exercise_id, tag) values ('tcp-character-normalize-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-character-normalize-001', 'missing_newline', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-character-normalize-001', 'missing_flush', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-character-normalize-001', 'console_output_used_as_submission', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-character-normalize-001', 'wrong_request_format', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-character-normalize-001', 'wrong_encoding', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-character-normalize-001', 'Dùng BufferedWriter.newLine().', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-character-normalize-001', 'Flush cả request và submission.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-character-normalize-001', 'Đừng in debug vào socket.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-character-normalize-001', 'Tuân thủ request/response/submission line contract.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-character-normalize-001', 'Không dùng console output làm submission.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-character-normalize-001', 'Nhận diện thiếu newline/flush qua timeout.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-character-normalize-001', 'Socket', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-character-normalize-001', 'BufferedReader', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-character-normalize-001', 'BufferedWriter', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-character-normalize-001', 'String processing', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-character-normalize-001', 'CLAIM-TCP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern TCP character stream dựa trên research snapshot; server là mock server của platform.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-character-normalize-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Pattern TCP character stream dựa trên research snapshot; server là mock server của platform.');
insert into exercise_tags (exercise_id, tag) values ('tcp-object-product-001', 'tcp');
insert into exercise_tags (exercise_id, tag) values ('tcp-object-product-001', 'object-stream');
insert into exercise_tags (exercise_id, tag) values ('tcp-object-product-001', 'serializable');
insert into exercise_tags (exercise_id, tag) values ('tcp-object-product-001', 'product');
insert into exercise_tags (exercise_id, tag) values ('tcp-object-product-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-object-product-001', 'object_stream_order_deadlock', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-object-product-001', 'wrong_package', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-object-product-001', 'serial_version_mismatch', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-object-product-001', 'wrong_object_type', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('tcp-object-product-001', 'missing_flush', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-object-product-001', 'Tạo ObjectOutputStream trước và flush header.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-object-product-001', 'Dùng đúng model trong starter.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('tcp-object-product-001', 'Không đổi tên package hoặc field.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-object-product-001', 'Dùng ObjectInputStream/ObjectOutputStream đúng thứ tự.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-object-product-001', 'Giữ đúng package/class/serialVersionUID.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('tcp-object-product-001', 'Không deserialize class ngoài allowlist.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-object-product-001', 'Serializable', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-object-product-001', 'Object stream', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-object-product-001', 'Package Java', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('tcp-object-product-001', 'Decimal rounding', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-object-product-001', 'CLAIM-TCP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Bài luyện Object Stream theo pattern exam contract; không dùng object từ server PTIT thật.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('tcp-object-product-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Bài luyện Object Stream theo pattern exam contract; không dùng object từ server PTIT thật.');
insert into exercise_tags (exercise_id, tag) values ('udp-string-request-id-001', 'udp');
insert into exercise_tags (exercise_id, tag) values ('udp-string-request-id-001', 'string');
insert into exercise_tags (exercise_id, tag) values ('udp-string-request-id-001', 'request-id');
insert into exercise_tags (exercise_id, tag) values ('udp-string-request-id-001', 'packet-length');
insert into exercise_tags (exercise_id, tag) values ('udp-string-request-id-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-string-request-id-001', 'missing_request_id', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-string-request-id-001', 'stale_request_id', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-string-request-id-001', 'wrong_delimiter', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-string-request-id-001', 'using_full_buffer_instead_of_packet_length', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-string-request-id-001', 'wrong_session_result', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-string-request-id-001', 'Tạo String từ packet.getData(), packet.getOffset(), packet.getLength().', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-string-request-id-001', 'Không tự sinh requestId.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-string-request-id-001', 'Gửi submission về đúng address/port của packet response.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-string-request-id-001', 'Giữ nguyên requestId theo session.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-string-request-id-001', 'Dùng packet.getLength() khi chuyển byte sang string.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-string-request-id-001', 'Phân biệt stale requestId và sai kết quả.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-string-request-id-001', 'DatagramSocket', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-string-request-id-001', 'DatagramPacket', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-string-request-id-001', 'UTF-8', 3);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('udp-string-request-id-001', 'CLAIM-UDP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Pattern UDP requestId dựa trên research; requestId/host/port trong platform không đại diện cho kỳ thi hiện hành.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('udp-string-request-id-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Pattern UDP requestId dựa trên research; requestId/host/port trong platform không đại diện cho kỳ thi hiện hành.');
insert into exercise_tags (exercise_id, tag) values ('udp-object-product-001', 'udp');
insert into exercise_tags (exercise_id, tag) values ('udp-object-product-001', 'object');
insert into exercise_tags (exercise_id, tag) values ('udp-object-product-001', 'request-id');
insert into exercise_tags (exercise_id, tag) values ('udp-object-product-001', 'serializable');
insert into exercise_tags (exercise_id, tag) values ('udp-object-product-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'missing_request_id', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'wrong_request_id_bytes', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'wrong_object_type', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'wrong_package', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'datagram_too_large', 5);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('udp-object-product-001', 'using_full_buffer_instead_of_packet_length', 6);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-object-product-001', 'Copy đúng 8 byte đầu bằng Arrays.copyOfRange.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-object-product-001', 'Deserialize phần sau requestId, không deserialize cả datagram.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('udp-object-product-001', 'Giữ model Product trong starter.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-object-product-001', 'Tách header requestId khỏi payload object.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-object-product-001', 'Dùng đúng offset/length của datagram.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('udp-object-product-001', 'Giữ class contract khi Java serialization qua UDP.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-object-product-001', 'DatagramSocket', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-object-product-001', 'ObjectInputStream', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-object-product-001', 'ObjectOutputStream', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('udp-object-product-001', 'Serializable', 4);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('udp-object-product-001', 'CLAIM-UDP-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Bài luyện UDP object theo pattern research; không deserialize arbitrary class ngoài allowlist.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('udp-object-product-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Bài luyện UDP object theo pattern research; không deserialize arbitrary class ngoài allowlist.');
insert into exercise_tags (exercise_id, tag) values ('rmi-data-pythagorean-001', 'rmi');
insert into exercise_tags (exercise_id, tag) values ('rmi-data-pythagorean-001', 'data-service');
insert into exercise_tags (exercise_id, tag) values ('rmi-data-pythagorean-001', 'pythagorean');
insert into exercise_tags (exercise_id, tag) values ('rmi-data-pythagorean-001', 'java-only');
insert into exercise_tags (exercise_id, tag) values ('rmi-data-pythagorean-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('rmi-data-pythagorean-001', 'wrong_rmi_service', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('rmi-data-pythagorean-001', 'wrong_qcode', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('rmi-data-pythagorean-001', 'wrong_interface_package', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('rmi-data-pythagorean-001', 'unsorted_submission', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('rmi-data-pythagorean-001', 'stale_session_token', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('rmi-data-pythagorean-001', 'Dùng interface trong starter.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('rmi-data-pythagorean-001', 'Service name chứa sessionId.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('rmi-data-pythagorean-001', 'Submit cùng token/qCode đã request.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('rmi-data-pythagorean-001', 'Lookup RMI registry đúng service name.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('rmi-data-pythagorean-001', 'Dùng interface artifact cố định.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('rmi-data-pythagorean-001', 'Gắn request/submit với token và qCode của phiên.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('rmi-data-pythagorean-001', 'Java RMI', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('rmi-data-pythagorean-001', 'Remote interface', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('rmi-data-pythagorean-001', 'List/array processing', 3);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('rmi-data-pythagorean-001', 'CLAIM-RMI-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'RMI là Java-specific trong Exam Track; service name/port là của platform luyện tập.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('rmi-data-pythagorean-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'RMI là Java-specific trong Exam Track; service name/port là của platform luyện tập.');
insert into exercise_tags (exercise_id, tag) values ('ws-data-factorization-001', 'soap');
insert into exercise_tags (exercise_id, tag) values ('ws-data-factorization-001', 'web-service');
insert into exercise_tags (exercise_id, tag) values ('ws-data-factorization-001', 'data-service');
insert into exercise_tags (exercise_id, tag) values ('ws-data-factorization-001', 'factorization');
insert into exercise_tags (exercise_id, tag) values ('ws-data-factorization-001', 'network-challenge');
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('ws-data-factorization-001', 'wrong_wsdl_endpoint', 1);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('ws-data-factorization-001', 'wrong_qcode', 2);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('ws-data-factorization-001', 'wrong_method_name', 3);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('ws-data-factorization-001', 'wrong_array_order', 4);
insert into exercise_common_failures (exercise_id, failure_code, display_order) values ('ws-data-factorization-001', 'soap_namespace_mismatch', 5);
insert into exercise_hints (exercise_id, content, display_order) values ('ws-data-factorization-001', 'Sinh client từ WSDL của phiên hiện tại.', 1);
insert into exercise_hints (exercise_id, content, display_order) values ('ws-data-factorization-001', 'Giữ nguyên token/qCode khi submit.', 2);
insert into exercise_hints (exercise_id, content, display_order) values ('ws-data-factorization-001', 'Danh sách factors phải tăng dần và có lặp.', 3);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('ws-data-factorization-001', 'Dùng WSDL endpoint đúng phiên.', 1);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('ws-data-factorization-001', 'Gọi request/submit theo contract SOAP.', 2);
insert into exercise_learning_objectives (exercise_id, objective, display_order) values ('ws-data-factorization-001', 'Serialize array/list số nguyên đúng định dạng.', 3);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('ws-data-factorization-001', 'SOAP/WSDL', 1);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('ws-data-factorization-001', 'Generated Java client', 2);
insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values ('ws-data-factorization-001', 'Prime factorization', 3);
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('ws-data-factorization-001', 'CLAIM-WS-001', 'docs/research/exam/04_EXAM_PATTERNS.md', 'Bài giữ SOAP cho Exam Track; endpoint là mock service của platform, không phải service thi thật.');
insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values ('ws-data-factorization-001', 'CLAIM-FAILURE-002', 'docs/research/exam/05_COMMON_FAILURES.md', 'Bài giữ SOAP cho Exam Track; endpoint là mock service của platform, không phải service thi thật.');

alter table exercises alter column evaluation_mode set not null;
alter table exercises alter column grader_key set not null;
alter table exercises alter column evidence_disclaimer set not null;
