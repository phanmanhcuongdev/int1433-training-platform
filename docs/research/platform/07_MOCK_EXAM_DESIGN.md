# 07 - Mock Exam Design

## Blueprint 6 Bai

Bat buoc:

- 1 TCP.
- 1 UDP.
- 1 RMI.
- 1 Web Service.
- 2 bai random tu pool con lai.

Rang buoc:

- Toi da 2 bai cung mot technology, tru khi pool qua nho.
- It nhat 1 bai object/list/matrix khi level >= L4.
- Khong lay bai Debugging lam mock exam mac dinh, tru khi mode `diagnostic`.
- Khong dung IP/port cu; mock exam sinh host/port local/session.

## Randomization

Moi bai co metadata:

- `technology`
- `stream_type`/`service`
- `level`
- `estimated_time`
- `source_label`
- `payload_family`

Algorithm:

1. Pick required technology slots.
2. Pick 2 flexible slots with diversity constraints.
3. Ensure total estimated time target.
4. Avoid same payload family repeated.
5. Generate per-session qCode/requestId/port.

## Scoring

Neu theo pattern 2025-2026:

| AC | 0 | 1 | 2 | 3 | 4 | 5 | 6 |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Score | 1 | 4 | 6 | 7 | 8 | 9 | 10 |

Nhung nen hien ro day la mock scoring, khong phai quy dinh chinh thuc hien hanh cua PTIT.

Verdict tung bai:

- `AC`: dung protocol va payload.
- `WA`: dung protocol, sai ket qua.
- `Protocol Error`: sai request/submit/order/requestId/package/service.
- `Timeout`: block/qua thoi gian.
- `Runtime Error`: crash.
- `Compile Error`: build fail.

## Timer

- Default 60 phut cho mock theo de cuong 2024-2025.
- Neu mock 6 bai theo 2025-2026, cho phep config 60/75/90 phut vi thoi luong KTHP 2025-2026 chua tim thay.
- UI phai ghi `training timer`, khong noi la lich thi chinh thuc.

## Fairness

- Moi de co tong difficulty gan nhau.
- Khong lap qCode/payload y het.
- Hidden cases can bang: whitespace, empty-ish data, duplicate, boundary size.
- Neu online judge, moi session co mock server rieng de tranh cross-talk.

## Result State

Trong luc lam:

- Hien AC/WA/PE/TLE/RE.
- An solution.
- Hint tuy mode: exam mode khoa, practice mode mo theo tung muc.

Sau khi ket thuc:

- Hien protocol log rut gon.
- Hien common failure mapping.
- Goi y bai remedial theo loi.

