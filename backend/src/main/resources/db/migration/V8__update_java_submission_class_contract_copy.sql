update exercises
set submission_format = 'Upload đúng một file .java. Tên file Java phải trùng với tên top-level public class, không khai báo package, và class đó phải có public static void main(String[] args).',
    server_contract = jsonb_set(server_contract, '{entrypoint}', '"Tên file Java trùng với top-level public class"', true)
where evaluation_mode = 'JAVA_CODE';
