# TCP Byte Stream - Tổng các số nguyên tố

Starter này chỉ minh họa protocol. Điền `HOST`, `PORT`, `TOKEN`, `QCODE` qua biến môi trường hoặc đổi trực tiếp trong code khi luyện tập.

```bash
javac -encoding UTF-8 src/Main.java
HOST=127.0.0.1 PORT=19000 TOKEN=... QCODE=... java -cp src Main
```

Không sử dụng địa chỉ server thi thật. Không giả định một lần `read()` sẽ nhận đủ payload.
