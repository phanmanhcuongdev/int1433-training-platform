<template>
  <div class="app">
    <header class="topbar">
      <RouterLink class="brand" to="/">INT1433 Training Platform</RouterLink>
      <nav aria-label="Điều hướng chính">
        <RouterLink to="/exercises">Bài tập</RouterLink>
        <RouterLink to="/about">Giới thiệu</RouterLink>
      </nav>
    </header>

    <RouterView />
  </div>
</template>
