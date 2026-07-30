# 02 - Final Exam Format

## Hinh Thuc Moi Nhat Tim Thay

Theo `Danh gia, cho diem LTM Ky dong 2025.docx`:

- Thi KTHP chiem 50%.
- Thi truc tiep tai phong may nha A3.
- De gom 6 bai.
- Co it nhat 1 TCP, 1 UDP, 1 RMI, 1 Web Service.
- 2 bai con lai co the vao bat ky dang nao.
- Cham theo so bai AC tren he thong.

Tai lieu nay khong noi ro thoi luong thi KTHP, duoc dung tai lieu hay khong, co Internet hay khong. Tuy nhien cung file noi cac bai kiem tra truc tiep 5 bai keo dai 60 phut; de cuong 2024-2025 noi KTHP 5 bai, 60 phut. Khong du co so de gan 60 phut cho KTHP 2025-2026 neu chi dua vao file moi.

## Hinh Thuc 2024-2025

Theo `DE CUONG MON HOC.pdf`:

- Thi tai phong may.
- De thi gom 5 bai.
- Thoi gian lam bai 60 phut.
- Ca nhan.
- Thang diem theo so bai dung.

Bang `THI_KTHP_CQ.xlsx` cho thay KTHP 2024-2025 co ca thi ngay 19/12/2024, gio 08h00 va nhieu ca khac trong cung file, diem KTHP, va ghi chu `Khong du DKDT`.

## He Thong Va Quy Trinh

Nguon chinh thuc 2025-2026:

- Trang luyen tap: `http://203.162.10.109/`.
- Tai khoan va mat khau mac dinh: ma sinh vien viet hoa.
- Kiem tra truc tuyen/luyen tap lam tren dia chi tren.
- Thi truc tiep van dung tai khoan/mat khau nay, nhung khong thi tai dia chi luyen tap ma thi tren client co giam sat, thong bao tai phong thi truoc gio thi.

Nguon ghi chu sinh vien 20/12/2024:

- Web luyen thi: `http://203.162.10.109/Webcms`.
- IP dua vao Socket cung la IP web.
- MSV da/ dang thi thi de bai bi an, port cu bi khoa; code tren git co the khong ping lai duoc.
- NetBeans: tao Java Application, khong tao Java with Maven.
- Web Service: khong dung JDK thap; moi truong phong thi dung JDK cao.

## Noi Dung Thi

| Nam/ky | TCP | UDP | RMI | Web Service | Ghi chu |
| --- | --- | --- | --- | --- | --- |
| HK1 2024-2025 | Co trong de cuong, de giua ky, code va de on | Co trong de cuong, de on, diem UDP | RMI co trong note va mau de on cuoi ky | WS co trong note va mau de on cuoi ky | KTHP 2024-2025 chi ghi tong hop noi dung thi phong may, khong noi ro ty le tung giao thuc |
| Dong 2025-2026 | Bat buoc it nhat 1 bai | Bat buoc it nhat 1 bai | Bat buoc it nhat 1 bai | Bat buoc it nhat 1 bai | 2/6 bai con lai co the vao bat ky dang nao |

Noi dung thuong gap trong de on va code:

- TCP: byte stream, data stream, character stream, object stream; port 2206, 2207, 2208, 2209 trong bo de 2024/2025; mot so de TCP on tap cu dung host `172.188.19.218` va port 1604/1605/1606.
- UDP: data/string/object; port 2207/2208/2209; requestId 8 ky tu trong object packet hoac prefix `requestId;...` trong chuoi.
- RMI: DataService, CharacterService, ByteService, ObjectService; registry service names `RMIDataService`, `RMICharacterService`, `RMIByteService`, `RMIObjectService`.
- Web Service: endpoint mau `http://<Exam_IP>:8080/JNPWS/{DataService|CharacterService|ObjectService}?wsdl`; package generated `vn.medianews`; method request/submit theo service.

## Cham Tu Dong Va AC

Nguon 2025-2026 noi ro `Chi xet so bai AC tren he thong`. Ghi chu sinh vien goi day la mon thi AC va nhac cac loi lam khong co log/khong AC.

Khong tim thay quy dinh chinh thuc ve:

- moi bai bao nhieu diem rieng;
- co diem tung test hay khong;
- co submit lai nhieu lan hay gioi han submit hay khong;
- console output co bi cham hay khong.

Nhung pattern de bai cho thay console output khong phai kenh nop dap an; dap an duoc gui lai server qua socket/RMI/WS. Ghi chu sinh vien noi `toString`/in ra man hinh chi de kiem tra, vi nop object chu khong in nhu CodePTIT.

