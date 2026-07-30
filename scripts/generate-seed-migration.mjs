import { readdir, readFile, writeFile } from 'node:fs/promises';
import path from 'node:path';
import process from 'node:process';

const root = process.cwd();
const exercisesDir = path.join(root, 'content', 'exercises');
const outputFile = path.join(root, 'backend', 'src', 'main', 'resources', 'db', 'migration', 'V4__seed_complete_ten_exercises.sql');

async function listJsonFiles(dir) {
  const entries = await readdir(dir, { withFileTypes: true });
  const files = [];
  for (const entry of entries) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      files.push(...await listJsonFiles(fullPath));
    } else if (entry.isFile() && entry.name.endsWith('.json')) {
      files.push(fullPath);
    }
  }
  return files;
}

function sqlString(value) {
  if (value === null || value === undefined) {
    return 'null';
  }
  return `'${String(value).replaceAll("'", "''")}'`;
}

function sqlJson(value) {
  if (value === null || value === undefined) {
    return 'null';
  }
  return `${sqlString(JSON.stringify(value))}::jsonb`;
}

function minutes(value) {
  const match = String(value).match(/^(?:(\d+)h)?(?:(\d+)m)?$/);
  if (!match) {
    throw new Error(`Invalid estimated_time: ${value}`);
  }
  return Number(match[1] || 0) * 60 + Number(match[2] || 0);
}

function timeoutConfig(exercise) {
  return exercise.timeout || null;
}

function timeLimit(exercise) {
  if (exercise.judge?.time_limit_ms) return exercise.judge.time_limit_ms;
  if (exercise.timeout?.run_ms) return exercise.timeout.run_ms;
  if (exercise.timeout?.io_ms) return exercise.timeout.io_ms;
  return null;
}

function languagePolicy(exercise) {
  if (exercise.judge?.language_policy) return exercise.judge.language_policy;
  if (exercise.evaluation_mode === 'JAVA_CODE' || exercise.technology === 'RMI') return ['JAVA'];
  return ['JAVA'];
}

const files = await listJsonFiles(exercisesDir);
const exercises = [];
for (const file of files) {
  exercises.push(JSON.parse(await readFile(file, 'utf8')));
}
exercises.sort((a, b) => (a.display_order ?? 9999) - (b.display_order ?? 9999) || a.id.localeCompare(b.id));

if (exercises.length !== 10) {
  throw new Error(`Expected exactly 10 exercises, found ${exercises.length}`);
}

const ids = exercises.map((exercise) => exercise.id);
const idList = ids.map(sqlString).join(', ');
const lines = [];

lines.push('-- Generated from content/exercises/*.json by scripts/generate-seed-migration.mjs.');
lines.push('-- Do not edit committed Flyway migrations after release.');
lines.push('delete from exercise_tags where exercise_id not in (' + idList + ');');
lines.push('delete from exercise_common_failures where exercise_id not in (' + idList + ');');
lines.push('delete from exercise_hints where exercise_id not in (' + idList + ');');
lines.push('delete from exercise_learning_objectives where exercise_id not in (' + idList + ');');
lines.push('delete from exercise_prerequisites where exercise_id not in (' + idList + ');');
lines.push('delete from exercise_sources where exercise_id not in (' + idList + ');');
lines.push('delete from exercises where id not in (' + idList + ');');
lines.push('');

