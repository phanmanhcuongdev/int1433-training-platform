import { readdir, readFile } from 'node:fs/promises';
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
  'common_failures',
  'tags',
  'estimated_time'
];

const enums = {
  source_label: ['OBSERVED', 'STRONG_PATTERN', 'EXTENDED', 'CHALLENGE'],
  track: ['EXAM', 'EXTENDED_NETWORKING', 'BACKEND_DISTRIBUTED'],
  difficulty: ['EASY', 'MEDIUM', 'HARD'],
  level: ['L0', 'L1', 'L2', 'L3', 'L4', 'L5', 'L6']
};

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

function validateExercise(file, data, seenIds) {
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

  if (data.track === 'EXAM' && ['OBSERVED', 'STRONG_PATTERN'].includes(data.source_label)) {
    if (!Array.isArray(data.source_claim_ids) || data.source_claim_ids.length === 0) {
      errors.push(`${rel}: observed Exam Track exercises must include source_claim_ids`);
    }
  }

  const serialized = JSON.stringify(data);
  if (/203\.162\.10\.109|172\.188\.19\.218/.test(serialized)) {
    errors.push(`${rel}: do not hard-code old real exam IPs; use <HOST> and <PORT>`);
  }

  return errors;
}

const files = await listJsonFiles(exercisesDir);
const seenIds = new Map();
const errors = [];

for (const file of files) {
  let data;
  try {
    data = JSON.parse(await readFile(file, 'utf8'));
  } catch (error) {
    errors.push(`${path.relative(root, file)}: invalid JSON: ${error.message}`);
    continue;
  }

  errors.push(...validateExercise(file, data, seenIds));
}

if (errors.length > 0) {
  console.error(`Content validation failed with ${errors.length} error(s):`);
  for (const error of errors) {
    console.error(`- ${error}`);
  }
  process.exit(1);
}

console.log(`Content validation passed for ${files.length} exercise file(s).`);

