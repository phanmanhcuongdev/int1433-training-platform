import { mkdtemp, readdir, readFile, rm } from 'node:fs/promises';
import { spawnSync } from 'node:child_process';
import os from 'node:os';
import path from 'node:path';
import process from 'node:process';

const root = process.cwd();
const startersRoot = path.join(root, 'assets', 'starters');
const expected = [
  'tcp-byte-prime-sum-001',
  'tcp-data-gcd-lcm-001',
  'tcp-character-normalize-001',
  'tcp-object-product-001',
  'udp-string-request-id-001',
  'udp-object-product-001',
  'rmi-data-pythagorean-001',
  'ws-data-factorization-001'
];

const realIpPattern = /203\.162\.10\.109|172\.188\.19\.218/;
const credentialPattern = /(password|passwd|secret|credential)\s*[:=]\s*['"]?[^<\s]/i;
const vietnamesePattern = /[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]/i;

const entries = await readdir(startersRoot, { withFileTypes: true });
const directories = entries.filter((entry) => entry.isDirectory()).map((entry) => entry.name).sort();
const errors = [];

if (JSON.stringify(directories) !== JSON.stringify([...expected].sort())) {
  errors.push(`Expected starter directories ${expected.join(', ')}, found ${directories.join(', ')}`);
}

const tempRoot = await mkdtemp(path.join(os.tmpdir(), 'int1433-starters-'));
try {
  for (const id of expected) {
    const dir = path.join(startersRoot, id);
    const readme = await readFile(path.join(dir, 'README.md'), 'utf8');
    if (!vietnamesePattern.test(readme)) {
      errors.push(`${id}: README must use Vietnamese with diacritics`);
    }
    const files = await listFiles(dir);
    for (const file of files) {
      const rel = path.relative(dir, file).replaceAll('\\', '/');
      if (rel.startsWith('../') || rel.includes('/../')) {
        errors.push(`${id}: path traversal-like entry ${rel}`);
      }
      const text = await readFile(file, 'utf8');
      if (realIpPattern.test(text)) {
        errors.push(`${id}: contains old real PTIT IP`);
      }
      if (credentialPattern.test(text)) {
        errors.push(`${id}: contains credential-like literal`);
      }
    }

    const javaFiles = files.filter((file) => file.endsWith('.java')).sort();
    const outDir = path.join(tempRoot, id);
    const compile = spawnSync('javac', ['-encoding', 'UTF-8', '-d', outDir, ...javaFiles], {
      cwd: root,
      encoding: 'utf8'
    });
    if (compile.status !== 0) {
      errors.push(`${id}: javac failed\n${compile.stderr || compile.stdout}`);
    }
  }
} finally {
  await rm(tempRoot, { recursive: true, force: true });
}

if (errors.length > 0) {
  console.error(`Starter check failed with ${errors.length} error(s):`);
  errors.forEach((error) => console.error(`- ${error}`));
  process.exit(1);
}

console.log(`Starter check passed for ${expected.length} starter project(s).`);

async function listFiles(dir) {
  const result = [];
  const entries = await readdir(dir, { withFileTypes: true });
  for (const entry of entries) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      result.push(...await listFiles(fullPath));
    } else if (entry.isFile()) {
      result.push(fullPath);
    }
  }
  return result;
}
