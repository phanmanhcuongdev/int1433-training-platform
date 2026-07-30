import { readdir, readFile } from 'node:fs/promises';
import path from 'node:path';
import process from 'node:process';

const root = process.cwd();
const exercisesDir = path.join(root, 'content', 'exercises');
const outputFile = path.join(root, 'web', 'public', 'generated', 'exercises.json');

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

function fail(message) {
  console.error(`Content index test failed: ${message}`);
  process.exit(1);
}

const files = await listJsonFiles(exercisesDir);
const raw = await readFile(outputFile, 'utf8');
const index = JSON.parse(raw);

if (!Array.isArray(index.exercises)) {
  fail('exercises must be an array');
}

if (index.count !== files.length || index.exercises.length !== files.length) {
  fail(`expected ${files.length} generated exercises, got count=${index.count}, length=${index.exercises.length}`);
}

const ids = index.exercises.map((exercise) => exercise.id);
if (new Set(ids).size !== ids.length) {
  fail('duplicate IDs found in generated output');
}

const sortedIds = [...index.exercises]
  .sort((a, b) => {
    const aOrder = Number.isFinite(a.display_order) ? a.display_order : null;
    const bOrder = Number.isFinite(b.display_order) ? b.display_order : null;
    if (aOrder !== null && bOrder !== null && aOrder !== bOrder) return aOrder - bOrder;
    if (aOrder !== null && bOrder === null) return -1;
    if (aOrder === null && bOrder !== null) return 1;
    return String(a.id).localeCompare(String(b.id));
  })
  .map((exercise) => exercise.id);

if (ids.join('\n') !== sortedIds.join('\n')) {
  fail('generated exercises are not sorted by display_order/id');
}

if (raw.includes(root)) {
  fail('generated output contains an absolute workspace path');
}

if (/203\.162\.10\.109|172\.188\.19\.218/.test(raw)) {
  fail('generated output contains old real exam IPs');
}

console.log(`Content index test passed for ${index.count} exercise(s).`);