for (const exercise of exercises) {
  lines.push(`insert into exercises (
    id, title, summary, status, track, technology, protocol, transport, stream_type,
    difficulty, level, source_label, statement, processing_requirement, request_format,
    response_format, submission_format, estimated_time_minutes, display_order,
    server_contract, timeout_config, evaluation_mode, grader_key, time_limit_ms,
    memory_limit_mb, network_session_ttl_seconds, max_attempts, starter_asset_path,
    language_policy, verdict_policy, constraints_json, examples_json, evidence_disclaimer
) values (
    ${sqlString(exercise.id)}, ${sqlString(exercise.title)}, ${sqlString(exercise.summary)}, ${sqlString(exercise.status)},
    ${sqlString(exercise.track)}, ${sqlString(exercise.technology)}, ${sqlString(exercise.protocol)}, ${sqlString(exercise.transport)},
    ${sqlString(exercise.stream_type)}, ${sqlString(exercise.difficulty)}, ${sqlString(exercise.level)}, ${sqlString(exercise.source_label)},
    ${sqlString(exercise.statement)}, ${sqlString(exercise.processing_requirement)}, ${sqlString(exercise.request_format)},
    ${sqlString(exercise.response_format)}, ${sqlString(exercise.submission_format)}, ${minutes(exercise.estimated_time)}, ${exercise.display_order},
    ${sqlJson(exercise.server_contract)}, ${sqlJson(timeoutConfig(exercise))}, ${sqlString(exercise.evaluation_mode)}, ${sqlString(exercise.grader_key)},
    ${timeLimit(exercise)}, ${exercise.judge?.memory_limit_mb ?? 'null'}, ${exercise.timeout?.session_ttl_seconds ?? exercise.judge?.network_session_ttl_seconds ?? 'null'},
    ${exercise.judge?.max_attempts ?? 'null'}, ${sqlString(exercise.starter_asset_path)}, ${sqlJson(languagePolicy(exercise))},
    ${sqlJson(exercise.verdict_definitions)}, ${sqlJson(exercise.constraints)}, ${sqlJson(exercise.examples)}, ${sqlString(exercise.evidence_disclaimer)}
) on conflict (id) do update set
    title = excluded.title,
    summary = excluded.summary,
    status = excluded.status,
    track = excluded.track,
    technology = excluded.technology,
    protocol = excluded.protocol,
    transport = excluded.transport,
    stream_type = excluded.stream_type,
    difficulty = excluded.difficulty,
    level = excluded.level,
    source_label = excluded.source_label,
    statement = excluded.statement,
    processing_requirement = excluded.processing_requirement,
    request_format = excluded.request_format,
    response_format = excluded.response_format,
    submission_format = excluded.submission_format,
    estimated_time_minutes = excluded.estimated_time_minutes,
    display_order = excluded.display_order,
    server_contract = excluded.server_contract,
    timeout_config = excluded.timeout_config,
    evaluation_mode = excluded.evaluation_mode,
    grader_key = excluded.grader_key,
    time_limit_ms = excluded.time_limit_ms,
    memory_limit_mb = excluded.memory_limit_mb,
    network_session_ttl_seconds = excluded.network_session_ttl_seconds,
    max_attempts = excluded.max_attempts,
    starter_asset_path = excluded.starter_asset_path,
    language_policy = excluded.language_policy,
    verdict_policy = excluded.verdict_policy,
    constraints_json = excluded.constraints_json,
    examples_json = excluded.examples_json,
    evidence_disclaimer = excluded.evidence_disclaimer,
    updated_at = now();`);
}

lines.push('');
lines.push('delete from exercise_tags where exercise_id in (' + idList + ');');
lines.push('delete from exercise_common_failures where exercise_id in (' + idList + ');');
lines.push('delete from exercise_hints where exercise_id in (' + idList + ');');
lines.push('delete from exercise_learning_objectives where exercise_id in (' + idList + ');');
lines.push('delete from exercise_prerequisites where exercise_id in (' + idList + ');');
lines.push('delete from exercise_sources where exercise_id in (' + idList + ');');

for (const exercise of exercises) {
  for (const tag of exercise.tags || []) {
    lines.push(`insert into exercise_tags (exercise_id, tag) values (${sqlString(exercise.id)}, ${sqlString(tag)});`);
  }
  for (const [index, failure] of (exercise.common_failures || []).entries()) {
    lines.push(`insert into exercise_common_failures (exercise_id, failure_code, display_order) values (${sqlString(exercise.id)}, ${sqlString(failure)}, ${index + 1});`);
  }
  for (const [index, hint] of (exercise.hints || []).entries()) {
    lines.push(`insert into exercise_hints (exercise_id, content, display_order) values (${sqlString(exercise.id)}, ${sqlString(hint)}, ${index + 1});`);
  }
  for (const [index, objective] of (exercise.learning_objectives || []).entries()) {
    lines.push(`insert into exercise_learning_objectives (exercise_id, objective, display_order) values (${sqlString(exercise.id)}, ${sqlString(objective)}, ${index + 1});`);
  }
  for (const [index, prerequisite] of (exercise.prerequisites || []).entries()) {
    lines.push(`insert into exercise_prerequisites (exercise_id, prerequisite, display_order) values (${sqlString(exercise.id)}, ${sqlString(prerequisite)}, ${index + 1});`);
  }
  const maxSources = Math.max(exercise.source_claim_ids?.length || 0, exercise.source_files?.length || 0);
  for (let index = 0; index < maxSources; index += 1) {
    lines.push(`insert into exercise_sources (exercise_id, claim_id, source_file, evidence_note) values (${sqlString(exercise.id)}, ${sqlString(exercise.source_claim_ids?.[index] || null)}, ${sqlString(exercise.source_files?.[index] || null)}, ${sqlString(exercise.evidence_disclaimer)});`);
  }
}

lines.push('');
lines.push("alter table exercises alter column evaluation_mode set not null;");
lines.push("alter table exercises alter column grader_key set not null;");
lines.push("alter table exercises alter column evidence_disclaimer set not null;");

await writeFile(outputFile, `${lines.join('\n')}\n`, 'utf8');
console.log(`Wrote ${path.relative(root, outputFile)} for ${exercises.length} exercises.`);
