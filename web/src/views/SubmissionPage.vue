<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import { ApiError } from '../api/http';
import { fetchExercise, fetchSubmission, submitJavaCode } from '../api/exercises';
import { getParticipantId } from '../api/participant';
import { failureLabel, verdictLabel } from '../utils/display';

const props = defineProps({
  id: {
    type: String,
    required: true
  }
});

const router = useRouter();
const participantId = getParticipantId();
const submission = ref(null);
const exercise = ref(null);
const loading = ref(true);
const error = ref('');
const editing = ref(false);
const editSource = ref('');
const editFileName = ref('');
const resubmitting = ref(false);
let timer = null;

const draftKey = computed(() => submission.value ? `int1433.draft.${submission.value.exerciseId}.${submission.value.id}` : '');
const draftFileNameKey = computed(() => submission.value ? `int1433.draft-file.${submission.value.exerciseId}.${submission.value.id}` : '');
const sourceLines = computed(() => (submission.value?.sourceCode || '').split('\n'));
const displayFileName = computed(() => submission.value?.originalFileName || `${submission.value?.entryClassName || 'Main'}.java`);
const displayEntryClassName = computed(() => submission.value?.entryClassName || 'Main');
const editValidation = computed(() => validateEditor(editFileName.value, editSource.value));
const editSourceBytes = computed(() => new Blob([editSource.value]).size);
const canResubmit = computed(() => !resubmitting.value && editSourceBytes.value <= 20 * 1024 && editValidation.value.ok);

async function load() {
  try {
    const payload = await fetchSubmission(props.id, participantId);
    submission.value = payload;
    if (!exercise.value || exercise.value.id !== payload.exerciseId) {
      exercise.value = await fetchExercise(payload.exerciseId);
    }
    error.value = '';
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải submission';
  } finally {
    loading.value = false;
  }
}

function startEditing() {
  const draft = draftKey.value ? localStorage.getItem(draftKey.value) : null;
  const filenameDraft = draftFileNameKey.value ? localStorage.getItem(draftFileNameKey.value) : null;
  editSource.value = draft || submission.value?.sourceCode || '';
  editFileName.value = filenameDraft || displayFileName.value;
  editing.value = true;
}

function cancelEditing() {
  editing.value = false;
  editSource.value = '';
  editFileName.value = '';
}

function discardDraft() {
  if (draftKey.value) localStorage.removeItem(draftKey.value);
  if (draftFileNameKey.value) localStorage.removeItem(draftFileNameKey.value);
  editSource.value = submission.value?.sourceCode || '';
  editFileName.value = displayFileName.value;
}

async function copySource() {
  await navigator.clipboard.writeText(submission.value?.sourceCode || '');
}

async function resubmit() {
  if (!canResubmit.value) return;
  resubmitting.value = true;
  error.value = '';
  try {
    const next = await submitJavaCode(submission.value.exerciseId, participantId, editSource.value, editFileName.value);
    if (draftKey.value) localStorage.removeItem(draftKey.value);
    if (draftFileNameKey.value) localStorage.removeItem(draftFileNameKey.value);
    router.push({ name: 'submission-detail', params: { id: next.id } });
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Không nộp lại được mã Java';
  } finally {
    resubmitting.value = false;
  }
}

watch(editSource, (value) => {
  if (editing.value && draftKey.value) {
    localStorage.setItem(draftKey.value, value);
  }
});

watch(editFileName, (value) => {
  if (editing.value && draftFileNameKey.value) {
    localStorage.setItem(draftFileNameKey.value, value);
  }
});

function isJavaIdentifier(value) {
  return /^[$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}]*$/u.test(value) && !new Set([
    'abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch', 'char', 'class', 'const',
    'continue', 'default', 'do', 'double', 'else', 'enum', 'extends', 'final', 'finally', 'float',
    'for', 'goto', 'if', 'implements', 'import', 'instanceof', 'int', 'interface', 'long', 'native',
    'new', 'package', 'private', 'protected', 'public', 'return', 'short', 'static', 'strictfp',
    'super', 'switch', 'synchronized', 'this', 'throw', 'throws', 'transient', 'try', 'void',
    'volatile', 'while', 'true', 'false', 'null', '_'
  ]).has(value);
}

