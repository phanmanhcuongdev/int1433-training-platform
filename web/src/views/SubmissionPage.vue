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
const resubmitting = ref(false);
let timer = null;

const draftKey = computed(() => submission.value ? `int1433.draft.${submission.value.exerciseId}.${submission.value.id}` : '');
const sourceLines = computed(() => (submission.value?.sourceCode || '').split('\n'));
const requiredFileName = computed(() => submission.value ? `${submission.value.exerciseId}.java` : '');

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
  editSource.value = draft || submission.value?.sourceCode || '';
  editing.value = true;
}

function cancelEditing() {
  editing.value = false;
  editSource.value = '';
}

function discardDraft() {
  if (draftKey.value) localStorage.removeItem(draftKey.value);
  editSource.value = submission.value?.sourceCode || '';
}

async function copySource() {
  await navigator.clipboard.writeText(submission.value?.sourceCode || '');
}

async function resubmit() {
  resubmitting.value = true;
  error.value = '';
  try {
    const next = await submitJavaCode(submission.value.exerciseId, participantId, editSource.value);
    if (draftKey.value) localStorage.removeItem(draftKey.value);
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
          <dd>{{ submission.originalFileName || requiredFileName }}</dd>
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
            <p class="muted">{{ submission.originalFileName || requiredFileName }}</p>
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
          <textarea v-model="editSource" class="source-editor" spellcheck="false" />
          <div class="actions">
            <button type="button" :disabled="resubmitting || editSource.length > 20000" @click="resubmit">
              {{ resubmitting ? 'Đang chấm...' : 'Nộp lại' }}
            </button>
            <button type="button" class="secondary" :disabled="resubmitting" @click="cancelEditing">Hủy chỉnh sửa</button>
            <button type="button" class="secondary" :disabled="resubmitting" @click="discardDraft">Bỏ draft</button>
            <span class="muted">{{ editSource.length }} / 20000 ký tự</span>
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
