import { readdir, readFile, stat } from 'node:fs/promises';
import path from 'node:path';
import process from 'node:process';

const root = process.cwd();
const exercisesDir = path.join(root, 'content', 'exercises');

const requiredFields = [
  'id',
  'title',
  'track',
  'technology',
  'difficulty',
  'level',
  'source_label',
  'statement',
  'processing_requirement',
  'evaluation_mode',
  'grader_key',
  'common_failures',
  'tags',
  'estimated_time'
];

const enums = {
  status: ['DRAFT', 'REVIEWED', 'PUBLISHED', 'DEPRECATED'],
  source_label: ['OBSERVED', 'STRONG_PATTERN', 'EXTENDED', 'CHALLENGE'],
  track: ['EXAM', 'EXTENDED_NETWORKING', 'BACKEND_DISTRIBUTED'],
  difficulty: ['EASY', 'MEDIUM', 'HARD'],
  level: ['L0', 'L1', 'L2', 'L3', 'L4', 'L5', 'L6'],
  evaluation_mode: ['OUTPUT_CHECK', 'JAVA_CODE', 'NETWORK_CHALLENGE']
};

const realIpPattern = /203\.162\.10\.109|172\.188\.19\.218/;
const unfinishedTextPattern = /\b(?:TODO|FIXME|lorem ipsum|placeholder)\b/i;
const idPattern = /^[a-z0-9]+(?:-[a-z0-9]+)*$/;
const estimatedTimePattern = /^(?:[1-9]\d*h(?:[1-5]?\dm)?|[1-9]\d*m)$/;

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

function hasDuplicates(items) {
  return new Set(items).size !== items.length;
}

function isRelativeRepoPath(value) {
  return typeof value === 'string' &&
    value.length > 0 &&
    !path.isAbsolute(value) &&
    !value.split(/[\\/]/).includes('..');
}

async function fileExists(relPath) {
  try {
    const entry = await stat(path.join(root, relPath));
    return entry.isFile();
  } catch {
    return false;
  }
}

