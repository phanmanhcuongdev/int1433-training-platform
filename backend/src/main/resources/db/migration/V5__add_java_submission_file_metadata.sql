alter table submissions
    add column original_file_name varchar(255),
    add column source_sha256 varchar(64);

create index idx_submissions_exercise_participant_created
    on submissions(exercise_id, participant_id, created_at desc);