function stripCommentsAndStrings(source) {
  return source
    .replace(/\/\*[\s\S]*?\*\//g, (match) => match.replace(/[^\n]/g, ' '))
    .replace(/\/\/[^\n]*/g, (match) => ' '.repeat(match.length))
    .replace(/"(?:\\.|[^"\\])*"/g, (match) => ' '.repeat(match.length))
    .replace(/'(?:\\.|[^'\\])*'/g, (match) => ' '.repeat(match.length));
}

function topLevelPublicClasses(source) {
  const masked = stripCommentsAndStrings(source);
  const classes = [];
  let depth = 0;
  for (let i = 0; i < masked.length; i += 1) {
    const ch = masked[i];
    if (ch === '{') depth += 1;
    else if (ch === '}') depth = Math.max(0, depth - 1);
    else if (depth === 0) {
      const declaration = readClassDeclaration(masked.slice(i));
      if (declaration?.isPublic && declaration.className) {
        classes.push(declaration.className);
        i += declaration.length;
      }
    }
  }
  return classes;
}

function readClassDeclaration(text) {
  let rest = text.trimStart();
  let consumed = text.length - rest.length;
  let isPublic = false;
  while (rest.startsWith('@')) {
    const annotation = rest.match(/^@[$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}.]*(?:\s*\([^)]*\))?/u);
    if (!annotation) return null;
    consumed += annotation[0].length;
    rest = rest.slice(annotation[0].length).trimStart();
    consumed = text.length - rest.length;
  }
  const modifiers = new Set(['public', 'abstract', 'final', 'strictfp', 'sealed', 'non-sealed']);
  while (true) {
    const token = rest.match(/^(non-sealed|[$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}]*)\b/u)?.[1];
    if (!token) return null;
    if (token === 'class') {
      const afterClass = rest.slice(token.length);
      const classMatch = afterClass.match(/^\s*([$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}]*)\b/u);
      return classMatch ? { isPublic, className: classMatch[1], length: consumed + token.length + classMatch[0].length } : null;
    }
    if (!modifiers.has(token)) return null;
    isPublic = isPublic || token === 'public';
    rest = rest.slice(token.length).trimStart();
    consumed = text.length - rest.length;
  }
}

