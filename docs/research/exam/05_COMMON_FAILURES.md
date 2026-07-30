# 05 - Common Failures

| Loi | Trieu chung | Nguyen nhan | Cach tranh | Nguon |
| --- | --- | --- | --- | --- |
| Tao NetBeans project sai kieu | Code khong chay trong phong thi | Tao `Java with Maven` thay vi `Java Application` | Tao Java Application | `NHUNG LUU Y...docx` |
| Khong dong socket/ket noi | Khong AC hoac loi ngoai le tren he thong | De bai yeu cau dong ket noi; server doi EOF/close | Dong socket/stream sau khi submit | `NHUNG LUU Y...docx`; nhieu de bai |
| Quen `flush()` | Server khong nhan du lieu, client treo/khong log | Buffer chua day/khong day du lieu ra network | Flush sau khi ghi | `NHUNG LUU Y...docx` |
| TCP Object gui request sai method | Khong co log | Dung `writeUTF` thay vi `writeObject` cho chuoi `studentCode;qCode` | Dung `ObjectOutputStream.writeObject` khi de yeu cau object stream | `NHUNG LUU Y...docx` |
| Sai package object | Run khong co log/deserialize fail | Class phai dung package `TCP`, `UDP`, `RMI` theo de | Tao package dung ten, cung cap voi package khac | `NHUNG LUU Y...docx` |
| Package nam trong package/project con | Khong duoc chap nhan | He thong load class theo fully qualified name | Tao package truc tiep trong source root | `NHUNG LUU Y...docx` |
| Sai ten thuoc tinh/hoa thuong | Object submit khong dung | Server deserialize/reflect theo ten thuoc tinh | Ghi dung tat ca thuoc tinh nhu de | `NHUNG LUU Y...docx`; de Object |
| Quen `implements Serializable` | Loi serialization | Object TCP/UDP/RMI can serialize | Implement Serializable va serialVersionUID theo de | `NHUNG LUU Y...docx`; de Object |
| Quan tam `toString`/console output nhu kenh nop bai | In dung nhung khong AC | He thong cham du lieu submit, khong cham console | Submit qua socket/RMI/WS dung format | `NHUNG LUU Y...docx`; INFERENCE |
| Dung JDK thap cho WS | Loi build/run generated WS | Moi truong phong thi dung JDK cao | Luyen va build WS voi JDK tuong thich | `NHUNG LUU Y...docx` |
| Ping lai port/de cu | Khong ket noi duoc | MSV da thi bi an de, port cu bi khoa | Khong coi port cu la server hien hanh | `NHUNG LUU Y...docx` |
| Sai host/IP | Connect fail | IP thi/luyen tap thay doi; de cu ghi host khac nhau | Lay IP tu thong bao/de thi tai phong | De TCP/UDP; `NHUNG LUU Y...docx` |
| Sai format TCP request | Server khong tra data | Thieu `studentCode;qCode` hoac sai dau phan tach | Ghi dung format de bai | De TCP |
| Sai format UDP request | Server khong tra data | Thieu dau `;` dau chuoi `;studentCode;qCode` | Ghi dung request UDP | De UDP |
| Bo qua requestId UDP | Server khong map submission | Ket qua phai kem requestId | Gui `requestId;...` hoac 8 byte requestId + object | De UDP |
| Doc du buffer UDP | Ket qua co byte rac | Dung full buffer thay vi packet length | Xu ly theo length packet | INFERENCE tu UDP datagram contract |
| Sai service name RMI | Lookup fail | Service registry name co tien to RMI | Dung `RMIDataService`, `RMICharacterService`, `RMIByteService`, `RMIObjectService` | De RMI |
| Sai WSDL/endpoint WS | Client khong goi duoc service | Endpoint theo `<Exam_IP>:8080/JNPWS/...` | Lay Exam_IP va WSDL dung service | De WS |
| Sai method submit WS | Khong AC du lieu dung logic | Moi dang co method submit khac nhau | Doc dung method `submitData...`, `submitCharacter...`, `submitList...` | De WS/generated classes |

