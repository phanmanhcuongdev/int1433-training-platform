# 01 - Exercise Taxonomy

## Nguyen Tac Chung

Moi bai phai co hai lop muc tieu:

- Algorithm/payload logic: xu ly chuoi, mang, object, list, matrix.
- Network/protocol contract: request, receive, submit, close/flush, timeout, package/service.

Moi bai phai co `source_label`:

- `OBSERVED`: co pattern truc tiep trong de/tai lieu/repo/ghi chu.
- `STRONG_PATTERN`: lap lai trong nhieu evidence family doc lap hoac khong phai copy.
- `EXTENDED`: mo rong tu ky nang thi de hoc chac hon.
- `CHALLENGE`: ngoai scope thi nhung huu ich cho Java/network/backend.

Khong co bai nao duoc hard-code rang IP/port cu la dung vinh vien. IP/port chi la bien trong mock server hoac contract cua tung bai.

## Taxonomy

### A. Java Stream Foundation

Muc tieu: lam chu Java I/O truoc khi cham TCP/UDP.

Nhom con:

- Byte stream: `InputStream`, `OutputStream`, byte array, partial read.
- Character stream: `Reader`, `Writer`, `BufferedReader`, newline.
- Data stream: `DataInputStream`, `DataOutputStream`, thu tu primitive.
- Object stream: `ObjectInputStream`, `ObjectOutputStream`, `Serializable`, `serialVersionUID`.
- Buffer/flush/close: flush bat buoc, close dung luc, EOF.
- Blocking/timeout: `readLine()` block, socket timeout.
- Encoding: UTF-8, ASCII, default charset traps.

Dependency: bat buoc truoc TCP/UDP Object va Character.

### B. TCP

#### TCP Byte Stream

- Request: `studentCode;qCode` qua bytes.
- Receive: byte/string payload, co the doc mot lan hoac lap den EOF/length.
- Processing: chuoi/mang so.
- Submit: byte/string result.
- Common failure: partial read, sai charset, khong flush, close som.
- Progression: simple scalar -> list -> format phuc tap -> partial read intentionally fragmented.

#### TCP Data Stream

- Request: UTF/string hoac bytes theo de.
- Receive: primitives theo thu tu: int, float, String.
- Processing: tinh toan theo primitive sequence.
- Submit: primitive theo dung thu tu.
- Common failure: sai order read/write, dung wrong stream, endian/format mismatch.
- Progression: 2 int -> n primitives -> mix primitive + string.

#### TCP Character Stream

- Request: text line `studentCode;qCode`.
- Receive: one/multiple lines.
- Processing: string normalization, counting, sorting.
- Submit: text line.
- Common failure: thieu newline, thieu flush, `readLine()` block.
- Progression: 1 line -> multiple lines -> delimiter edge cases -> Unicode/encoding.

#### TCP Object Stream

- Request: `writeObject("studentCode;qCode")`.
- Receive: object theo package/class contract.
- Processing: update field.
- Submit: object da sua.
- Common failure: `writeUTF`, sai package, sai `serialVersionUID`, sai field name, order OIS/OOS.
- Progression: single object -> nested object -> list object -> intentionally incompatible broken code.

### C. UDP

#### UDP String

- Request: `;studentCode;qCode`.
- Receive: `requestId;data`.
- Submit: `requestId;processedData`.
- Common failure: bo requestId, doc full buffer, sai delimiter.

#### UDP Data

- Request/response tuong tu UDP String nhung payload la number/list/matrix text.
- Them timeout/retry o muc extended.
- Common failure: packet length, timeout, stale packet.

#### UDP Object

- Request: datagram text `;studentCode;qCode`.
- Receive: 8 byte requestId + serialized object.
- Submit: 8 byte requestId + serialized object da sua.
- Common failure: requestId length, serialization, package, byte offset.

Extended UDP: duplicate/loss/out-of-order, idempotency, retry with requestId. Khong dua vao Exam Track mac dinh vi de thi chi can request/response co ban.

### D. RMI

Nhom theo service:

- DataService: `requestData`, `submitData`.
- CharacterService: `requestCharacter`, `submitCharacter`.
- ByteService: `requestData`, `submitData`.
- ObjectService: `requestObject`, `submitObject`.

Contract can day:

- Registry lookup host/port/service name.
- Interface nam dung package.
- Request/submit pair dung tham so `studentCode`, `qCode`.
- Object contract: package `RMI`, field, constructor, `serialVersionUID`.

Common failure: sai service name, sai interface, classpath/package, remote exception, submit wrong type.

### E. Web Service

Nhom theo service:

- DataService: scalar/list/matrix, `getData`, `getDataDouble`, submit int/string/list.
- CharacterService: string/list/char array.
- ObjectService: object/list object.

Contract can day:

