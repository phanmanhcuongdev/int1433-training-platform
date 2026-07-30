# 04 - Exam Patterns

## TCP Contract

Evidence family: de on TCP 2024, de bo sung TCP 2025, code trong `ThiThu`, `ThiTCP_2310`, `TCP`.

- Client ket noi host de bai cho, thuong la `203.162.10.109` hoac `172.188.19.218` trong de on cu.
- Port phu thuoc stream/dang bai, hay gap: 2206 byte stream, 2207 data stream, 2208 character stream, 2209 object stream. Khong duoc coi co dinh cho moi nam.
- Request dau tien: chuoi `studentCode;qCode`; rieng TCP Object ghi chu nhac phai gui chuoi nay bang `writeObject`, khong phai `writeUTF`.
- Server tra du lieu theo kieu stream tuong ung: byte/chuoi, DataInputStream primitives, BufferedReader text, ObjectInputStream object.
- Client xu ly va gui lai ket qua qua cung kenh stream, theo dung thu tu de bai.
- Ket thuc: dong ket noi/socket; nhieu de ghi ro dong ket noi va ket thuc.
- Timeout trong de: nhieu de ghi server ho tro thoi gian giao tiep toi da 5s moi yeu cau.

## UDP Contract

Evidence family: `Mau de on Bai KT 2 - UDP.pdf`, `Mau de on thi cuoi ky UDP (Bo sung).pdf`, `DE THI UDP BO SUNG...docx`, code `UDP`.

- Request dau tien: chuoi `;studentCode;qCode`.
- Data/String: server tra `requestId;data` hoac `requestId;n;A1,A2,...`.
- Object: server tra packet co 8 byte dau la requestId, cac byte con lai la serialized object.
- Client gui ket qua kem requestId: `requestId;processedData` hoac packet 8 byte requestId + object da sua.
- Port hay gap: 2207 cho DataType, 2208 cho String/Character, 2209 cho Object.
- Can dung `packet.getLength()`/bytesRead khi xu ly datagram; day la suy luan ky thuat tu contract UDP object/string, khong phai cau van chinh thuc trong tai lieu.

## RMI Contract

Evidence family: `Mau de on thi cuoi ky RMI.pdf`, interface/class trong `RMI`.

- Interface nam trong package `RMI`.
- DataService:
  `requestData(String studentCode, String qCode)` va `submitData(String studentCode, String qCode, Object data)`.
- CharacterService:
  `requestCharacter(String studentCode, String qCode)` va `submitCharacter(String studentCode, String qCode, String strSubmit)`.
- ByteService:
  `requestData(String studentCode, String qCode)` va `submitData(String studentCode, String qCode, byte[] data)`.
- ObjectService:
  `requestObject(String studentCode, String qCode/qAlias)` va `submitObject(String studentCode, String qCode/qAlias, Serializable object)`.
- Registry service names: `RMIDataService`, `RMICharacterService`, `RMIByteService`, `RMIObjectService`.
- Object classes phai dung package `RMI`, thuoc tinh, constructor va `serialVersionUID` theo de.

## Web Service Contract

Evidence family: `Mau de on thi cuoi ky WEB SERVICE.docx`, generated classes `vn/medianews`.

- Endpoint mau: `http://<Exam_IP>:8080/JNPWS/DataService?wsdl`, `CharacterService?wsdl`, `ObjectService?wsdl`.
- DataService: `getData`, `getDataDouble`, cac submit nhu `submitDataStringArray`, `submitDataIntArray`, `submitDataIntMatrix`, `submitDataString`.
- CharacterService: `requestString`, `requestStringArray`, submit `submitCharacterString`, `submitCharacterStringArray`, `submitCharacterCharArray`.
- ObjectService: request/submit cac doi tuong/list nhu `requestProductY`, `submitProductY`, `requestListStudentY`, `submitListStudentY`.
- De bai dung `studentCode` va `qCode` lam tham so request/submit.
- Ghi chu sinh vien: can JDK cao, doc huong dan setup WS moi.

## Noi Dung Xuat Hien Thuong Xuyen

- Stream/socket: TCP, UDP, byte stream, data stream, character stream, object serialization.
- RMI: Data/Character/Byte/Object service.
- WS SOAP/WSDL: Data/Character/Object service va generated client.
- Giai thuat xu ly chuoi/mang/object co muc do tu de den vua; day la payload cua bai thi, khong phai trong tam quy dinh diem.

