alter table submissions
    add column entry_class_name varchar(255);

update submissions
set entry_class_name = 'Main'
where evaluation_mode = 'JAVA_CODE'
  and entry_class_name is null;
