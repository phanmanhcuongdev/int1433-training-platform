<script setup>
import { onMounted, ref } from 'vue';
import { RouterLink } from 'vue-router';
import ProjectNotice from '../components/ProjectNotice.vue';
import { fetchHealth } from '../api/exercises';

const healthStatus = ref('');

onMounted(async () => {
  try {
    const health = await fetchHealth();
    healthStatus.value = health.status || 'UNKNOWN';
  } catch {
    healthStatus.value = 'chưa kết nối';
  }
});
</script>

<template>
  <main class="shell">
    <ProjectNotice :health-status="healthStatus" />

    <section class="panel home-actions">
      <div>
        <h2>Bắt đầu với danh sách bài tập</h2>
        <p>
          Phiên bản này dùng Spring Boot REST API và PostgreSQL làm runtime source.
          Không có submit online, local judge hay chạy code người dùng.
        </p>
      </div>
      <RouterLink to="/exercises">Mở danh sách bài tập</RouterLink>
    </section>
  </main>
</template>

<style scoped>
.home-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  padding: 20px;
  margin-top: 24px;
}

h2,
p {
  margin: 0;
}

p {
  margin-top: 6px;
  color: #526171;
}

a {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 42px;
  padding: 0 16px;
  border-radius: 6px;
  background: #203040;
  color: #ffffff;
  font-weight: 750;
  text-decoration: none;
  white-space: nowrap;
}

@media (max-width: 700px) {
  .home-actions {
    display: block;
  }

  a {
    width: 100%;
    margin-top: 16px;
  }
}
</style>
