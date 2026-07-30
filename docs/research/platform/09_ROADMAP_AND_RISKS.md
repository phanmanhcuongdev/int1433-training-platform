# 09 - Roadmap And Risks

## Roadmap

| Phase | Noi dung | Effort | Dependency | Risk | Stop condition |
| --- | --- | --- | --- | --- | --- |
| 0 | Chot schema, taxonomy, 10 template | 2-4 ngay | Research hien co | Schema qua phuc tap | Khong tao duoc 5 bai mau nhanh |
| 1 | Content-only site/catalog 24-48 bai | 1-3 tuan | Phase 0 | Content drift, bai trung lap | Bai chi thay so/ten bien |
| 2 | Local judge pilot 6 bai | 2-4 tuan | Starter + mock server lib | Harness flake | Verdict khong on dinh local |
| 3 | Online judge Java-only | 4-8 tuan | Local judge on + sandbox | Security | Chua harden sandbox |
| 4 | Mock exam online | 2-4 tuan | Online judge on | Random unfair | De random qua lech |
| 5 | Multi-language extension | 4+ tuan | Java judge on | Overhead lon | Java Exam Track chua xong |
| 6 | Analytics/classroom | 4+ tuan | User data that | Overbuild | Khong co nguoi dung that |

## Top Risks

1. Overengineering judge som:
   Giai phap: content-only + local judge truoc.

2. Lam bai qua xa thi:
   Giai phap: Exam Track bat buoc co source label va claim ID.

3. Hoc tu/tao bai thay so:
   Giai phap: taxonomy theo protocol failure + payload family, moi bai co objective rieng.

4. Online judge security:
   Giai phap: sandbox hardening, no outbound network, worker tach API.

5. RMI/WS tooling kho:
   Giai phap: Java-only pilot, chot JDK/tooling, template starter ro.

6. Port/IP cu gay hieu nham:
   Giai phap: UI ghi mock host/port configurable; khong goi la server PTIT.

7. Source copy lam lech evidence:
   Giai phap: evidence family, khong tang muc tin cay tu ban copy.

## Dieu Kien Thanh Cong

- Nguoi hoc co the lam bai theo contract, debug duoc loi khong AC.
- Mock exam tao ap luc gan thi nhung khong gia danh he thong chinh thuc.
- Content versioned va traceable.
- Online judge neu co thi an toan hon la tien loi.

