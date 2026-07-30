export const sourceLabelDescriptions = {
  OBSERVED: 'Pattern thấy trực tiếp trong nguồn nghiên cứu.',
  STRONG_PATTERN: 'Pattern có bằng chứng lặp lại.',
  EXTENDED: 'Bài mở rộng để học chắc hơn.',
  CHALLENGE: 'Ngoài scope thi cơ bản.'
};

export const failureLabels = {
  missing_flush: 'Quên gọi flush()',
  missing_newline: 'Thiếu newline khi contract yêu cầu theo dòng',
  readline_blocking: 'readLine() bị treo do thiếu kết thúc dòng',
  wrong_request_format: 'Sai format request',
  console_output_used_as_submission: 'Nhầm console output là submission',
  missing_request_id: 'Thiếu hoặc thay đổi requestId',
  using_full_buffer_instead_of_packet_length: 'Dùng toàn bộ buffer thay vì packet length',
  wrong_udp_request_prefix: 'Sai prefix request UDP',
  stale_port_assumption: 'Giả định port cũ vẫn còn đúng'
};

export function failureLabel(code) {
  return failureLabels[code] || code;
}

export function minutesLabel(minutes) {
  if (!Number.isFinite(minutes)) {
    return '';
  }
  if (minutes < 60) {
    return `${minutes} phút`;
  }
  const hours = Math.floor(minutes / 60);
  const rest = minutes % 60;
  return rest ? `${hours} giờ ${rest} phút` : `${hours} giờ`;
}
