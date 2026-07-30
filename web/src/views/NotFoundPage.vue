<template>
  <main class="shell">
    <section class="panel state state-error">
      <h1>Không tìm thấy trang</h1>
      <p>Đường dẫn này không tồn tại trong ứng dụng.</p>
      <RouterLink to="/exercises">Quay lại danh sách bài tập</RouterLink>
    </section>
  </main>
</template>

<style scoped>
h1,
p {
  margin-top: 0;
}

a {
  color: #203040;
  font-weight: 800;
}
</style>
