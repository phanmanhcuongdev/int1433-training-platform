update exercises
set submission_format = 'Upload đúng một file fnd-character-flush-001.java. Mã nguồn bên trong phải chứa public class Main.',
    examples_json = '[
      {
        "title": "Ví dụ 1",
        "input": "   hello      java",
        "output": "HELLO JAVA",
        "explanation": "Bỏ khoảng trắng hai đầu, gom nhiều khoảng trắng liên tiếp thành một dấu cách và chuyển sang chữ hoa."
      },
      {
        "title": "Ví dụ 2",
        "input": "  Java   Stream   and   Flush",
        "output": "JAVA STREAM AND FLUSH",
        "explanation": "Kết quả phải là đúng một dòng output, có newline và flush sau khi ghi."
      },
      {
        "title": "Ví dụ 3",
        "input": "xin Chào mạng",
        "output": "XIN CHÀO MẠNG",
        "explanation": "Input UTF-8 được giữ Unicode hợp lệ khi chuyển chữ hoa."
      }
    ]'::jsonb
where id = 'fnd-character-flush-001';

update exercises
set submission_format = 'Upload đúng một file fnd-data-order-001.java. Chương trình ghi int gcd, long lcm, long total, double doubled theo chuẩn DataOutputStream.',
    examples_json = '[
      {
        "title": "Ví dụ 1",
        "input": "a=12, b=18, c=5, d=1.5",
        "output": "gcd=6, lcm=36, total=35, doubled=3.0",
        "explanation": "Ví dụ mô tả giá trị logic; dữ liệu thật của judge là binary primitive, không phải text."
      },
      {
        "title": "Ví dụ 2",
        "input": "a=7, b=13, c=100, d=2.25",
        "output": "gcd=1, lcm=91, total=120, doubled=4.5",
        "explanation": "Output phải được ghi bằng DataOutputStream theo đúng thứ tự primitive."
      },
      {
        "title": "Ví dụ 3",
        "input": "a=21, b=21, c=0, d=-1.0",
        "output": "gcd=21, lcm=21, total=42, doubled=-2.0",
        "explanation": "Không in chuỗi mô tả ra stdout trong bài binary contract."
      }
    ]'::jsonb
where id = 'fnd-data-order-001';
