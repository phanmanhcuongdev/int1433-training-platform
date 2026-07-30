<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ExerciseCard from '../components/ExerciseCard.vue';
import ExerciseFilters from '../components/ExerciseFilters.vue';
import { ApiError } from '../api/http';
import { fetchExerciseFilters, fetchExercises } from '../api/exercises';

const route = useRoute();
const router = useRouter();

const filters = ref({
  search: String(route.query.q || ''),
  technology: String(route.query.technology || ''),
  level: String(route.query.level || ''),
  sourceLabel: String(route.query.sourceLabel || '')
});
const page = ref(Number(route.query.page || 0));
const pageSize = 20;
const data = ref({ items: [], page: 0, size: pageSize, totalItems: 0, totalPages: 0 });
const metadata = ref({ technologies: [], levels: [], sourceLabels: [], statuses: [] });
const loading = ref(false);
const error = ref('');

const hasPrevious = computed(() => data.value.page > 0);
const hasNext = computed(() => data.value.page + 1 < data.value.totalPages);

async function loadExercises(signal) {
  loading.value = true;
  error.value = '';
  try {
    data.value = await fetchExercises({
      q: filters.value.search,
      technology: filters.value.technology,
      level: filters.value.level,
      sourceLabel: filters.value.sourceLabel,
      page: page.value,
      size: pageSize
    }, { signal });
  } catch (requestError) {
    if (requestError.name === 'AbortError') return;
    error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải dữ liệu';
  } finally {
    loading.value = false;
  }
}

async function loadFilters() {
  try {
    metadata.value = await fetchExerciseFilters();
  } catch {
    metadata.value = { technologies: [], levels: [], sourceLabels: [], statuses: [] };
  }
}

function syncQuery() {
  const query = {
    q: filters.value.search || undefined,
    technology: filters.value.technology || undefined,
    level: filters.value.level || undefined,
    sourceLabel: filters.value.sourceLabel || undefined,
    page: page.value > 0 ? String(page.value) : undefined
  };
  router.replace({ name: 'exercises', query });
}

function nextPage() {
  if (hasNext.value) {
    page.value += 1;
  }
}

function previousPage() {
  if (hasPrevious.value) {
    page.value -= 1;
  }
}

watch(filters, () => {
  page.value = 0;
  syncQuery();
}, { deep: true });

watch(page, syncQuery);

watch(() => route.query, () => {
  filters.value = {
    search: String(route.query.q || ''),
    technology: String(route.query.technology || ''),
    level: String(route.query.level || ''),
    sourceLabel: String(route.query.sourceLabel || '')
  };
  page.value = Number(route.query.page || 0);
}, { deep: true });

watch([filters, page], (newValue, oldValue, onCleanup) => {
  const controller = new AbortController();
  loadExercises(controller.signal);
  onCleanup(() => controller.abort());
}, { deep: true });

onMounted(() => {
  loadFilters();
  loadExercises();
});
</script>

<template>
  <main class="shell">
    <div class="section-heading page-title">
      <div>
        <h1>Danh sách bài tập</h1>
        <p>Catalog được tải từ Spring Boot API, dữ liệu runtime nằm trong PostgreSQL.</p>
      </div>
    </div>

    <ExerciseFilters
      v-model="filters"
      :technologies="metadata.technologies"
      :levels="metadata.levels"
      :source-labels="metadata.sourceLabels"
    />

    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="error" class="panel state state-error">{{ error }}</section>

    <section v-else class="catalog">
      <div class="section-heading">
        <h2>Bài tập pilot</h2>
        <p>{{ data.totalItems }} bài phù hợp</p>
      </div>

      <div v-if="data.items.length === 0" class="panel state">
        Không tìm thấy bài tập phù hợp.
      </div>

      <div v-else class="exercise-grid">
        <ExerciseCard v-for="exercise in data.items" :key="exercise.id" :exercise="exercise" />
      </div>

      <div class="pagination">
        <button type="button" :disabled="!hasPrevious" @click="previousPage">Trang trước</button>
        <span>Trang {{ data.page + 1 }} / {{ Math.max(data.totalPages, 1) }}</span>
        <button type="button" :disabled="!hasNext" @click="nextPage">Trang sau</button>
      </div>
    </section>
  </main>
</template>

<style scoped>
.page-title {
  margin-top: 22px;
}

h1,
h2,
p {
  margin: 0;
}

.page-title p {
  margin-top: 6px;
  color: #526171;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 20px;
}

.pagination button {
  min-height: 38px;
  padding: 0 12px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  background: #ffffff;
  color: #203040;
  font-weight: 750;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.45;
}
</style>
