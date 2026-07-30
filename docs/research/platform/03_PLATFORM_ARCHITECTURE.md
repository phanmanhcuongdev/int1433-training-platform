# 03 - Platform Architecture

## Phase 1 - Content-only

Chuc nang:

- Danh sach bai.
- Filter protocol, technology, stream/service, level, source label.
- De bai, hint, solution, starter code.
- Download local harness/starter project neu co.
- Khong submit online.

Component diagram:

```text
Markdown/YAML content repo
        |
        v
Content loader/build script
        |
        v
Static/Vue site ----> User browser
```

Nen lam ngay vi rui ro thap, tao gia tri hoc nhanh, va giup chuan hoa taxonomy/schema truoc judge.

## Phase 2 - Local Judge

Chuc nang:

- Tai starter project.
- Chay mock server local.
- Chay script kiem tra client.
- Tra `AC`, `WA`, `Protocol Error`, `Timeout`, `Runtime Error`.
- Luu protocol log local.

Data flow:

```text
Exercise YAML -> generate starter/harness
Student code -> local script -> mock server
                         |       |
                         v       v
                    protocol log + verdict
```

Gia tri: giai quyet phan kho nhat cua mon la protocol contract ma khong can online sandbox.

## Phase 3 - Online Judge

Chuc nang:

- Submit source.
- Build trong sandbox.
- Chay client voi mock server rieng.
- Gioi han CPU/RAM/time/process.
- No outbound network.
- Log protocol, verdict, compile output ngan.

Judge flow:

```text
Browser -> API -> submission queue -> judge worker
                                  |
                                  v
                         ephemeral sandbox
                         /      |       \
                    compile  mock server  client run
                         \      |       /
                          verdict + protocol log
```

Online judge chi nen lam khi:

- Co it nhat 20 bai content da on.
- Co local judge cho TCP/UDP/RMI/WS.
- Co threat model va sandbox policy test duoc.

## Phase 4 - Mock Exam

Chuc nang:

- Tao de 6 bai random theo blueprint.
- Timer.
- AC state tung bai.
- Khoa hint/solution trong ca thi.
- Ket thuc ca, hien loi, thong ke.

Mock exam nen dung chung engine voi Phase 3, hoac local exam mode neu chua co online judge.

## Phase 5 - Advanced Platform

Chuc nang:

- Classroom.
- Leaderboard.
- Analytics loi hay gap.
- Recommendation.
- Spaced repetition.
- Teacher dashboard.

Chua nen lam trong MVP. Chi lam khi co user that va data submission that.

## Dieu Kien Chuyen Phase

| Phase | Chuyen khi | Stop condition |
| --- | --- | --- |
| 1 -> 2 | Schema on, 12 bai content dau tien co starter | Bai tap van doi format lien tuc |
| 2 -> 3 | Local judge chay du 4 technology chinh | Chua co sandbox threat model |
| 3 -> 4 | Online judge verdict on dinh, co pool >= 36 bai | Verdict hay flake/time out |
| 4 -> 5 | Co nhom nguoi dung that va nhu cau quan ly | Chua co du submission data |

## Ranh Gioi He Thong

Nen tach:

- Web app: content, user, submission metadata.
- Judge worker: build/run untrusted code.
- Mock server library: protocol contracts.
- Content repo: bai tap versioned.

Khong can microservices som. API monolith + worker queue la du.

