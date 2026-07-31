<script setup>
import { computed, onMounted, ref } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import { ApiError } from '../api/http';
import { fetchExercise, fetchExerciseEvaluation, startChallengeSession, submitJavaFile } from '../api/exercises';
import { getParticipantId } from '../api/participant';
import { failureLabel, minutesLabel } from '../utils/display';

const props = defineProps({
  id: {
    type: String,
    required: true
  }
});

const router = useRouter();
const participantId = getParticipantId();
const exercise = ref(null);
const evaluation = ref(null);
const loading = ref(true);
const submitting = ref(false);
const error = ref('');
const selectedFile = ref(null);
const selectedClassName = ref('');
const fileError = ref('');
const uploadZoneActive = ref(false);

const isJavaCode = computed(() => evaluation.value?.evaluationMode === 'JAVA_CODE');
const isNetwork = computed(() => evaluation.value?.evaluationMode === 'NETWORK_CHALLENGE');
const canSubmitFile = computed(() => selectedFile.value && !fileError.value && !submitting.value);

async function load() {
  loading.value = true;
  error.value = '';
  try {
    const [exercisePayload, evaluationPayload] = await Promise.all([
      fetchExercise(props.id),
      fetchExerciseEvaluation(props.id)
    ]);
    exercise.value = exercisePayload;
    evaluation.value = evaluationPayload;
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải dữ liệu';
  } finally {
    loading.value = false;
  }
}

async function submitFile() {
  if (!canSubmitFile.value) return;
  submitting.value = true;
  error.value = '';
  try {
    const submission = await submitJavaFile(props.id, participantId, selectedFile.value);
    router.push({ name: 'submission-detail', params: { id: submission.id } });
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Không nộp được file Java';
  } finally {
    submitting.value = false;
  }
}

async function startSession() {
  submitting.value = true;
  error.value = '';
  try {
    const session = await startChallengeSession(props.id, participantId);
    sessionStorage.setItem(`int1433.challenge.${session.sessionId}.token`, session.token);
    router.push({ name: 'challenge-session-detail', params: { id: session.sessionId } });
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Không tạo được phiên kết nối';
  } finally {
    submitting.value = false;
  }
}

async function handleFileInput(event) {
  await validateAndSetFile(event.target.files);
  event.target.value = '';
}

async function handleDrop(event) {
  uploadZoneActive.value = false;
  await validateAndSetFile(event.dataTransfer.files);
}