async function validateExercise(file, data, seenIds) {
  const errors = [];
  const rel = path.relative(root, file);

  for (const field of requiredFields) {
    if (!(field in data)) {
      errors.push(`${rel}: missing required field "${field}"`);
    }
  }

  for (const [field, allowed] of Object.entries(enums)) {
    if (field in data && !allowed.includes(data[field])) {
      errors.push(`${rel}: invalid ${field} "${data[field]}"; expected one of ${allowed.join(', ')}`);
    }
  }

  if (typeof data.id === 'string') {
    if (!idPattern.test(data.id)) {
      errors.push(`${rel}: id must be lowercase kebab-case using letters, numbers, and hyphens`);
    }

    if (seenIds.has(data.id)) {
      errors.push(`${rel}: duplicate id "${data.id}" also used in ${seenIds.get(data.id)}`);
    } else {
      seenIds.set(data.id, rel);
    }
  }

  for (const arrayField of ['common_failures', 'tags']) {
    if (arrayField in data && (!Array.isArray(data[arrayField]) || data[arrayField].length === 0)) {
      errors.push(`${rel}: ${arrayField} must be a non-empty array`);
    }
  }

  for (const arrayField of ['learning_objectives', 'constraints', 'examples', 'hints']) {
    if (!(arrayField in data) || !Array.isArray(data[arrayField]) || data[arrayField].length === 0) {
      errors.push(`${rel}: ${arrayField} must be a non-empty array for complete exercises`);
    }
  }

  if (!data.evidence_disclaimer || typeof data.evidence_disclaimer !== 'string') {
    errors.push(`${rel}: evidence_disclaimer is required`);
  }

  if (!data.verdict_definitions || typeof data.verdict_definitions !== 'object') {
    errors.push(`${rel}: verdict_definitions is required`);
  }

  if (typeof data.estimated_time === 'string' && !estimatedTimePattern.test(data.estimated_time)) {
    errors.push(`${rel}: estimated_time must look like 15m, 30m, 1h, or 1h30m`);
  }

  for (const uniqueArrayField of ['tags', 'source_claim_ids']) {
    if (Array.isArray(data[uniqueArrayField]) && hasDuplicates(data[uniqueArrayField])) {
      errors.push(`${rel}: ${uniqueArrayField} must not contain duplicates`);
    }
  }

  if (data.track === 'EXAM' && ['OBSERVED', 'STRONG_PATTERN'].includes(data.source_label)) {
    if (!Array.isArray(data.source_claim_ids) || data.source_claim_ids.length === 0) {
      errors.push(`${rel}: observed Exam Track exercises must include source_claim_ids`);
    }

    if (!Array.isArray(data.source_files) || data.source_files.length === 0) {
      errors.push(`${rel}: observed Exam Track exercises must include source_files`);
    }
  }

  if (!Array.isArray(data.source_claim_ids) || data.source_claim_ids.length === 0) {
    errors.push(`${rel}: source_claim_ids is required for traceability`);
  }

  if (!Array.isArray(data.source_files) || data.source_files.length === 0) {
    errors.push(`${rel}: source_files is required for traceability`);
  }

  if (data.evaluation_mode === 'JAVA_CODE') {
    if (!data.judge || data.judge.runner_required !== true) {
      errors.push(`${rel}: JAVA_CODE exercises must require runner config`);
    }
    const expectedFileName = `${data.id}.java`;
    if ('requiredFileName' in data && data.requiredFileName !== expectedFileName) {
      errors.push(`${rel}: requiredFileName must be ${expectedFileName}`);
    }
    if ('submissionInstructions' in data && typeof data.submissionInstructions !== 'string') {
      errors.push(`${rel}: submissionInstructions must be a string`);
    }
  }

  if (data.evaluation_mode === 'NETWORK_CHALLENGE') {
    if (!data.timeout || typeof data.timeout.session_ttl_seconds !== 'number') {
      errors.push(`${rel}: NETWORK_CHALLENGE exercises must define timeout.session_ttl_seconds`);
    }

    if (!data.starter_asset_path || typeof data.starter_asset_path !== 'string') {
      errors.push(`${rel}: NETWORK_CHALLENGE exercises must define starter_asset_path`);
    }
  }

  if (Array.isArray(data.source_files)) {
    for (const sourceFile of data.source_files) {
      if (!isRelativeRepoPath(sourceFile)) {
        errors.push(`${rel}: source_files entry "${sourceFile}" must be a relative repo path`);
      } else if (!await fileExists(sourceFile)) {
        errors.push(`${rel}: source_files entry "${sourceFile}" does not exist`);
      }
    }
  }

  const serialized = JSON.stringify(data);
  if (realIpPattern.test(serialized)) {
    errors.push(`${rel}: do not hard-code old real exam IPs; use <HOST> and <PORT>`);
  }

  if (unfinishedTextPattern.test(serialized)) {
    errors.push(`${rel}: unfinished placeholder text such as TODO/FIXME/lorem is not allowed`);
  }

  return errors;
}

const files = await listJsonFiles(exercisesDir);
const seenIds = new Map();
const errors = [];

if (files.length !== 10) {
  errors.push(`content/exercises: expected exactly 10 exercise file(s), found ${files.length}`);
}

for (const file of files) {
  let data;
  try {
    data = JSON.parse(await readFile(file, 'utf8'));
  } catch (error) {
    errors.push(`${path.relative(root, file)}: invalid JSON: ${error.message}`);
    continue;
  }

  errors.push(...await validateExercise(file, data, seenIds));
}

if (errors.length > 0) {
  console.error(`Content validation failed with ${errors.length} error(s):`);
  for (const error of errors) {
    console.error(`- ${error}`);
  }
  process.exit(1);
}

console.log(`Content validation passed for ${files.length} exercise file(s).`);
