export class ApiError extends Error {
  constructor(message, status, payload) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.payload = payload;
  }
}

export async function getJson(path, { signal, headers = {} } = {}) {
  return requestJson(path, { method: 'GET', signal, headers });
}

export async function postJson(path, body = undefined, { signal, headers = {} } = {}) {
  return requestJson(path, {
    method: 'POST',
    body: body === undefined ? undefined : JSON.stringify(body),
    headers: { 'content-type': 'application/json', ...headers },
    signal
  });
}

export async function postForm(path, formData, { signal, headers = {} } = {}) {
  return requestJson(path, {
    method: 'POST',
    body: formData,
    headers,
    signal
  });
}

export async function requestJson(path, { method = 'GET', body, signal, headers = {} } = {}) {
  const response = await fetch(path, {
    method,
    body,
    headers: { accept: 'application/json', ...headers },
    signal
  });

  let payload = null;
  const contentType = response.headers.get('content-type') || '';
  if (contentType.includes('application/json')) {
    payload = await response.json();
  }

  if (!response.ok) {
    const message = payload?.message || `Yêu cầu thất bại với HTTP ${response.status}`;
    throw new ApiError(message, response.status, payload);
  }

  return payload;
}

export function toQueryString(params) {
  const search = new URLSearchParams();

  for (const [key, value] of Object.entries(params)) {
    if (value !== undefined && value !== null && String(value).trim() !== '') {
      search.set(key, String(value));
    }
  }

  const query = search.toString();
  return query ? `?${query}` : '';
}
