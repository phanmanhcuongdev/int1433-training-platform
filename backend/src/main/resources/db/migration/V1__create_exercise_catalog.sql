create table exercises (
    id varchar(120) primary key,
    title varchar(255) not null,
    summary text,
    status varchar(32) not null,
    track varchar(64) not null,
    technology varchar(120) not null,
    protocol varchar(80),
    transport varchar(80),
    stream_type varchar(120),
    difficulty varchar(32) not null,
    level varchar(16) not null,
    source_label varchar(64) not null,
    statement text not null,
    processing_requirement text not null,
    request_format text,
    response_format text,
    submission_format text,
    estimated_time_minutes integer not null,
    display_order integer not null,
    server_contract jsonb,
    timeout_config jsonb,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table exercise_tags (
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    tag varchar(120) not null,
    primary key (exercise_id, tag)
);

create table exercise_common_failures (
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    failure_code varchar(160) not null,
    display_order integer not null,
    primary key (exercise_id, failure_code)
);

create table exercise_hints (
    id bigserial primary key,
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    content text not null,
    display_order integer not null
);

create table exercise_learning_objectives (
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    objective text not null,
    display_order integer not null,
    primary key (exercise_id, display_order)
);

create table exercise_prerequisites (
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    prerequisite text not null,
    display_order integer not null,
    primary key (exercise_id, display_order)
);

create table exercise_sources (
    id bigserial primary key,
    exercise_id varchar(120) not null references exercises(id) on delete cascade,
    claim_id varchar(160),
    source_file text,
    evidence_note text
);

create index idx_exercises_catalog_sort on exercises(display_order, id);
create index idx_exercises_technology on exercises(technology);
create index idx_exercises_level on exercises(level);
create index idx_exercises_source_label on exercises(source_label);
create index idx_exercises_status on exercises(status);
create index idx_exercise_tags_tag on exercise_tags(tag);
