import { apiFetch } from './client.js';

export function getPendingAppointments() {
    return apiFetch('/admin/appointments/pending', { auth: true });
}

export function confirmAppointment(id) {
    return apiFetch(`/admin/appointments/${id}/confirm`, { method: 'POST', auth: true, as: 'none' });
}

export function rejectAppointment(id) {
    return apiFetch(`/admin/appointments/${id}/reject`, { method: 'POST', auth: true, as: 'none' });
}

export function getDoctors() {
    return apiFetch('/admin/doctors', { auth: true });
}

export function setDoctorEnabled(id, enabled) {
    const action = enabled ? 'enable' : 'disable';
    return apiFetch(`/admin/doctors/${id}/${action}`, { method: 'POST', auth: true, as: 'none' });
}

export function getSentReports() {
    return apiFetch('/admin/reports', { auth: true });
}

export function resendReportEmail(appointmentId) {
    return apiFetch(`/admin/reports/${appointmentId}/resend`, { method: 'POST', auth: true, as: 'none' });
}

export function getStats(period) {
    return apiFetch(`/admin/stats?${new URLSearchParams({ period })}`, { auth: true });
}
