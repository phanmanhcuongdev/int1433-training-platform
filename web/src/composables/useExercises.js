import { onMounted, ref } from 'vue';

export function useExercises() {
  const exercises = ref([]);
  const loading = ref(true);
  const error = ref('');
  const malformed = ref(false);

  onMounted(async () => {
    try {
      const response = await fetch('/generated/exercises.json', {
        headers: { accept: 'application/json' }
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      const payload = await response.json();
      if (!payload || !Array.isArray(payload.exercises) || payload.count !== payload.exercises.length) {
        malformed.value = true;
        return;
      }

      exercises.value = payload.exercises;
    } catch (fetchError) {
      error.value = fetchError instanceof Error ? fetchError.message : String(fetchError);
    } finally {
      loading.value = false;
    }
  });

  return {
    exercises,
    loading,
    error,
    malformed
  };
}
