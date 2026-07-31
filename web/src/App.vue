<template>
  <div class="app">
    <header class="topbar">
      <div class="topbar-inner">
        <RouterLink class="brand" to="/" aria-label="Trang chủ INT1433 Training Platform">
          <span class="brand-mark" aria-hidden="true">I</span>
          <span>INT1433 Training Platform</span>
        </RouterLink>

        <nav aria-label="Điều hướng chính">
          <RouterLink to="/exercises">Bài tập</RouterLink>
          <RouterLink to="/about">Giới thiệu</RouterLink>
        </nav>
      </div>
    </header>

    <RouterView />
  </div>
</template>

<style scoped>
.topbar {
  position: sticky;
  top: 0;
  z-index: 20;
  border-bottom: 1px solid #d9e0e8;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(10px);
}

.topbar-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: min(1160px, calc(100% - 32px));
  min-height: 58px;
  margin: 0 auto;
  gap: 20px;
}

.brand {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  gap: 10px;
  color: #1f2933;
  font-weight: 800;
  text-decoration: none;
}

.brand span:last-child {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.brand-mark {
  display: inline-grid;
  flex: 0 0 auto;
  width: 32px;
  height: 32px;
  place-items: center;
  border-radius: 8px;
  color: #ffffff;
  background: #203648;
  font-size: 0.95rem;
  font-weight: 850;
}

nav {
  display: inline-flex;
  align-items: center;
  flex: 0 0 auto;
  gap: 4px;
  padding: 4px;
  border: 1px solid #d9e0e8;
  border-radius: 8px;
  background: #f8fafc;
}

nav a {
  display: inline-flex;
  align-items: center;
  min-height: 34px;
  padding: 0 12px;
  border-radius: 6px;
  color: #526171;
  font-size: 0.94rem;
  font-weight: 700;
  text-decoration: none;
}

nav a:hover {
  color: #1f2933;
  background: #eef2f6;
}

nav a.router-link-active {
  color: #ffffff;
  background: #203648;
}

@media (max-width: 640px) {
  .topbar-inner {
    width: min(100% - 20px, 1160px);
    min-height: 56px;
    gap: 10px;
  }

  .brand {
    font-size: 0.95rem;
  }

  .brand-mark {
    width: 30px;
    height: 30px;
  }

  nav a {
    min-height: 32px;
    padding: 0 9px;
    font-size: 0.88rem;
  }
}
</style>
