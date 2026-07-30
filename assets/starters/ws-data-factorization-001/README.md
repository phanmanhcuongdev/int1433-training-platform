# Web Service DataService - Phân tích thừa số nguyên tố

Phiên luyện tập trả về endpoint SOAP và WSDL URL. Nên sinh client từ WSDL bằng công cụ SOAP phù hợp với JDK/môi trường của bạn, ví dụ `wsimport` trên JDK có JAX-WS tools hoặc Maven plugin `jaxws-maven-plugin`.

Starter này giữ một client tối thiểu bằng JDK để bạn thấy rõ SOAP contract, nhưng phần cần làm vẫn là đọc response, phân tích số và submit danh sách thừa số.

```bash
javac -encoding UTF-8 src/Main.java
WSDL=http://127.0.0.1:8080/ws/factorization.wsdl ENDPOINT=http://127.0.0.1:8080/ws TOKEN=... QCODE=... java -cp src Main
```

Contract chính:

- Operation `request(token, qCode)` trả về `n`.
- Operation `submit(token, qCode, factors[])` trả về `accepted`.
- Namespace: `http://training.int1433.ptit.edu.vn/ws/factorization`.
