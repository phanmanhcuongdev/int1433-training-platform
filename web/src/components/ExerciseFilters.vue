<script setup>
const model = defineModel({
  type: Object,
  required: true
});

defineProps({
  technologies: {
    type: Array,
    required: true
  },
  levels: {
    type: Array,
    required: true
  },
  sourceLabels: {
    type: Array,
    required: true
  }
});

function update(field, value) {
  model.value = {
    ...model.value,
    [field]: value
  };
}

function reset() {
  model.value = {
    search: '',
    technology: '',
    level: '',
    sourceLabel: ''
  };
}
</script>

<template>
  <section class="panel filters" aria-label="Bo loc bai tap">
    <label class="search-field">
      <span>Tim theo title, id, tag</span>
      <input
        :value="model.search"
        type="search"
        placeholder="tcp, udp, flush..."
        @input="update('search', $event.target.value)"
      />
    </label>

    <label>
      <span>Technology</span>
      <select :value="model.technology" @change="update('technology', $event.target.value)">
        <option value="">Tat ca</option>
        <option v-for="technology in technologies" :key="technology" :value="technology">
          {{ technology }}
        </option>
      </select>
    </label>

    <label>
      <span>Level</span>
      <select :value="model.level" @change="update('level', $event.target.value)">
        <option value="">Tat ca</option>
        <option v-for="level in levels" :key="level" :value="level">
          {{ level }}
        </option>
      </select>
    </label>

    <label>
      <span>source label</span>
      <select :value="model.sourceLabel" @change="update('sourceLabel', $event.target.value)">
        <option value="">Tat ca</option>
        <option v-for="label in sourceLabels" :key="label" :value="label">
          {{ label }}
        </option>
      </select>
    </label>

    <button type="button" @click="reset">Xoa loc</button>
  </section>
</template>

<style scoped>
.filters {
  display: grid;
  grid-template-columns: minmax(220px, 1.7fr) repeat(3, minmax(140px, 1fr)) auto;
  gap: 12px;
  align-items: end;
  padding: 16px;
  margin-top: 24px;
}

label {
  display: grid;
  gap: 5px;
}

span {
  color: #526171;
  font-size: 0.82rem;
  font-weight: 700;
}

input,
select,
button {
  min-height: 40px;
  border: 1px solid #cfd8e3;
  border-radius: 6px;
  background: #ffffff;
  color: #1f2933;
}

input,
select {
  width: 100%;
  padding: 0 10px;
}

button {
  padding: 0 14px;
  font-weight: 750;
}

@media (max-width: 920px) {
  .filters {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .filters {
    grid-template-columns: 1fr;
  }
}
</style>
