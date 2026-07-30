<script setup>
import { computed, ref } from 'vue';
import ExerciseCard from './components/ExerciseCard.vue';
import ExerciseDetail from './components/ExerciseDetail.vue';
import ExerciseFilters from './components/ExerciseFilters.vue';
import ProjectNotice from './components/ProjectNotice.vue';
import { useExercises } from './composables/useExercises';

const { exercises, error, loading, malformed } = useExercises();
const selectedId = ref('');
const filters = ref({
  search: '',
  technology: '',
  level: '',
  sourceLabel: ''
});

const technologies = computed(() => uniqueSorted(exercises.value.map((exercise) => exercise.technology)));
const levels = computed(() => uniqueSorted(exercises.value.map((exercise) => exercise.level)));
const sourceLabels = computed(() => uniqueSorted(exercises.value.map((exercise) => exercise.source_label)));

const filteredExercises = computed(() => {
  const search = filters.value.search.trim().toLowerCase();

  return exercises.value.filter((exercise) => {
    const matchesSearch = !search ||
      [exercise.id, exercise.title, ...(exercise.tags || [])]
        .join(' ')
        .toLowerCase()
        .includes(search);

    return matchesSearch &&
      (!filters.value.technology || exercise.technology === filters.value.technology) &&
      (!filters.value.level || exercise.level === filters.value.level) &&
      (!filters.value.sourceLabel || exercise.source_label === filters.value.sourceLabel);
  });
});

const selectedExercise = computed(() => {
  return exercises.value.find((exercise) => exercise.id === selectedId.value) || null;
});

function uniqueSorted(values) {
  return [...new Set(values.filter(Boolean))].sort((a, b) => a.localeCompare(b));
}

function selectExercise(exercise) {
  selectedId.value = exercise.id;
  window.scrollTo({ top: 0, behavior: 'smooth' });
}
</script>

<template>
  <main class="shell">
    <ProjectNotice :count="exercises.length" />

    <section v-if="loading" class="panel state">
      Dang tai danh sach bai tap...
    </section>

    <section v-else-if="error" class="panel state state-error">
      Khong doc duoc content generated: {{ error }}
    </section>

    <section v-else-if="malformed" class="panel state state-error">
      File content generated khong dung dinh dang mong doi.
    </section>

    <ExerciseDetail
      v-else-if="selectedExercise"
      :exercise="selectedExercise"
      @back="selectedId = ''"
    />

    <template v-else>
      <ExerciseFilters
        v-model="filters"
        :technologies="technologies"
        :levels="levels"
        :source-labels="sourceLabels"
      />

      <section class="catalog">
        <div class="section-heading">
          <h2>Danh sach bai pilot</h2>
          <p>{{ filteredExercises.length }} / {{ exercises.length }} bai phu hop bo loc</p>
        </div>

        <div v-if="filteredExercises.length === 0" class="panel state">
          Khong co bai nao khop voi bo loc hien tai.
        </div>

        <div v-else class="exercise-grid">
          <ExerciseCard
            v-for="exercise in filteredExercises"
            :key="exercise.id"
            :exercise="exercise"
            @select="selectExercise"
          />
        </div>
      </section>
    </template>
  </main>
</template>
