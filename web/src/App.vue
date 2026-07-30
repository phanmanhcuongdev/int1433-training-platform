<template>
  <main class="shell">
    <section class="hero">
      <p class="eyebrow">Bootstrap · Content-only</p>
      <h1>INT1433 Training Platform</h1>
      <p class="notice">
        This is an unofficial practice project. It is not a PTIT system, not an exam server,
        and not a source of current official exam rules.
      </p>
    </section>

    <section class="panel">
      <h2>Tracks</h2>
      <div class="track-grid">
        <article>
          <h3>Exam Track</h3>
          <p>Java-first practice for TCP, UDP, RMI, and SOAP Web Service contracts.</p>
        </article>
        <article>
          <h3>Extended Networking Track</h3>
          <p>Networking exercises beyond exam drills, introduced after the pilot content is stable.</p>
        </article>
        <article>
          <h3>Backend/Distributed Systems Track</h3>
          <p>Future backend and distributed systems topics that are separate from mock exams.</p>
        </article>
      </div>
    </section>

    <section class="panel status">
      <h2>Status</h2>
      <ul>
        <li>Bootstrap repository: ready</li>
        <li>Judge: not implemented</li>
        <li>Content pilot: three draft exercises</li>
        <li>Backend/database/auth: not started</li>
      </ul>
    </section>
  </main>
</template>

<style>
:root {
  color: #1b1f24;
  background: #f5f7fb;
  font-family: Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

body {
  margin: 0;
}

.shell {
  width: min(1080px, calc(100% - 32px));
  margin: 0 auto;
  padding: 56px 0;
}

.hero {
  padding: 32px 0 24px;
}

.eyebrow {
  margin: 0 0 12px;
  color: #536070;
  font-size: 0.9rem;
  font-weight: 700;
  text-transform: uppercase;
}

h1 {
  margin: 0 0 16px;
  max-width: 760px;
  font-size: clamp(2.3rem, 7vw, 5rem);
  line-height: 1;
}

h2,
h3,
p {
  margin-top: 0;
}

.notice {
  max-width: 760px;
  color: #344050;
  font-size: 1.1rem;
  line-height: 1.6;
}

.panel {
  margin-top: 28px;
  padding: 24px;
  background: #ffffff;
  border: 1px solid #dde3ec;
  border-radius: 8px;
}

.track-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

article {
  padding: 16px;
  border: 1px solid #e2e7f0;
  border-radius: 8px;
}

article p,
.status li {
  color: #465366;
  line-height: 1.5;
}

@media (max-width: 760px) {
  .track-grid {
    grid-template-columns: 1fr;
  }
}
</style>

