import { mkdir, readdir, readFile, writeFile } from 'node:fs/promises';
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

function compareExercises(a, b) {
  const aOrder = Number.isFinite(a.display_order) ? a.display_order : null;
  const bOrder = Number.isFinite(b.display_order) ? b.display_order : null;

  if (aOrder !== null && bOrder !== null && aOrder !== bOrder) {
    return aOrder - bOrder;
  }

  if (aOrder !== null && bOrder === null) {
    return -1;
  }

  if (aOrder === null && bOrder !== null) {
    return 1;
  }

  return String(a.id).localeCompare(String(b.id));
}

const files = await listJsonFiles(exercisesDir);
const exercises = [];

for (const file of files) {
  const data = JSON.parse(await readFile(file, 'utf8'));
  exercises.push(data);
}

exercises.sort(compareExercises);

const output = {
  generated_at: new Date().toISOString(),
  count: exercises.length,
  exercises
};

await mkdir(path.dirname(outputFile), { recursive: true });
await writeFile(outputFile, `${JSON.stringify(output, null, 2)}\n`, 'utf8');

console.log(`Generated ${path.relative(root, outputFile)} with ${exercises.length} exercise(s).`);
