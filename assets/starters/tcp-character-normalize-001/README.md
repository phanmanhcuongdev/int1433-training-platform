# TCP Character Stream - Chuẩn hóa từ

Protocol theo dòng UTF-8. Request và submission đều cần kết thúc bằng newline và flush.

```bash
javac -encoding UTF-8 src/Main.java
HOST=127.0.0.1 PORT=19000 TOKEN=... QCODE=... java -cp src Main
```
