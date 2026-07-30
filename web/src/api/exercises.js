import { getJson, toQueryString } from './http';

export function fetchExercises(params = {}, options = {}) {
  return getJson(`/api/v1/exercises${toQueryString(params)}`, options);
}

export function fetchExercise(id, options = {}) {
  return getJson(`/api/v1/exercises/${encodeURIComponent(id)}`, options);
}

export function fetchExerciseFilters(options = {}) {
  return getJson('/api/v1/exercises/filters', options);
}

export function fetchHealth(options = {}) {
  return getJson('/actuator/health', options);
}
