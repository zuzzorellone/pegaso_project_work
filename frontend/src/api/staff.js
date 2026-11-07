import { apiFetch } from './client.js';

const base = '/staff/me/appointments';

export function getUpcomingAppointments() {
    return apiFetch(`${base}/future`, { auth: true });
}

export function getPastAppointments() {
    return apiFetch(`${base}/past`, { auth: true });
}

export function confirmAttendance(id) {
    return apiFetch(`${base}/${id}/attendance`, { method: 'POST', auth: true, as: 'none' });
}

export function uploadReport(id, file) {
    const form = new FormData();
    form.append('file', file);
    return apiFetch(`${base}/${id}/report`, { method: 'POST', body: form, auth: true });
}

export function resendReport(id) {
    return apiFetch(`${base}/${id}/report/resend`, { method: 'POST', auth: true, as: 'none' });
}

export function downloadReport(id) {
    return apiFetch(`${base}/${id}/report/download`, { auth: true, as: 'blob' });
}
