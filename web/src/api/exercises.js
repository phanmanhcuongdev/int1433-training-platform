import { getJson, postForm, postJson, toQueryString } from './http';

export function fetchExercises(params = {}, options = {}) {
  return getJson(`/api/v1/exercises${toQueryString(params)}`, options);
}

export function fetchExercise(id, options = {}) {
  return getJson(`/api/v1/exercises/${encodeURIComponent(id)}`, options);
}

export function fetchExerciseFilters(options = {}) {
  return getJson('/api/v1/exercises/filters', options);
}

export function fetchExerciseEvaluation(id, options = {}) {
  return getJson(`/api/v1/exercises/${encodeURIComponent(id)}/evaluation`, options);
}

export function submitJavaCode(id, participantId, sourceCode, options = {}) {
  return postJson(
    `/api/v1/exercises/${encodeURIComponent(id)}/code-submissions`,
    { language: 'JAVA', sourceCode },
    { ...options, headers: { 'X-Participant-Id': participantId } }
  );
}

export function submitJavaFile(id, participantId, file, options = {}) {
  const form = new FormData();
  form.set('file', file);
  return postForm(
    `/api/v1/exercises/${encodeURIComponent(id)}/submissions`,
    form,
    { ...options, headers: { 'X-Participant-Id': participantId } }
  );
}

export function startChallengeSession(id, participantId, options = {}) {
  return postJson(
    `/api/v1/exercises/${encodeURIComponent(id)}/challenge-sessions`,
    undefined,
    { ...options, headers: { 'X-Participant-Id': participantId } }
  );
}

export function fetchSubmission(id, participantId, options = {}) {
  return getJson(`/api/v1/submissions/${encodeURIComponent(id)}`, {
    ...options,
    headers: { 'X-Participant-Id': participantId }
  });
}

export function fetchSubmissions(participantId, options = {}) {
  return getJson('/api/v1/submissions', {
    ...options,
    headers: { 'X-Participant-Id': participantId }
  });
}

export function fetchChallengeSession(id, participantId, options = {}) {
  return getJson(`/api/v1/challenge-sessions/${encodeURIComponent(id)}`, {
    ...options,
    headers: { 'X-Participant-Id': participantId }
  });
}

export function fetchHealth(options = {}) {
  return getJson('/actuator/health', options);
}
