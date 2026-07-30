<script setup>
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import ExerciseDetail from '../components/ExerciseDetail.vue';
import { ApiError } from '../api/http';
import { fetchExercise } from '../api/exercises';

const route = useRoute();
const exercise = ref(null);
const loading = ref(false);
const error = ref('');
const notFound = ref(false);

async function loadExercise(id, signal) {
  loading.value = true;
  error.value = '';
  notFound.value = false;
  exercise.value = null;

  try {
    exercise.value = await fetchExercise(id, { signal });
  } catch (requestError) {
    if (requestError.name === 'AbortError') return;
    if (requestError instanceof ApiError && requestError.status === 404) {
      notFound.value = true;
    } else {
      error.value = requestError instanceof ApiError ? requestError.message : 'Lỗi khi tải dữ liệu';
    }
  } finally {
    loading.value = false;
  }
}

watch(() => route.params.id, (id, oldId, onCleanup) => {
  const controller = new AbortController();
  loadExercise(id, controller.signal);
  onCleanup(() => controller.abort());
});

onMounted(() => loadExercise(route.params.id));
</script>

<template>
  <main class="shell">
    <section v-if="loading" class="panel state">Đang tải dữ liệu...</section>
    <section v-else-if="notFound" class="panel state state-error">
      Không tìm thấy bài tập <code>{{ route.params.id }}</code>.
    </section>
    <section v-else-if="error" class="panel state state-error">{{ error }}</section>
    <ExerciseDetail v-else-if="exercise" :exercise="exercise" />
  </main>
</template>
