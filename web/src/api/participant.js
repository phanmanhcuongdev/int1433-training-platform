const STORAGE_KEY = 'int1433.participantId';

export function getParticipantId() {
  const existing = localStorage.getItem(STORAGE_KEY);
  if (isUuid(existing)) {
    return existing;
  }
  const created = crypto.randomUUID();
  localStorage.setItem(STORAGE_KEY, created);
  return created;
}

function isUuid(value) {
  return typeof value === 'string'
    && /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(value);
}
