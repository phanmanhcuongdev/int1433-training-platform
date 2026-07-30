# 02 - Language And Framework Options

## Language Options

### Java

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Rat cao, mon va de thi xoay quanh Java, RMI, SOAP |
| Ho tro TCP/UDP | Tot, `java.net` day du |
| Ho tro RMI tuong duong | Native RMI |
| Ho tro SOAP/WSDL | Tot nhung tooling can chot JDK/JAX-WS |
| De sandbox/cham tu dong | Tot, compile/run ro rang |
| De cho nguoi hoc | Phu hop vi dung voi mon |
| Phu hop backend website | Tot voi Spring Boot |
| Gia tri portfolio | Cao neu lam backend/judge nghiem tuc |
| Rui ro security | Trung binh, can sandbox JVM |
| Chi phi trien khai | Trung binh |

Ket luan: ngon ngu chinh cho Exam Track.

### Kotlin

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Trung binh-cao vi JVM, nhung de thi Java |
| Ho tro TCP/UDP | Tot qua JVM |
| Ho tro RMI tuong duong | Co the dung JVM RMI |
| Ho tro SOAP/WSDL | Co the dung Java libs, nhung tooling phuc tap hon |
| De sandbox/cham tu dong | Tot neu compile Kotlin |
| De cho nguoi hoc | Trung binh, can hoc them syntax |
| Phu hop backend website | Tot voi Spring/Ktor |
| Gia tri portfolio | Cao |
| Rui ro security | Tuong tu Java |
| Chi phi trien khai | Cao hon Java neu them compiler |

Ket luan: cho phep sau Java, dung chung JVM; khong dua vao MVP judge.

### C#

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Trung binh |
| Ho tro TCP/UDP | Tot |
| Ho tro RMI tuong duong | Khong co RMI Java; chi co remoting/gRPC/WCF style |
| Ho tro SOAP/WSDL | Tot hon nhieu ngon ngu hien dai |
| De sandbox/cham tu dong | Kha tot voi dotnet container |
| De cho nguoi hoc | Trung binh |
| Phu hop backend website | Rat tot voi ASP.NET Core |
| Gia tri portfolio | Cao |
| Rui ro security | Trung binh |
| Chi phi trien khai | Trung binh |

Ket luan: tot cho track mo rong/backend, khong phu hop Exam Track RMI.

### Python

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Thap-trung binh |
| Ho tro TCP/UDP | Tot, rat de hoc |
| Ho tro RMI tuong duong | Khong |
| Ho tro SOAP/WSDL | Co nhung khong nen lam exam parity |
| De sandbox/cham tu dong | De chay nhung kho chan abuse neu khong sandbox |
| De cho nguoi hoc | Rat de |
| Phu hop backend website | Tot voi FastAPI |
| Gia tri portfolio | Cao cho automation/backend |
| Rui ro security | Cao neu online judge vi dynamic runtime |
| Chi phi trien khai | Thap |

Ket luan: chi nen dung TCP/UDP extension va backend prototype.

### Go

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Trung binh thap |
| Ho tro TCP/UDP | Rat tot |
| Ho tro RMI tuong duong | Khong |
| Ho tro SOAP/WSDL | Yeu/trung binh |
| De sandbox/cham tu dong | Tot, binary static |
| De cho nguoi hoc | Trung binh |
| Phu hop backend website | Tot |
| Gia tri portfolio | Cao cho network/backend |
| Rui ro security | Trung binh |
| Chi phi trien khai | Thap |

Ket luan: tot cho Extended Networking Track TCP/UDP, khong dung cho RMI exam.

### JavaScript/TypeScript

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Thap |
| Ho tro TCP/UDP | Tot voi Node.js, nhung khac Java |
| Ho tro RMI tuong duong | Khong |
| Ho tro SOAP/WSDL | Co package nhung khong ly tuong |
| De sandbox/cham tu dong | Trung binh, npm dependency rui ro |
| De cho nguoi hoc | De voi web dev |
| Phu hop backend website | Tot |
| Gia tri portfolio | Cao |
| Rui ro security | Cao neu cho npm tu do |
| Chi phi trien khai | Thap-trung binh |

Ket luan: chi dung frontend/backend platform, khong dung Exam Track.

### Rust

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Thap |
| Ho tro TCP/UDP | Rat tot |
| Ho tro RMI tuong duong | Khong |
| Ho tro SOAP/WSDL | Yeu |
| De sandbox/cham tu dong | Tot nhung compile ton tai nguyen |
| De cho nguoi hoc | Kho |
| Phu hop backend website | Tot nhung nang |
| Gia tri portfolio | Cao |
| Rui ro security | Runtime thap, compile abuse van co |
| Chi phi trien khai | Cao |

Ket luan: Challenge track, khong nen mo som.

### C/C++

| Tieu chi | Danh gia |
| --- | --- |
| Sat mon INT1433 | Thap-trung binh cho socket, thap cho RMI/WS |
| Ho tro TCP/UDP | Rat tot |
| Ho tro RMI tuong duong | Khong |
| Ho tro SOAP/WSDL | Kho |
| De sandbox/cham tu dong | Kho hon do native binary |
| De cho nguoi hoc | Kho, memory bug |
| Phu hop backend website | Khong phu hop MVP |
| Gia tri portfolio | Cao cho systems |
| Rui ro security | Cao |
| Chi phi trien khai | Cao |

Ket luan: Challenge track, khong nen dua vao judge som.

## Quyet Dinh Ngon Ngu

- Bai thi chinh: Java.
- Co nen da ngon ngu: co, nhung sau khi Java Exam Track on dinh.
- Thu tu mo da ngon ngu: Java -> Kotlin -> Python/Go TCP-UDP extension -> C# backend/WS extension -> TypeScript -> Rust/C++ challenge.
- RMI: giu Java/JVM-only trong Exam Track; ngon ngu khac co bai "RMI concept equivalent" o Backend Track, khong cham nhu INT1433.
- Web Service: giu SOAP/WSDL cho Exam Track; them REST extension rieng.
- Kotlin: nen cho dung chung JVM sau khi co judge Java.
- C#: nen la track mo rong, nhat la SOAP/backend, khong thay Java.
- Python/Go: nen chi dung TCP/UDP extension luc dau.

## Framework/Stack Options

| Phuong an | Toc do MVP | Do phuc tap | Bao mat | Sandbox | Deploy/VPS | Scale | Portfolio | Overengineering |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| A Java-first: Spring Boot + Vue 3 + PostgreSQL + Java/Docker judge | Trung binh | Trung binh | Tot neu tach judge | Rat phu hop Java | Can VPS 2-4GB | Tot | Cao | Vua |
| B Lightweight: FastAPI + Vue 3 + PostgreSQL + Java worker | Nhanh | Thap-trung | Can can than hon | Tot neu worker rieng | Re | Tot cho MVP | Cao | Thap |
| C .NET: ASP.NET Core + Vue/React + PostgreSQL + Java worker | Trung binh | Trung | Tot | Worker rieng | Can VPS 2-4GB | Tot | Cao | Vua |
| D Static content first: Vite/Vue static + YAML + no backend | Rat nhanh | Thap | Rat tot | Khong co judge | Re nhat | Tot cho content | Vua | Thap |

De xuat:

- Stack chinh: A, vi Exam Track Java-first va backend/judge cung ecosystem.
- Stack du phong: B, neu uu tien ra content UI nhanh va tach judge Java thanh service doc lap.
- Phase 1 co the bat dau bang D ngay ca khi sau nay migrate sang A.

