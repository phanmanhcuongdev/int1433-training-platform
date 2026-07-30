<script setup>
import { RouterLink } from 'vue-router';
import SourceLabelBadge from './SourceLabelBadge.vue';
import { failureLabel, minutesLabel } from '../utils/display';

defineProps({
  exercise: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <article class="detail panel">
    <RouterLink class="back" to="/exercises">Quay lại danh sách</RouterLink>

    <header>
      <p class="mono id">{{ exercise.id }}</p>
      <h2>{{ exercise.title }}</h2>
      <p class="summary">{{ exercise.summary || exercise.statement }}</p>
      <div class="metadata-row">
        <span class="pill">{{ exercise.track }}</span>
        <span class="pill">{{ exercise.technology }}</span>
        <span class="pill">{{ exercise.level }}</span>
        <span class="pill">{{ exercise.difficulty }}</span>
        <span class="pill">{{ minutesLabel(exercise.estimatedTimeMinutes) }}</span>
        <span class="pill">{{ exercise.status || 'DRAFT' }}</span>
        <SourceLabelBadge :label="exercise.sourceLabel" />
      </div>
    </header>

    <section>
      <h3>Đề bài</h3>
      <p>{{ exercise.statement }}</p>
    </section>

    <section v-if="exercise.learningObjectives?.length">
      <h3>Mục tiêu học tập</h3>
      <ul>
        <li v-for="item in exercise.learningObjectives" :key="item">{{ item }}</li>
      </ul>
    </section>

    <section>
      <h3>Yêu cầu xử lý</h3>
      <p>{{ exercise.processingRequirement }}</p>
    </section>

    <section v-if="exercise.prerequisites?.length">
      <h3>Kiến thức cần có</h3>
      <ul>
        <li v-for="item in exercise.prerequisites" :key="item">{{ item }}</li>
      </ul>
    </section>

    <section v-if="exercise.serverContract">
      <h3>Server contract</h3>
      <dl class="contract">
        <template v-for="(value, key) in exercise.serverContract" :key="key">
          <dt>{{ key }}</dt>
          <dd><code>{{ value }}</code></dd>
        </template>
      </dl>
    </section>

    <section class="formats">
      <h3>Contract format</h3>
      <dl>
        <template v-if="exercise.requestFormat">
          <dt>request</dt>
          <dd><code>{{ exercise.requestFormat }}</code></dd>
        </template>
        <template v-if="exercise.responseFormat">
          <dt>response</dt>
          <dd><code>{{ exercise.responseFormat }}</code></dd>
        </template>
        <template v-if="exercise.submissionFormat">
          <dt>submission</dt>
          <dd><code>{{ exercise.submissionFormat }}</code></dd>
        </template>
      </dl>
    </section>

    <section v-if="exercise.timeoutConfig">
      <h3>Timeout</h3>
      <pre>{{ JSON.stringify(exercise.timeoutConfig, null, 2) }}</pre>
    </section>

    <section>
      <h3>Lỗi thường gặp</h3>
      <ul>
        <li v-for="failure in exercise.commonFailures" :key="failure">
          {{ failureLabel(failure) }} <code>{{ failure }}</code>
        </li>
      </ul>
    </section>

    <section v-if="exercise.hints?.length">
      <h3>Gợi ý</h3>
      <ul>
        <li v-for="hint in exercise.hints" :key="hint">{{ hint }}</li>
      </ul>
    </section>

    <section>
      <h3>Nguồn và traceability</h3>
      <p>
        source label: <strong>{{ exercise.sourceLabel }}</strong>.
        Nội dung này dùng cho luyện tập, không phải thông báo thi chính thức.
      </p>
      <ul v-if="exercise.sources?.length">
        <li v-for="source in exercise.sources" :key="`${source.claimId}-${source.sourceFile}`">
          <code v-if="source.claimId">{{ source.claimId }}</code>
          <code v-if="source.sourceFile">{{ source.sourceFile }}</code>
          <span v-if="source.evidenceNote"> {{ source.evidenceNote }}</span>
        </li>
      </ul>
      <p v-else class="muted">Bài mở rộng không có claim ID trực tiếp.</p>
    </section>
  </article>
</template>

<style scoped>
.detail {
  padding: 22px;
  margin-top: 24px;
}

.back {
  display: inline-flex;
  align-items: center;
  min-height: 38px;
  margin-bottom: 20px;
  padding: 0 14px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  background: #ffffff;
  color: #203040;
  font-weight: 750;
  text-decoration: none;
}

.id {
  margin: 0 0 6px;
  color: #667485;
}

h2 {
  margin: 0 0 10px;
  font-size: clamp(1.7rem, 4vw, 2.6rem);
  line-height: 1.1;
}

h3 {
  margin: 0 0 8px;
}

.summary {
  margin: 0 0 14px;
  color: #526171;
}

section {
  margin-top: 24px;
}

p,
li,
dd {
  color: #405166;
}

ul {
  padding-left: 20px;
}

dl {
  display: grid;
  grid-template-columns: minmax(110px, 180px) minmax(0, 1fr);
  gap: 8px 12px;
  margin: 0;
}

dt {
  color: #667485;
  font-weight: 800;
}

dd {
  margin: 0;
  min-width: 0;
}

pre {
  overflow: auto;
  padding: 12px;
  border-radius: 6px;
  background: #eef2f6;
}

.formats code,
.contract code {
  word-break: break-word;
}

li code {
  display: inline-flex;
  margin: 4px 4px 0 0;
}

@media (max-width: 600px) {
  dl {
    grid-template-columns: 1fr;
  }
}
</style>
