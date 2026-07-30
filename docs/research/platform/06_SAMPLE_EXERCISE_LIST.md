# 06 - Sample Exercise List

Danh sach mau 28 bai, chua viet full solution.

| ID | Ten | Cong nghe | Stream/Service | Level | Source label | Ky nang | Loi chinh can tranh |
| --- | --- | --- | --- | --- | --- | --- | --- |
| FND-001 | Partial Read Counter | Java I/O | Byte Stream | L0 | EXTENDED | Doc byte dung length | Doc full buffer |
| FND-002 | Line Protocol With Flush | Java I/O | Character Stream | L0 | EXTENDED | newline, flush | `readLine()` block |
| FND-003 | Primitive Order Drill | Java I/O | Data Stream | L0 | EXTENDED | read/write int,float | Sai order |
| FND-004 | Serializable Product | Java I/O | Object Stream | L0 | EXTENDED | Serializable contract | Thieu serialVersionUID |
| TCP-B-001 | Sum Prime Numbers | TCP | Byte Stream | L2 | OBSERVED | Request-response byte | Sai format request |
| TCP-B-002 | Longest Increasing Segment | TCP | Byte Stream | L3 | OBSERVED | Parse list, submit string | Partial read |
| TCP-D-001 | GCD LCM Sum Product | TCP | Data Stream | L2 | OBSERVED | Primitive order | Gui sai thu tu |
| TCP-D-002 | Dice Probability | TCP | Data Stream | L3 | OBSERVED | float submit | Sai type |
| TCP-C-001 | Filter Domains | TCP | Character Stream | L2 | OBSERVED | BufferedReader/Writer | Thieu newline/flush |
| TCP-C-002 | Sort Words By Length | TCP | Character Stream | L3 | OBSERVED | Stable sort | Sai delimiter |
| TCP-O-001 | Fix Product Object | TCP | Object Stream | L4 | OBSERVED | ObjectInput/Output | `writeUTF` thay `writeObject` |
| TCP-O-002 | Normalize Customer | TCP | Object Stream | L4 | OBSERVED | Package/class/field | Sai package |
| UDP-S-001 | Normalize String With RequestId | UDP | String | L2 | OBSERVED | requestId prefix | Bo requestId |
| UDP-S-002 | Character Frequency | UDP | String | L3 | OBSERVED | Count, preserve order | Sai format |
| UDP-D-001 | Min Max Numbers | UDP | Data Text | L2 | OBSERVED | Datagram text | Doc du buffer |
| UDP-D-002 | Missing Numbers | UDP | Data Text | L3 | OBSERVED | Parse n/list | Sai delimiter |
| UDP-O-001 | Product Quantity Repair | UDP | Object | L4 | OBSERVED | 8 byte requestId + object | Sai offset |
| UDP-O-002 | Student Email Builder | UDP | Object | L4 | OBSERVED | Serialize object | Sai class package |
| RMI-D-001 | Pythagorean Triples | RMI | DataService | L3 | OBSERVED | requestData/submitData | Sai service name |
| RMI-C-001 | Caesar Decoder | RMI | CharacterService | L3 | OBSERVED | String submit | Sai qCode |
| RMI-B-001 | Byte Frequency | RMI | ByteService | L4 | OBSERVED | byte[] submit | Sai type submit |
| RMI-O-001 | Product Final Price | RMI | ObjectService | L4 | OBSERVED | Serializable object | Sai serialVersionUID |
| WS-D-001 | Factorize Integers | Web Service | DataService | L3 | OBSERVED | WSDL generated client | Sai submit method |
| WS-C-001 | Sort By Vowel Count | Web Service | CharacterService | L3 | OBSERVED | List<String> | Sai stable sort |
| WS-O-001 | Student Top Score Per Subject | Web Service | ObjectService | L4 | OBSERVED | List object | Sai generated class |
| DBG-001 | Missing Flush TCP | Debugging | TCP Character | L5 | STRONG_PATTERN | Diagnose blocking | Quen flush |
| DBG-002 | Wrong UDP RequestId | Debugging | UDP String | L5 | STRONG_PATTERN | Protocol log | Bo requestId |
| MOCK-001 | Mixed Final Exam A | Mock Exam | 6 bai | L6 | STRONG_PATTERN | TCP/UDP/RMI/WS mix | Quan ly thoi gian |

Can bang cong nghe:

- Foundation: 4/28.
- TCP: 8/28.
- UDP: 6/28.
- RMI: 4/28.
- WS: 3/28.
- Debug/mock: 3/28.

Khong cong nghe nao vuot 40%.

