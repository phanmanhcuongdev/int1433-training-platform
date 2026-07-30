<script setup>
import SourceLabelBadge from './SourceLabelBadge.vue';

defineProps({
  exercise: {
    type: Object,
    required: true
  }
});

defineEmits(['back']);
</script>

<template>
  <article class="detail panel">
    <button class="back" type="button" @click="$emit('back')">Quay lai danh sach</button>

    <header>
      <p class="mono id">{{ exercise.id }}</p>
      <h2>{{ exercise.title }}</h2>
      <p class="summary">{{ exercise.summary || exercise.statement }}</p>
      <div class="metadata-row">
        <span class="pill">{{ exercise.track }}</span>
        <span class="pill">{{ exercise.technology }}</span>
        <span class="pill">{{ exercise.level }}</span>
        <span class="pill">{{ exercise.difficulty }}</span>
        <span class="pill">{{ exercise.estimated_time }}</span>
        <span class="pill">{{ exercise.status || 'DRAFT' }}</span>
        <SourceLabelBadge :label="exercise.source_label" />
      </div>
    </header>

    <section>
      <h3>De bai</h3>
      <p>{{ exercise.statement }}</p>
    </section>

    <section v-if="exercise.learning_objectives?.length">
      <h3>Learning objective</h3>
      <ul>
        <li v-for="item in exercise.learning_objectives" :key="item">{{ item }}</li>
      </ul>
    </section>

    <section>
      <h3>Processing requirement</h3>
      <p>{{ exercise.processing_requirement }}</p>
    </section>

    <section v-if="exercise.prerequisites?.length">
      <h3>Prerequisites</h3>
      <ul>
        <li v-for="item in exercise.prerequisites" :key="item">{{ item }}</li>
      </ul>
    </section>

    <section v-if="exercise.server_contract">
      <h3>Server contract</h3>
      <dl class="contract">
        <template v-for="(value, key) in exercise.server_contract" :key="key">
          <dt>{{ key }}</dt>
          <dd><code>{{ value }}</code></dd>
        </template>
      </dl>
    </section>

    <section class="formats">
      <h3>Contract format</h3>
      <dl>
        <template v-if="exercise.request_format">
          <dt>request</dt>
          <dd><code>{{ exercise.request_format }}</code></dd>
        </template>
        <template v-if="exercise.response_format">
          <dt>response</dt>
          <dd><code>{{ exercise.response_format }}</code></dd>
        </template>
        <template v-if="exercise.submission_format">
          <dt>submission</dt>
          <dd><code>{{ exercise.submission_format }}</code></dd>
        </template>
      </dl>
    </section>

    <section v-if="exercise.timeout">
      <h3>Timeout</h3>
      <pre>{{ JSON.stringify(exercise.timeout, null, 2) }}</pre>
    </section>

    <section>
      <h3>Common failures</h3>
      <ul>
        <li v-for="failure in exercise.common_failures" :key="failure">
          <code>{{ failure }}</code>
        </li>
      </ul>
    </section>

    <section v-if="exercise.hints?.length">
      <h3>Hints</h3>
      <ul>
        <li v-for="hint in exercise.hints" :key="hint">{{ hint }}</li>
      </ul>
    </section>

    <section>
      <h3>Traceability</h3>
      <p>
        source label: <strong>{{ exercise.source_label }}</strong>.
        Noi dung nay dung cho luyen tap, khong phai thong bao thi chinh thuc.
      </p>
      <p v-if="exercise.source_claim_ids?.length">
        Claim IDs:
        <code v-for="claim in exercise.source_claim_ids" :key="claim">{{ claim }}</code>
      </p>
      <ul v-if="exercise.source_files?.length">
        <li v-for="file in exercise.source_files" :key="file">
          <code>{{ file }}</code>
        </li>
      </ul>
    </section>
  </article>
</template>

<style scoped>
.detail {
  padding: 22px;
  margin-top: 24px;
}

.back {
  min-height: 38px;
  margin-bottom: 20px;
  padding: 0 14px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  background: #ffffff;
  color: #203040;
  font-weight: 750;
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

section p code {
  display: inline-flex;
  margin: 4px 4px 0 0;
}

@media (max-width: 600px) {
  dl {
    grid-template-columns: 1fr;
  }
}
</style>
