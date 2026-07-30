#!/usr/bin/env node
import fs from 'node:fs';

const expected = process.argv[2]?.replace(/^v/, '');
if (!expected) {
  console.error('Usage: node scripts/verify-release-version.mjs <version-or-tag>');
  process.exit(2);
}

const rootPackage = JSON.parse(fs.readFileSync('package.json', 'utf8'));
const webPackage = JSON.parse(fs.readFileSync('web/package.json', 'utf8'));
const versionFile = fs.readFileSync('VERSION', 'utf8').trim();
const pom = fs.readFileSync('backend/pom.xml', 'utf8');
const pomMatch = pom.match(/<artifactId>training-platform<\/artifactId>\s*<version>([^<]+)<\/version>/);
const backendVersion = pomMatch?.[1];

const versions = {
  VERSION: versionFile,
  'package.json': rootPackage.version,
  'web/package.json': webPackage.version,
  'backend/pom.xml': backendVersion
};

let ok = true;
for (const [file, value] of Object.entries(versions)) {
  if (value !== expected) {
    console.error(`${file} has ${value}; expected ${expected}`);
    ok = false;
  }
}

if (!ok) {
  process.exit(1);
}
console.log(`Release version ${expected} is consistent.`);