async function validateAndSetFile(files) {
  selectedFile.value = null;
  selectedClassName.value = '';
  fileError.value = '';

  if (!files || files.length !== 1) {
    fileError.value = 'Chỉ chấp nhận một file .java.';
    return;
  }

  const file = files[0];
  const fileName = file.name || '';
  if (!fileName.endsWith('.java')) {
    fileError.value = 'Chỉ chấp nhận một file .java.';
    return;
  }
  if (fileName.includes('/') || fileName.includes('\\') || fileName.includes('..') || !isJavaIdentifier(fileName.slice(0, -5))) {
    fileError.value = 'Tên file không phải là tên lớp Java hợp lệ.';
    return;
  }
  if (file.size === 0) {
    fileError.value = 'File không được rỗng.';
    return;
  }
  if (file.size > 20 * 1024) {
    fileError.value = 'File vượt quá giới hạn 20 KB.';
    return;
  }

  let source = '';
  try {
    source = new TextDecoder('utf-8', { fatal: true }).decode(await file.arrayBuffer());
  } catch {
    fileError.value = 'File không phải UTF-8 hợp lệ.';
    return;
  }

  const validation = validateJavaSource(fileName, source);
  if (!validation.ok) {
    fileError.value = validation.message;
    return;
  }

  selectedFile.value = file;
  selectedClassName.value = validation.entryClassName;
}

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
    if (ch === '{') {
      depth += 1;
    } else if (ch === '}') {
      depth = Math.max(0, depth - 1);
    } else if (depth === 0) {
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

function validateJavaSource(fileName, source) {
  const basename = fileName.slice(0, -5);
  const masked = stripCommentsAndStrings(source);
  if (/^\s*package\s+[A-Za-z_$][A-Za-z0-9_$.]*\s*;/m.test(masked)) {
    return { ok: false, message: 'Không được khai báo package trong bài một file.' };
  }
  const classes = topLevelPublicClasses(source);
  if (classes.length === 0) {
    return { ok: false, message: 'Không tìm thấy top-level public class.' };
  }
  if (classes.length > 1) {
    return { ok: false, message: 'Chỉ được khai báo một top-level public class.' };
  }
  if (classes[0] !== basename) {
    return { ok: false, message: `Tên public class ${classes[0]} không trùng với tên file ${fileName}.` };
  }
  if (!/\bpublic\s+static\s+void\s+main\s*\(\s*String\s*\[\s*\]\s+[$_\p{L}][$_\p{L}\p{N}\p{Mn}\p{Mc}\p{Pc}]*\s*\)(?:\s+throws\b[^{;]*)?\s*\{/u.test(masked)) {
    return { ok: false, message: 'Không tìm thấy public static void main(String[] args).' };
  }
  return { ok: true, entryClassName: classes[0] };
}

onMounted(load);
</script>

<template>
  <main class="shell">
    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="error && !exercise" class="panel state state-error">{{ error }}</section>

    <article v-else class="panel practice">
      <RouterLink class="back" :to="{ name: 'exercise-detail', params: { id } }">Quay lại tổng quan</RouterLink>
      <header>
        <p class="mono">{{ exercise.id }}</p>
        <h1>{{ exercise.title }}</h1>
        <p>{{ exercise.summary }}</p>
        <div class="metadata-row">
          <span class="pill">{{ evaluation.evaluationMode }}</span>
          <span class="pill">{{ exercise.technology }}</span>
          <span class="pill">{{ exercise.level }}</span>
          <span class="pill">{{ minutesLabel(exercise.estimatedTimeMinutes) }}</span>
        </div>
      </header>

      <section v-if="error" class="state state-error">{{ error }}</section>

      <section class="practice-section statement-block">
        <h2>Đề bài</h2>
        <p>{{ exercise.statement }}</p>
      </section>

      <section v-if="exercise.learningObjectives?.length" class="practice-section">
        <h2>Mục tiêu học tập</h2>
        <ul>
          <li v-for="item in exercise.learningObjectives" :key="item">{{ item }}</li>
        </ul>
      </section>

      <section class="practice-section">
        <h2>Yêu cầu xử lý</h2>
        <p>{{ exercise.processingRequirement }}</p>
      </section>

      <section v-if="exercise.requestFormat || exercise.submissionFormat" class="practice-section io-grid">
        <div v-if="exercise.requestFormat">
          <h2>Input</h2>
          <pre>{{ exercise.requestFormat }}</pre>
        </div>
        <div v-if="exercise.submissionFormat">
          <h2>Output / Submission</h2>
          <pre>{{ exercise.submissionFormat }}</pre>
        </div>
      </section>

      <section v-if="exercise.examples?.length" class="practice-section">
        <h2>Ví dụ</h2>
        <div class="example-grid">
          <article v-for="(example, index) in exercise.examples" :key="index" class="example-card">
            <h3>{{ example.title || `Ví dụ ${index + 1}` }}</h3>
            <p class="example-label">Input</p>
            <pre>{{ example.input }}</pre>
            <p class="example-label">Output</p>
            <pre>{{ example.output }}</pre>
            <p v-if="example.explanation" class="muted">{{ example.explanation }}</p>
          </article>
        </div>
      </section>

      <section v-if="exercise.hints?.length" class="practice-section">
        <h2>Gợi ý</h2>
        <ul>
          <li v-for="hint in exercise.hints" :key="hint">{{ hint }}</li>
        </ul>
      </section>

      <section v-if="exercise.commonFailures?.length" class="practice-section">
        <h2>Lỗi thường gặp</h2>
        <ul>
          <li v-for="failure in exercise.commonFailures" :key="failure">{{ failureLabel(failure) }}</li>
        </ul>
      </section>

      <section v-if="isJavaCode" class="practice-section">
        <div class="section-heading">
          <div>
            <h2>Nộp file Java</h2>
            <p>Viết và chạy thử local bằng IDE của bạn, sau đó upload đúng một file Java. Runner chỉ nhận JDK 21, không dependency ngoài.</p>
          </div>
        </div>
        <label
          class="upload-zone"
          :class="{ active: uploadZoneActive, invalid: fileError }"
          @dragenter.prevent="uploadZoneActive = true"
          @dragover.prevent="uploadZoneActive = true"
          @dragleave.prevent="uploadZoneActive = false"
          @drop.prevent="handleDrop"
        >
          <input type="file" accept=".java,text/x-java-source,text/plain" @change="handleFileInput" />
          <strong>Kéo thả file Java vào đây</strong>
          <span>hoặc bấm để chọn file</span>
          <small>Tên file Java phải trùng với tên <code>public class</code>, UTF-8, tối đa 20 KB, không khai báo package.</small>
        </label>
        <p v-if="fileError" class="state state-error">{{ fileError }}</p>
        <p v-if="selectedFile" class="selected-file">
          Đã chọn: <strong>{{ selectedFile.name }}</strong>
          <span>Class: <strong>{{ selectedClassName }}</strong></span>
          <span>{{ Math.ceil(selectedFile.size / 1024) }} KB</span>
        </p>
        <div class="actions">
          <button type="button" :disabled="!canSubmitFile" @click="submitFile">
            {{ submitting ? 'Đang chấm...' : 'Nộp bài' }}
          </button>
          <span class="muted">Mỗi lần nộp tạo một submission mới.</span>
        </div>
      </section>

      <section v-else-if="isNetwork" class="practice-section">
        <div class="section-heading">
          <div>
            <h2>Bắt đầu phiên kết nối</h2>
            <p>Platform sẽ tạo token/qCode/payload riêng. Client Java của bạn kết nối tới server challenge và verdict được lưu tự động.</p>
          </div>
        </div>
        <ul>
          <li v-for="line in evaluation.instructions" :key="line">{{ line }}</li>
        </ul>
        <button type="button" :disabled="submitting" @click="startSession">
          {{ submitting ? 'Đang tạo phiên...' : 'Bắt đầu phiên kết nối' }}
        </button>
      </section>

      <section v-else class="state state-error">
        Evaluation mode này chưa có UI practice.
      </section>
    </article>
  </main>
</template>

<style scoped>
.practice {
  padding: 22px;
}

.back {
  display: inline-flex;
  min-height: 38px;
  align-items: center;
  margin-bottom: 18px;
  padding: 0 14px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  color: #203040;
  font-weight: 750;
  text-decoration: none;
}

h1,
h2,
h3,
p {
  margin: 0;
}

header p {
  margin-top: 6px;
  color: #526171;
}

.practice-section {
  margin-top: 24px;
}

.statement-block p,
li {
  color: #405166;
}

.io-grid,
.example-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
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

pre {
  overflow: auto;
  padding: 12px;
  border-radius: 6px;
  background: #eef2f6;
  white-space: pre-wrap;
}

.upload-zone {
  position: relative;
  display: grid;
  gap: 8px;
  place-items: center;
  min-height: 190px;
  padding: 22px;
  border: 2px dashed #9fb0c3;
  border-radius: 8px;
  background: #f8fafc;
  text-align: center;
}

.upload-zone.active {
  border-color: #1f5f8b;
  background: #eef7ff;
}

.upload-zone.invalid {
  border-color: #d79a9a;
}

.upload-zone input {
  position: absolute;
  inset: 0;
  cursor: pointer;
  opacity: 0;
}

.upload-zone small {
  color: #667485;
}

.selected-file {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 12px 0 0;
  color: #405166;
}

.actions {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

button {
  min-height: 40px;
  padding: 0 16px;
  border: 0;
  border-radius: 6px;
  background: #1f5f8b;
  color: #ffffff;
  font-weight: 800;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}

@media (max-width: 680px) {
  .actions {
    align-items: stretch;
    flex-direction: column;
  }

  .io-grid,
  .example-grid {
    grid-template-columns: 1fr;
  }
}
</style>
