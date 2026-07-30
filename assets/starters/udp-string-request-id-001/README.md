# UDP String - Giữ nguyên requestId

Đọc và gửi datagram theo đúng `packet.getLength()`. Không dùng toàn bộ buffer.

```bash
javac -encoding UTF-8 src/Main.java
HOST=127.0.0.1 PORT=19100 TOKEN=... QCODE=... java -cp src Main
```