- WSDL endpoint configurable.
- Generated client.
- Request method va submit method dung service.
- Array/list/matrix/object type mapping.

Common failure: sai endpoint, sai generated package, sai method submit, JDK/tooling mismatch.

SOAP nen giu cho Exam Track. REST chi nen la Extended/Backend Track.

### F. Debugging Exercises

Dang bai:

- Broken code: code gan dung nhung sai mot loi protocol.
- Protocol mismatch: TCP Data dung Character stream.
- Hidden blocking: `readLine()` doi newline khong ton tai.
- Wrong package/object.
- Wrong method: `writeUTF` vs `writeObject`.
- Wrong requestId UDP.
- Stale port/IP assumption.
- Missing flush.
- Wrong charset.
- Object incompatibility.

Muc tieu: hoc cach tim loi khong AC, khong chi hoc giai thuat.

### G. Mock Exam

Mock exam 6 bai:

- It nhat 1 TCP.
- It nhat 1 UDP.
- It nhat 1 RMI.
- It nhat 1 Web Service.
- 2 bai random tu pool con lai, co gioi han khong trung technology qua nhieu.

Mock exam phai co state AC/WA/Protocol Error/Timeout/Runtime Error. Loi giai va hint khoa den khi het gio.

## Level De Xuat

| Level | Kien thuc yeu cau | Loai bai | Thoi gian | Tieu chi hoan thanh | San sang len level sau khi |
| --- | --- | --- | ---: | --- | --- |
| L0 Foundation I/O | Java syntax, classpath, exception | Stream offline, no network | 10-20 phut | Doc/ghi dung format, flush/close dung | Lam 6/8 bai stream khong can xem loi giai |
| L1 API Discipline | Socket/RMI/WS API co ban | Contract skeleton, starter code nhieu | 15-25 phut | Ket noi, request, receive, submit dung | Khong con sai format request co ban |
| L2 Single Request-Response | L0-L1 | TCP/UDP/RMI/WS payload don | 20-35 phut | AC voi 1 contract va 1 payload | Tu debug duoc WA vs Protocol Error |
| L3 Processing + Submit | Xu ly chuoi/mang vung | Bai thi standard | 30-45 phut | Dung logic va format edge cases | AC >= 70% bai standard |
| L4 Object/List/Matrix | Serializable, classpath, generated types | TCP/UDP/RMI/WS Object/List | 35-55 phut | Dung package, field, submit type | It gap loi class/package/service |
| L5 Failure Handling | Timeout, blocking, malformed input | Debugging, broken code, timeout | 20-50 phut | Tim va sua loi protocol | Giai thich duoc nguyen nhan khong AC |
| L6 Mock Exam | Tong hop | 6 bai mixed | 60-90 phut | Dat target AC theo muc tieu | On dinh qua 3 mock lien tiep |

Skill map:

- TCP/UDP exam readiness: L0 -> L1 -> L2 -> L3 -> L5 -> L6.
- RMI/WS readiness: L0 Object -> L1 -> L2 -> L4 -> L5 -> L6.
- Backend extension: L0 -> TCP/UDP -> HTTP/REST -> concurrency/security.

## Exercise Templates

### TCP Byte Stream

- Learning objective: doc/ghi byte dung contract, khong phu thuoc console.
- Server contract: TCP server nhan `studentCode;qCode`, tra byte payload hoac text bytes, doi result bytes.
- Student task: parse payload, xu ly, submit result.
- Hidden edge cases: fragmented response, leading/trailing spaces, empty token, large buffer.
- AC condition: request dung, result dung, flush/close dung.
- Common WA reasons: doc full buffer, sai charset, thieu flush, close som.
- Extension ideas: length-prefixed payload, binary payload, retry sau timeout.

### TCP Data Stream

- Learning objective: giu dung thu tu primitive read/write.
- Server contract: TCP + `DataInputStream/DataOutputStream`, server gui primitives theo sequence.
- Student task: doc primitives, tinh toan, gui primitives/string theo thu tu.
- Hidden edge cases: negative numbers, float precision, n=0/1, duplicate.
- AC condition: dung gia tri va dung order submit.
- Common WA reasons: sai order, dung Character stream, write string thay primitive.
- Extension ideas: mixed primitive + array length prefix.

### TCP Character Stream

- Learning objective: line protocol, newline, flush, blocking.
- Server contract: TCP + `BufferedReader/BufferedWriter`, request line, response one/more lines.
- Student task: xu ly text va submit line.
- Hidden edge cases: multiple spaces, Unicode, no trailing newline from server.
- AC condition: submit dung delimiter/newline va flush.
- Common WA reasons: `readLine()` block, thieu newline, sai delimiter.
- Extension ideas: multi-line protocol, charset variants.

### TCP Object Stream

