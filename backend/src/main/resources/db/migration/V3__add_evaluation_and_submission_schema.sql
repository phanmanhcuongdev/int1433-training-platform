alter table exercises
    add column evaluation_mode varchar(40),
    add column grader_key varchar(160),
    add column time_limit_ms integer,
    add column memory_limit_mb integer,
    add column network_session_ttl_seconds integer,
    add column max_attempts integer,
    add column starter_asset_path text,
    add column language_policy jsonb,
    add column verdict_policy jsonb,
    add column constraints_json jsonb,
    add column examples_json jsonb,
    add column evidence_disclaimer text;

create table submissions (
    id uuid primary key,
    exercise_id varchar(120) not null references exercises(id),
    participant_id uuid not null,
    evaluation_mode varchar(40) not null,
    language varchar(40),
    source_code text,
    submitted_answer jsonb,
    status varchar(40) not null,
    verdict varchar(40) not null,
    score numeric(5,2) not null default 0,
    diagnostic_code varchar(160),
    public_message text,
    compile_output text,
    runtime_output text,
    created_at timestamptz not null default now(),
    judged_at timestamptz
);

create table submission_test_results (
    id bigserial primary key,
    submission_id uuid not null references submissions(id) on delete cascade,
    test_index integer not null,
    verdict varchar(40) not null,
    execution_time_ms integer not null default 0,
    memory_kb integer,
    diagnostic_code varchar(160),
    public_message text,
    unique(submission_id, test_index)
);

create table challenge_sessions (
    id uuid primary key,
    exercise_id varchar(120) not null references exercises(id),
    participant_id uuid not null,
    token_hash varchar(128) not null,
    q_code varchar(80) not null,
    request_id varchar(160),
    host_metadata text,
    port_metadata integer,
    endpoint_metadata text,
    state varchar(40) not null,
    verdict varchar(40) not null,
    diagnostic_code varchar(160),
    public_message text,
    seed bigint not null,
    expected_answer jsonb,
    payload jsonb,
    protocol_trace jsonb not null default '[]'::jsonb,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    completed_at timestamptz
);

create index idx_submissions_participant_created on submissions(participant_id, created_at desc);
create index idx_submissions_exercise on submissions(exercise_id);
create index idx_challenge_sessions_participant_created on challenge_sessions(participant_id, created_at desc);
create index idx_challenge_sessions_state_expires on challenge_sessions(state, expires_at);
create index idx_challenge_sessions_exercise on challenge_sessions(exercise_id);
