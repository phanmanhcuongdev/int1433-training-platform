<script setup>
defineProps({
  count: {
    type: Number,
    required: true
  }
});
</script>

<template>
  <section class="notice">
    <p class="eyebrow">Version 0.2.0 · Content pilot</p>
    <h1>INT1433 Training Platform</h1>
    <p>
      Nen tang luyen tap khong chinh thuc cho INT1433. Day khong phai he thong PTIT,
      khong cung cap IP/port thi hien hanh, va khong thay the thong bao cua giang vien/khoa.
    </p>

    <div class="track-grid">
      <article>
        <h2>Exam Track</h2>
        <p>Java-first cho TCP, UDP, RMI va Web Service contract.</p>
      </article>
      <article>
        <h2>Extended Networking Track</h2>
        <p>Bai mo rong de nam chac stream, protocol va loi thuong gap.</p>
      </article>
      <article>
        <h2>Backend/Distributed Systems Track</h2>
        <p>Huong mo rong sau, tach khoi mock exam co ban.</p>
      </article>
    </div>

    <div class="status-row">
      <span>{{ count }} bai pilot</span>
      <span>Chua co judge</span>
      <span>Chua co submit online</span>
    </div>
  </section>
</template>

<style scoped>
.notice {
  padding: 28px 0 12px;
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