- Learning objective: object stream order va Java serialization contract.
- Server contract: TCP + `ObjectOutputStream/ObjectInputStream`; request object string, response object.
- Student task: tao class dung package/fields, update object, submit object.
- Hidden edge cases: null field, private field, serialVersionUID, constructor mismatch.
- AC condition: deserialize/serialize dung va field sau xu ly dung.
- Common WA reasons: `writeUTF`, sai package, sai field case, missing Serializable.
- Extension ideas: nested object, list object, object compatibility debugging.

### UDP String

- Learning objective: datagram request/response va requestId.
- Server contract: UDP nhan `;studentCode;qCode`, tra `requestId;data`.
- Student task: xu ly string va gui `requestId;answer`.
- Hidden edge cases: packet co byte rac sau length, delimiter trong data, spaces.
- AC condition: dung requestId va answer.
- Common WA reasons: bo requestId, doc ca buffer, sai delimiter.
- Extension ideas: timeout/retry, duplicate response.

### UDP Data

- Learning objective: parse numeric/list payload trong datagram.
- Server contract: UDP tra `requestId;n;list` hoac `requestId;list`.
- Student task: tinh toan tren number/list/matrix text.
- Hidden edge cases: duplicate, sorted/unsorted, boundary n, negative values.
- AC condition: format result dung voi requestId.
- Common WA reasons: split sai, index sai, packet length sai.
- Extension ideas: malformed packet, loss/duplicate simulation.

### UDP Object

- Learning objective: requestId byte prefix + object serialization.
- Server contract: UDP response 8 byte requestId + serialized object.
- Student task: deserialize object, sua field, serialize lai voi 8 byte requestId dau.
- Hidden edge cases: object larger than expected, field null, requestId exact 8 bytes.
- AC condition: prefix va object dung.
- Common WA reasons: sai offset, sai package, requestId text length sai.
- Extension ideas: object list, split packet warning, custom timeout.

### RMI Data

- Learning objective: registry lookup va request/submit pair.
- Server contract: `RMIDataService.requestData(studentCode,qCode)` va `submitData`.
- Student task: request data, xu ly scalar/list, submit object dung type.
- Hidden edge cases: submit `String` thay `List`, sort stability, empty list.
- AC condition: service lookup dung, submit type/value dung.
- Common WA reasons: sai service name, sai package interface, wrong submit type.
- Extension ideas: multiple request aliases, remote exception handling.

### RMI Object

- Learning objective: RMI object contract + Serializable.
- Server contract: `RMIObjectService.requestObject`/`submitObject`, object class trong package `RMI`.
- Student task: dinh nghia class dung fields, update object, submit.
- Hidden edge cases: field type mismatch, constructor note, serialVersionUID.
- AC condition: object submit deserialize dung va field dung.
- Common WA reasons: sai package, sai field case, missing Serializable.
- Extension ideas: polymorphic object, list object.

### Web Service Data

- Learning objective: SOAP/WSDL generated client va method mapping.
- Server contract: WSDL DataService, request method nhu `getData`, submit method nhu `submitDataIntArray`.
- Student task: goi service, xu ly list/matrix/scalar, submit dung method.
- Hidden edge cases: list mutability, matrix dimension, type conversion.
- AC condition: endpoint/method/value dung.
- Common WA reasons: sai endpoint, sai submit method, generated class mismatch.
- Extension ideas: REST equivalent in Backend Track.

### Web Service Object

- Learning objective: SOAP object/list mapping.
- Server contract: WSDL ObjectService, request object/list va submit object/list.
- Student task: xu ly generated POJO/list, preserve order khi can.
- Hidden edge cases: null date, duplicate score, stable grouping.
- AC condition: submit dung generated type va noi dung.
- Common WA reasons: import sai package, submit list sai type, mutate wrong object.
- Extension ideas: DTO mapping, REST/gRPC equivalent.

### Broken Code Debugging

- Learning objective: nhan dien loi protocol gay khong AC.
- Server contract: tuy bai, nhung starter code co san 1-3 bug.
- Student task: sua code, khong viet lai tu dau neu khong can.
- Hidden edge cases: bug chi lo khi fragmented packet/timeout/object mismatch.
- AC condition: code sua dat AC va giai thich loi.
- Common WA reasons: sua logic nhung khong sua protocol root cause.
- Extension ideas: protocol log diff, minimal patch challenge.

### Mock Exam

- Learning objective: tong hop, quan ly thoi gian, uu tien bai de AC truoc.
- Server contract: 6 mock servers/session, moi bai co contract rieng.
- Student task: lam tung client hoac class theo de.
- Hidden edge cases: mix easy/hard, object/list, timeout.
- AC condition: so bai AC theo scoring mock.
- Common WA reasons: nham contract giua bai, copy port/service, bo flush/requestId.
- Extension ideas: diagnostic mode, randomized retake, post-exam remediation.
