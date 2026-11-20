import { apiFetch } from './client.js';

export function getBookedDates(departmentName) {
    const query = new URLSearchParams({ medicalExaminationType: departmentName });
    return apiFetch(`/appointments/bookedDates?${query}`);
}

export function getAvailableTimes(departmentName, date) {
    const query = new URLSearchParams({ medicalExaminationType: departmentName, selectedDate: date });
    return apiFetch(`/appointments/availableTimes?${query}`);
}

export function createAppointment(request) {
    return apiFetch('/appointments', { method: 'POST', body: request });
}
