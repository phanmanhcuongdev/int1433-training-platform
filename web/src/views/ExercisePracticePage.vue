<script setup>
import { computed, onMounted, ref } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import { ApiError } from '../api/http';
import { fetchExercise, fetchExerciseEvaluation, startChallengeSession, submitJavaCode } from '../api/exercises';
import { getParticipantId } from '../api/participant';
import { minutesLabel } from '../utils/display';

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
const sourceCode = ref('');
const loading = ref(true);
const submitting = ref(false);
const error = ref('');

const isJavaCode = computed(() => evaluation.value?.evaluationMode === 'JAVA_CODE');
const isNetwork = computed(() => evaluation.value?.evaluationMode === 'NETWORK_CHALLENGE');

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
    sourceCode.value = starterSource(props.id);
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải dữ liệu';
  } finally {
    loading.value = false;
  }
}

async function submitCode() {
  submitting.value = true;
  error.value = '';
  try {
    const submission = await submitJavaCode(props.id, participantId, sourceCode.value);
    router.push({ name: 'submission-detail', params: { id: submission.id } });
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Không nộp được mã Java';
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

function starterSource(id) {
  if (id === 'fnd-data-order-001') {
    return `import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(System.out));

        // TODO: đọc primitive theo đúng thứ tự và ghi kết quả binary.
        out.flush();
    }
}`;
  }

  return `import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));

        // TODO: đọc một dòng, chuẩn hóa payload, ghi một dòng và flush.
        out.flush();
    }
}`;
}

onMounted(load);
</script>

<template>
  <main class="shell">
    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="error && !exercise" class="panel state state-error">{{ error }}</section>

    <article v-else class="panel practice">
      <RouterLink class="back" :to="{ name: 'exercise-detail', params: { id } }">Quay lại đề bài</RouterLink>
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

      <section v-if="isJavaCode" class="practice-section">
        <div class="section-heading">
          <div>
            <h2>Nộp mã Java</h2>
            <p>Mã được biên dịch và chạy trong runner Docker cô lập. Không có dependency ngoài JDK 21.</p>
          </div>
        </div>
        <textarea v-model="sourceCode" class="source-editor" spellcheck="false" />
        <div class="actions">
          <button type="button" :disabled="submitting || sourceCode.length > 20000" @click="submitCode">
            {{ submitting ? 'Đang chấm...' : 'Nộp mã Java' }}
          </button>
          <span class="muted">{{ sourceCode.length }} / 20000 ký tự</span>
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

.source-editor {
  width: 100%;
  min-height: 360px;
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
</style>
