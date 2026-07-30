# 06 - Evidence Matrix

| Claim ID | Khang dinh | Nguon | Loai nguon | Nam/ky | Muc tin cay | Trich dan/ngu canh |
| --- | --- | --- | --- | --- | --- | --- |
| CLAIM-GRADING-001 | Ty trong tong ket 2024-2025 la CC 10, TBKT/Thuc hanh 20, BTL 20, KTHP 50 | `DE CUONG MON HOC.pdf` | De cuong hoc phan | HK1 2024-2025 | OFFICIAL | Bang `Cac dau diem danh gia mon hoc` |
| CLAIM-GRADING-002 | Bang diem 2024-2025 dung trong so 10/20/20/50 | `BĐHP - INT1433...2024.xlsx` | Bang diem tong ket | HK1 2024-2025 | OFFICIAL | Hang `Trong so`: 10,20,20,50 |
| CLAIM-GRADING-003 | Cong thuc tong ket khop 0.1*CC + 0.2*TBKT + 0.2*BTL + 0.5*THI | `BĐHP - INT1433...2024.xlsx` | Bang diem | HK1 2024-2025 | OFFICIAL | Vi du CC=10, TBKT=3.5, BTL=8.5, THI=4 -> 5.4 |
| CLAIM-GRADING-004 | Dong 2025-2026 van giu CC 10, TBKT 20, BTL 20, KTHP 50 | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia/cho diem | Dong 2025-2026 | OFFICIAL | Cac muc ghi trong so tung dau diem |
| CLAIM-GRADING-005 | Diem chuyen can 2025-2026 tinh qua ZoomMonitor va thoi gian online 50-75% | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | Muc `Diem Chuyen can` |
| CLAIM-GRADING-006 | TBKT 2025-2026 chia online 20% va truc tiep 80%, noi dung TCP+UDP | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | Muc `Diem Trung binh kiem tra` |
| CLAIM-GRADING-007 | BTL 2025-2026 chia online RMI/WS va truc tiep RMI+WS | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | Muc `Diem Bai tap lon` |
| CLAIM-FINAL-EXAM-001 | KTHP 2024-2025 thi phong may, 5 bai, 60 phut | `DE CUONG MON HOC.pdf` | De cuong hoc phan | HK1 2024-2025 | OFFICIAL | Muc `Kiem tra cuoi ky` |
| CLAIM-FINAL-EXAM-002 | KTHP 2025-2026 thi truc tiep phong may A3, 6 bai, gom TCP/UDP/RMI/WS | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | Muc `Thi Ket thuc hoc phan` |
| CLAIM-FINAL-EXAM-003 | KTHP 2025-2026 cham theo so bai AC | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | `Chi xet so bai AC theo he thong` |
| CLAIM-FINAL-EXAM-004 | Thang diem 2025-2026: 0/1/2/3/4/5/6 AC -> 1/4/6/7/8/9/10 | `Danh gia, cho diem LTM Ky dong 2025.docx` | Tai lieu danh gia | Dong 2025-2026 | OFFICIAL | Bang thang diem KTHP |
| CLAIM-FINAL-EXAM-005 | KTHP D21 co ca thi ngay 19/12/2024 va co ghi `Khong du DKDT` | `THI_KTHP_CQ.xlsx` | Bang diem/KTHP | HK1 2024-2025 | OFFICIAL | Cot `Ca thi`, `Ngay`, `Diem thi KTHP`, `Ghi chu` |
| CLAIM-ELIGIBILITY-001 | Co sinh vien khong du dieu kien du thi | `THI_KTHP_CQ.xlsx`; `BĐTP...2024.xlsx`; `BĐHP...2024.xlsx` | Bang diem | 2024-2025 | OFFICIAL | Ghi chu `Khong du DKDT`, `KD DK` |
| CLAIM-ELIGIBILITY-002 | Chua thay dieu kien dinh luong de du thi | Toan bo nguon da doc | Tong hop | Nhieu nam | INFERENCE | Chi thay ket qua DKDT, khong thay quy tac |
| CLAIM-TCP-001 | TCP request thuong la `studentCode;qCode` | De TCP PDF/DOCX, code | De on/loi giai | 2024-2025 | STUDENT_NOTE/INFERENCE | Nhieu de ghi format nay |
| CLAIM-TCP-002 | TCP object request can `writeObject`, khong `writeUTF` | `NHUNG LUU Y...docx` | Ghi chu sinh vien | 2024-12-20 | STUDENT_NOTE | Ghi ro `writeObject moi chay duoc` |
| CLAIM-UDP-001 | UDP request dau tien la `;studentCode;qCode` | Mau de UDP PDF/DOCX | De on | 2024-2025 | STUDENT_NOTE | Nhieu de UDP ghi format nay |
| CLAIM-UDP-002 | UDP object packet co 8 byte dau requestId | Mau de UDP PDF | De on | 2024 | STUDENT_NOTE | BAI Object UDP Product/Customer |
| CLAIM-RMI-001 | RMI dung service names RMIDataService/RMICharacterService/RMIByteService/RMIObjectService | Mau de RMI PDF | De on | 2024 | STUDENT_NOTE | Mo ta interface va RegistryServer |
| CLAIM-WS-001 | WS dung endpoint JNPWS va WSDL Data/Character/ObjectService | Mau de WS DOCX | De on | 2024 | STUDENT_NOTE | `http://<Exam_IP>:8080/JNPWS/...` |
| CLAIM-FAILURE-001 | Sai package object lam khong chay/khong log | `NHUNG LUU Y...docx` | Ghi chu sinh vien | 2024 | STUDENT_NOTE | Package phai dung va cung cap |
| CLAIM-FAILURE-002 | Can close/flush khi nop socket | `NHUNG LUU Y...docx`; de bai | Ghi chu + de on | 2024 | STUDENT_NOTE | `gui cai gi xong flush`, `dong ket noi` |
| CLAIM-CONFLICT-001 | Co file diem D22 2025-2026 dang tieu chi bao ve du an, khac KTHP AC 6 bai | `Diem thi Lap trinh mang - D22.pdf`; `Danh gia...2025.docx` | Bang diem vs tai lieu danh gia | 2025-2026 | OFFICIAL, conflict | Can xac minh ngu canh file diem |

