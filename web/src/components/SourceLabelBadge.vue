<script setup>
const props = defineProps({
  label: {
    type: String,
    required: true
  }
});

import { sourceLabelDescriptions } from '../utils/display';
</script>

<template>
  <span class="source-label" :title="sourceLabelDescriptions[props.label] || props.label">
    {{ props.label }}
  </span>
</template>

<style scoped>
.source-label {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  padding: 4px 9px;
  border: 1px solid #cfd8e3;
  border-radius: 999px;
  color: #344256;
  background: #f5f7fa;
  font-size: 0.82rem;
  font-weight: 700;
}
</style>
