<script setup>
import { RouterLink } from 'vue-router';
import SourceLabelBadge from './SourceLabelBadge.vue';
import { minutesLabel } from '../utils/display';

defineProps({
  exercise: {
    type: Object,
    required: true
  }
});

</script>

<template>
  <article class="card">
    <div class="card-top">
      <div>
        <p class="id mono">{{ exercise.id }}</p>
        <h3>{{ exercise.title }}</h3>
      </div>
      <SourceLabelBadge :label="exercise.sourceLabel" />
    </div>

    <p class="summary">{{ exercise.summary || exercise.statement }}</p>

    <div class="metadata-row">
      <span class="pill">{{ exercise.technology }}</span>
      <span class="pill">{{ exercise.level }}</span>
      <span class="pill">{{ exercise.difficulty }}</span>
      <span class="pill">{{ minutesLabel(exercise.estimatedTimeMinutes) }}</span>
      <span class="pill">{{ exercise.status || 'DRAFT' }}</span>
    </div>

    <div class="tags">
      <span v-for="tag in exercise.tags || []" :key="tag">#{{ tag }}</span>
    </div>

    <RouterLink class="detail-link" :to="`/exercises/${exercise.id}`">Xem chi tiết</RouterLink>
  </article>
</template>

<style scoped>
.card {
  display: flex;
  min-height: 310px;
  flex-direction: column;
  gap: 14px;
  padding: 18px;
  border: 1px solid #d9e0e8;
  border-radius: 8px;
  background: #ffffff;
}

.card-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}

.id {
  margin: 0 0 6px;
  color: #667485;
  font-size: 0.78rem;
}

h3 {
  margin: 0;
  font-size: 1.12rem;
  line-height: 1.25;
}

.summary {
  margin: 0;
  color: #526171;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: auto;
  color: #667485;
  font-size: 0.85rem;
}

.detail-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 40px;
  border: 1px solid #203040;
  border-radius: 6px;
  background: #203040;
  color: #ffffff;
  font-weight: 750;
  text-decoration: none;
}
</style>
