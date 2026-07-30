# Web Service DataService - Phân tích thừa số nguyên tố

Phiên trả về WSDL endpoint. Trong bản starter tối thiểu này có ví dụ SOAP thủ công bằng JDK để kiểm tra contract; khi học thật có thể sinh client từ WSDL bằng công cụ SOAP phù hợp.

```bash
javac -encoding UTF-8 src/Main.java
ENDPOINT=http://127.0.0.1:8080/ws/factorization TOKEN=... QCODE=... java -cp src Main
```