function validateEditor(fileName, source) {
  if (!fileName.endsWith('.java') || fileName.includes('/') || fileName.includes('\\') || fileName.includes('..') || !isJavaIdentifier(fileName.slice(0, -5))) {
    return { ok: false, message: 'Tên file không phải là tên lớp Java hợp lệ.' };
  }
  const basename = fileName.slice(0, -5);
  const masked = stripCommentsAndStrings(source);
  if (/^\s*package\s+[A-Za-z_$][A-Za-z0-9_$.]*\s*;/m.test(masked)) {
    return { ok: false, message: 'Không được khai báo package trong bài một file.' };
  }
  const classes = topLevelPublicClasses(source);
  if (classes.length === 0) return { ok: false, message: 'Không tìm thấy top-level public class.' };
  if (classes.length > 1) return { ok: false, message: 'Chỉ được khai báo một top-level public class.' };
  if (classes[0] !== basename) return { ok: false, message: `Tên public class ${classes[0]} không trùng với tên file ${fileName}.` };
  if (!/\bpublic\s+static\s+void\s+main\s*\(\s*String\s*\[\s*\]\s+[$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}]*\s*\)(?:\s+throws\b[^{;]*)?\s*\{/u.test(masked)) {
    return { ok: false, message: 'Không tìm thấy public static void main(String[] args).' };
  }
  return { ok: true, message: `Entry class: ${classes[0]}` };
}

onMounted(() => {
  load();
  timer = window.setInterval(load, 2000);
});

onUnmounted(() => {
  if (timer) window.clearInterval(timer);
});
</script>

<template>
  <main class="shell">
    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="error && !submission" class="panel state state-error">{{ error }}</section>

    <article v-else class="panel result">
      <RouterLink class="back" :to="{ name: 'exercise-practice', params: { id: submission.exerciseId } }">
        Quay lại luyện tập
      </RouterLink>

      <header>
        <p class="mono">{{ submission.id }}</p>
        <h1>{{ verdictLabel(submission.verdict) }}</h1>
        <p>{{ submission.publicMessage }}</p>
        <div class="metadata-row">
          <span class="pill">{{ submission.exerciseId }}</span>
          <span class="pill">{{ submission.evaluationMode }}</span>
          <span class="pill">{{ submission.status }}</span>
          <span class="pill">Điểm {{ submission.score }}</span>
        </div>
      </header>

      <section v-if="error" class="state state-error">{{ error }}</section>

      <section v-if="exercise" class="statement-block">
        <h2>Đề bài</h2>
        <p>{{ exercise.statement }}</p>
        <h3>Yêu cầu xử lý</h3>
        <p>{{ exercise.processingRequirement }}</p>
        <div v-if="exercise.examples?.length" class="example-grid">
          <article v-for="(example, index) in exercise.examples" :key="index" class="example-card">
            <h4>{{ example.title || `Ví dụ ${index + 1}` }}</h4>
            <p class="example-label">Input</p>
            <pre>{{ example.input }}</pre>
            <p class="example-label">Output</p>
            <pre>{{ example.output }}</pre>
            <p v-if="example.explanation" class="muted">{{ example.explanation }}</p>
          </article>
        </div>
      </section>

      <section>
        <h2>Kết quả</h2>
        <dl class="result-meta">
          <dt>File</dt>
          <dd>{{ displayFileName }}</dd>
          <dt>Entry class</dt>
          <dd>{{ displayEntryClassName }}</dd>
          <dt>SHA-256</dt>
          <dd><code>{{ submission.sourceSha256 || 'chưa có' }}</code></dd>
          <dt>Chấm lúc</dt>
          <dd>{{ submission.judgedAt || submission.createdAt }}</dd>
        </dl>
      </section>

      <section v-if="submission.compileOutput">
        <h2>Compile output</h2>
        <pre>{{ submission.compileOutput }}</pre>
      </section>

      <section v-if="submission.runtimeOutput">
        <h2>Runtime output</h2>
        <pre>{{ submission.runtimeOutput }}</pre>
      </section>

      <section>
        <h2>Hidden test summary</h2>
        <div class="test-grid">
          <div v-for="test in submission.testResults" :key="test.testIndex" class="test-row">
            <strong>#{{ test.testIndex }}</strong>
            <span>{{ verdictLabel(test.verdict) }}</span>
            <span>{{ test.executionTimeMs }} ms</span>
            <span>{{ test.publicMessage }}</span>
          </div>
        </div>
      </section>

      <section v-if="exercise?.commonFailures?.length">
        <h2>Lỗi thường gặp cần rà lại</h2>
        <ul>
          <li v-for="failure in exercise.commonFailures" :key="failure">{{ failureLabel(failure) }}</li>
        </ul>
      </section>

      <section>
        <div class="source-heading">
          <div>
            <h2>Mã nguồn đã nộp</h2>
            <p class="muted">{{ displayFileName }} · {{ displayEntryClassName }}</p>
          </div>
          <div class="source-actions">
            <button type="button" class="secondary" @click="copySource">Copy</button>
            <RouterLink class="secondary link-button" :to="{ name: 'exercise-practice', params: { id: submission.exerciseId } }">
              Nộp file khác
            </RouterLink>
            <button v-if="!editing" type="button" @click="startEditing">Chỉnh sửa và nộp lại</button>
          </div>
        </div>

        <div v-if="!editing" class="code-viewer" aria-label="Mã nguồn đã nộp">
          <div v-for="(line, index) in sourceLines" :key="index" class="code-line">
            <span class="line-number">{{ index + 1 }}</span>
            <code>{{ line || ' ' }}</code>
          </div>
        </div>

        <div v-else class="edit-panel">
          <label class="filename-field">
            <span>Tên file</span>
            <input v-model.trim="editFileName" type="text" spellcheck="false" />
          </label>
          <p class="muted">{{ editValidation.ok ? editValidation.message : 'Tên file Java phải trùng với tên public class.' }}</p>
          <p v-if="!editValidation.ok" class="state state-error">{{ editValidation.message }}</p>
          <textarea v-model="editSource" class="source-editor" spellcheck="false" />
          <div class="actions">
            <button type="button" :disabled="!canResubmit" @click="resubmit">
              {{ resubmitting ? 'Đang chấm...' : 'Nộp lại' }}
            </button>
            <button type="button" class="secondary" :disabled="resubmitting" @click="cancelEditing">Hủy chỉnh sửa</button>
            <button type="button" class="secondary" :disabled="resubmitting" @click="discardDraft">Bỏ draft</button>
            <span class="muted">{{ editSourceBytes }} / 20480 bytes</span>
          </div>
        </div>
      </section>
    </article>
  </main>
</template>

<style scoped>
.result {
  padding: 22px;
}

.back,
.link-button {
  display: inline-flex;
  align-items: center;
  min-height: 38px;
  padding: 0 14px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  color: #203040;
  font-weight: 750;
  text-decoration: none;
}

.back {
  margin-bottom: 18px;
}

h1,
h2,
h3,
h4,
p {
  margin: 0;
}

section {
  margin-top: 22px;
}

.statement-block h3 {
  margin-top: 14px;
}

.statement-block p,
li,
dd {
  color: #405166;
}

pre {
  overflow: auto;
  max-height: 320px;
  padding: 12px;
  border-radius: 6px;
  background: #eef2f6;
  white-space: pre-wrap;
}

.example-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 14px;
}

