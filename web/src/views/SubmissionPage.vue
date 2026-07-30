<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { RouterLink } from 'vue-router';
import { ApiError } from '../api/http';
import { fetchSubmission } from '../api/exercises';
import { getParticipantId } from '../api/participant';
import { verdictLabel } from '../utils/display';

const props = defineProps({
  id: {
    type: String,
    required: true
  }
});

const participantId = getParticipantId();
const submission = ref(null);
const loading = ref(true);
const error = ref('');
let timer = null;

async function load() {
  try {
    submission.value = await fetchSubmission(props.id, participantId);
    error.value = '';
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải submission';
  } finally {
    loading.value = false;
  }
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
    <section v-else-if="error" class="panel state state-error">{{ error }}</section>

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
    </article>
  </main>
</template>

<style scoped>
.result {
  padding: 22px;
}

.back {
  display: inline-flex;
  align-items: center;
  min-height: 38px;
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

section {
  margin-top: 22px;
}

pre {
  overflow: auto;
  max-height: 320px;
  padding: 12px;
  border-radius: 6px;
  background: #eef2f6;
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

@media (max-width: 680px) {
  .test-row {
    grid-template-columns: 1fr;
  }
}
</style>
