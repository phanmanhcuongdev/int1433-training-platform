<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { RouterLink } from 'vue-router';
import { ApiError } from '../api/http';
import { fetchChallengeSession } from '../api/exercises';
import { getParticipantId } from '../api/participant';
import { challengeStateLabel, verdictLabel } from '../utils/display';

const props = defineProps({
  id: {
    type: String,
    required: true
  }
});

const participantId = getParticipantId();
const session = ref(null);
const loading = ref(true);
const error = ref('');
let timer = null;

const token = computed(() => sessionStorage.getItem(`int1433.challenge.${props.id}.token`) || '');
const terminal = computed(() => ['AC', 'WA', 'PROTOCOL_ERROR', 'TIMEOUT', 'EXPIRED', 'INTERNAL_ERROR'].includes(session.value?.state));
const secondsLeft = computed(() => {
  if (!session.value?.expiresAt) return 0;
  return Math.max(0, Math.floor((new Date(session.value.expiresAt).getTime() - Date.now()) / 1000));
});

async function load() {
  try {
    session.value = await fetchChallengeSession(props.id, participantId);
    error.value = '';
    if (terminal.value && timer) {
      window.clearInterval(timer);
      timer = null;
    }
  } catch (requestError) {
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải phiên kết nối';
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  load();
  timer = window.setInterval(load, 1500);
});

onUnmounted(() => {
  if (timer) window.clearInterval(timer);
});
</script>

<template>
  <main class="shell">
    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="error" class="panel state state-error">{{ error }}</section>

    <article v-else class="panel session">
      <RouterLink class="back" :to="{ name: 'exercise-practice', params: { id: session.exerciseId } }">
        Quay lại luyện tập
      </RouterLink>
      <header>
        <p class="mono">{{ session.sessionId }}</p>
        <h1>{{ challengeStateLabel(session.state) }}</h1>
        <p>{{ session.publicMessage || 'Client Java của bạn cần kết nối tới thông tin phiên bên dưới.' }}</p>
        <div class="metadata-row">
          <span class="pill">{{ session.exerciseId }}</span>
          <span class="pill">{{ verdictLabel(session.verdict) }}</span>
          <span class="pill">Còn {{ secondsLeft }} giây</span>
        </div>
      </header>

      <section class="connection">
        <h2>Thông tin kết nối</h2>
        <dl>
          <dt>host</dt>
          <dd><code>{{ session.host }}</code></dd>
          <dt v-if="session.port">port</dt>
          <dd v-if="session.port"><code>{{ session.port }}</code></dd>
          <dt v-if="session.endpoint">endpoint/service</dt>
          <dd v-if="session.endpoint"><code>{{ session.endpoint }}</code></dd>
          <dt>token</dt>
          <dd><code>{{ token || 'Token chỉ hiển thị ngay sau khi tạo phiên trong browser session này.' }}</code></dd>
          <dt>qCode</dt>
          <dd><code>{{ session.qCode }}</code></dd>
        </dl>
      </section>

      <section>
        <h2>Hướng dẫn phiên</h2>
        <ul>
          <li v-for="line in session.instructions" :key="line">{{ line }}</li>
        </ul>
      </section>

      <section>
        <h2>Protocol timeline</h2>
        <ol class="timeline">
          <li v-for="(step, index) in session.protocolTrace" :key="`${step.state}-${index}`">
            <strong>{{ challengeStateLabel(step.state) }}</strong>
            <span>{{ step.message }}</span>
          </li>
        </ol>
      </section>
    </article>
  </main>
</template>

<style scoped>
.session {
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

dl {
  display: grid;
  grid-template-columns: 140px minmax(0, 1fr);
  gap: 8px 12px;
}

dt {
  color: #667485;
  font-weight: 800;
}

dd {
  min-width: 0;
  margin: 0;
}

code {
  word-break: break-word;
}

.timeline {
  display: grid;
  gap: 8px;
  padding-left: 20px;
}

.timeline li {
  padding: 10px;
  border: 1px solid #d9e0e8;
  border-radius: 6px;
}

.timeline span {
  display: block;
  color: #526171;
}

@media (max-width: 640px) {
  dl {
    grid-template-columns: 1fr;
  }
}
</style>
