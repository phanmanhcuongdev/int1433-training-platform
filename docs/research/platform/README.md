# INT1433 Training Platform Research

## Ket Luan Nhanh

Huong nen lam: xay dung mot nen tang luyen tap khong chinh thuc, Java-first, bat dau bang content-only platform va local mock server truoc khi lam online judge.

Track nen tach ro:

- Exam Track: Java, bam sat TCP/UDP/RMI/Web Service SOAP, AC theo du lieu gui len server, khong cham console output.
- Extended Networking Track: Python/Go/Kotlin/C# cho TCP/UDP va cac bai network that hon.
- Backend/Distributed Systems Track: REST, gRPC, message queue, observability, security, nhung khong dua vao mock exam INT1433 mac dinh.

Stack chinh de xuat:

- Backend: Spring Boot.
- Frontend: Vue 3.
- Database: PostgreSQL.
- Content format: YAML/Markdown trong repo.
- Judge: Phase 2 local Java harness; Phase 3 Java worker chay trong container sandbox.

Stack du phong:

- Backend: FastAPI.
- Frontend: Vue 3.
- Database: PostgreSQL.
- Judge service: Java worker rieng.

MVP de xuat: Balanced MVP 48 bai + 3 mock exam template, khong tao hang tram bai ngay.

| Nhom | So bai MVP |
| --- | ---: |
| Java Stream Foundation | 8 |
| TCP | 12 |
| UDP | 8 |
| RMI | 6 |
| Web Service | 6 |
| Debugging | 8 |
| Mock exam set | 3 template |

Buoc tiep theo nen lam:

1. Chot schema bai tap YAML.
2. Tao 8-12 bai content-only dau tien.
3. Viet local mock server/harness cho 1 TCP Byte, 1 UDP String, 1 RMI Data.
4. Moi khi local harness on dinh moi tinh online judge.

Nhung khong nen lam ngay: multi-language judge, Kubernetes, leaderboard phuc tap, AI recommendation, realtime collaboration, he thong thi co giam sat.

