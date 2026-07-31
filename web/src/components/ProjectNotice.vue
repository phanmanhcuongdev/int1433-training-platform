<script setup>
defineProps({
  healthStatus: {
    type: String,
    default: ''
  }
});
</script>

<template>
  <section class="notice">
    <p class="eyebrow">Version 0.6.0 · Natural Java filename submissions</p>
    <h1>Nền tảng luyện tập INT1433</h1>
    <p>
      Dự án luyện tập không chính thức cho môn INT1433. Đây không phải hệ thống PTIT,
      không cung cấp IP/port thi hiện hành, và không thay thế thông báo của giảng viên hoặc khoa.
    </p>

    <div class="track-grid">
      <article>
        <h2>Exam Track</h2>
        <p>Java-first cho TCP, UDP, RMI và Web Service contract.</p>
      </article>
      <article>
        <h2>Extended Networking Track</h2>
        <p>Bài mở rộng để nắm chắc stream, protocol và lỗi thường gặp.</p>
      </article>
      <article>
        <h2>Backend/Distributed Systems Track</h2>
        <p>Hướng mở rộng sau, tách khỏi mock exam cơ bản.</p>
      </article>
    </div>

    <div class="status-row">
      <span>Backend REST API</span>
      <span>PostgreSQL runtime source</span>
      <span>Java runner + network challenge</span>
      <span>Chưa có tài khoản</span>
      <span v-if="healthStatus">Health: {{ healthStatus }}</span>
    </div>
  </section>
</template>

<style scoped>
.notice {
  padding: 32px 0 12px;
}

.eyebrow {
  margin: 0 0 10px;
  color: #667485;
  font-size: 0.86rem;
  font-weight: 800;
  text-transform: uppercase;
}

h1 {
  margin: 0 0 14px;
  max-width: 900px;
  font-size: clamp(2rem, 7vw, 4.8rem);
  line-height: 1;
}

p {
  max-width: 860px;
  margin: 0;
  color: #465569;
}

.track-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin-top: 24px;
}

article {
  padding: 16px;
  border: 1px solid #d9e0e8;
  border-radius: 8px;
  background: #ffffff;
}

h2 {
  margin: 0 0 8px;
  font-size: 1rem;
}

.status-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 14px;
}

.status-row span {
  padding: 6px 10px;
  border: 1px solid #d9e0e8;
  border-radius: 999px;
  background: #ffffff;
  color: #405166;
  font-size: 0.9rem;
  font-weight: 700;
}

@media (max-width: 760px) {
  .track-grid {
    grid-template-columns: 1fr;
  }
}
</style>
