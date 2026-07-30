# UDP Object - Sửa dữ liệu Product

Datagram phản hồi có 8 byte đầu là `requestId`; phần còn lại là object `Product` đã serialize. Khi nộp lại, phải giữ nguyên đúng 8 byte này.

```bash
javac -encoding UTF-8 -d out src/Main.java src/vn/edu/ptit/int1433/training/contract/Product.java
HOST=127.0.0.1 PORT=19100 TOKEN=... QCODE=... java -cp out Main
```
