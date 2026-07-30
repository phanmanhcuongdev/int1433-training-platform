<template>
  <main class="shell">
    <section class="panel about">
      <h1>Giới thiệu</h1>
      <p>
        INT1433 Training Platform là dự án luyện tập không chính thức. Dự án không phải hệ thống thi
        của PTIT, không cung cấp IP/port thi hiện hành và không thay thế thông báo chính thức.
      </p>

      <h2>Evidence labels</h2>
      <ul>
        <li><strong>OBSERVED</strong>: pattern thấy trực tiếp trong nguồn nghiên cứu.</li>
        <li><strong>STRONG_PATTERN</strong>: pattern có bằng chứng lặp lại.</li>
        <li><strong>EXTENDED</strong>: bài mở rộng để học chắc hơn.</li>
        <li><strong>CHALLENGE</strong>: ngoài scope thi cơ bản.</li>
      </ul>

      <h2>Ranh giới hiện tại</h2>
      <p>
        Phiên bản này chỉ có catalog bài tập, REST API và PostgreSQL. Chưa có judge, chưa có submit
        online, không chạy code người dùng và không có authentication.
      </p>

      <h2>Nguồn nghiên cứu</h2>
      <p>
        Research snapshot nằm trong <code>docs/research/</code>. Đây là tài liệu tổng hợp trong
        repository, không phải tài liệu chính thức của PTIT.
      </p>
    </section>
  </main>
</template>

<style scoped>
.about {
  padding: 24px;
  margin-top: 24px;
}

h1,
h2 {
  margin-top: 0;
}

p,
li {
  color: #405166;
}
</style>