.example-card {
  padding: 14px;
  border: 1px solid #d9e0e8;
  border-radius: 8px;
}

.example-label {
  margin-top: 12px;
  color: #667485;
  font-weight: 800;
}

.result-meta {
  display: grid;
  grid-template-columns: 130px minmax(0, 1fr);
  gap: 8px 12px;
}

dt {
  color: #667485;
  font-weight: 800;
}

dd {
  min-width: 0;
  margin: 0;
  word-break: break-word;
}

.test-grid {
  display: grid;
  gap: 8px;
}

.test-row {
  display: grid;
  grid-template-columns: 60px 140px 90px minmax(0, 1fr);
  gap: 10px;
  padding: 10px;
  border: 1px solid #d9e0e8;
  border-radius: 6px;
}

.source-heading {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 12px;
}

.source-actions,
.actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.code-viewer {
  overflow: auto;
  max-height: 520px;
  border: 1px solid #233244;
  border-radius: 8px;
  background: #0f1720;
}

.code-line {
  display: grid;
  grid-template-columns: 56px minmax(0, 1fr);
  min-height: 24px;
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", monospace;
  font-size: 0.9rem;
  line-height: 1.55;
}

.line-number {
  padding: 0 10px;
  color: #8aa0b7;
  text-align: right;
  user-select: none;
  background: #162231;
}

.code-line code {
  display: block;
  padding: 0 12px;
  color: #e8eef5;
  white-space: pre;
  background: transparent;
}

.source-editor {
  width: 100%;
  min-height: 420px;
  resize: vertical;
  padding: 14px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  background: #0f1720;
  color: #e8eef5;
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", monospace;
  font-size: 0.92rem;
  line-height: 1.55;
}

.filename-field {
  display: grid;
  gap: 6px;
  max-width: 360px;
  margin-bottom: 10px;
  color: #405166;
  font-weight: 800;
}

.filename-field input {
  min-height: 38px;
  padding: 0 10px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  font: inherit;
}

button {
  min-height: 38px;
  padding: 0 14px;
  border: 0;
  border-radius: 6px;
  background: #1f5f8b;
  color: #ffffff;
  font-weight: 800;
}

.secondary {
  border: 1px solid #cfd8e3;
  background: #ffffff;
  color: #203040;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}

@media (max-width: 680px) {
  .source-heading,
  .actions {
    align-items: stretch;
    flex-direction: column;
  }

  .test-row,
  .result-meta,
  .example-grid {
    grid-template-columns: 1fr;
  }
}
</style>
