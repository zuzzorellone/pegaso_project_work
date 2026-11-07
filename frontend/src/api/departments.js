import { apiFetch } from './client.js';

export function getDepartments() {
    return apiFetch('/medical-examination-types');
}

export async function getDepartmentLabels() {
    const departments = await getDepartments();
    return Object.fromEntries(departments.map(d => [d.name, d.description || d.name]));
}

export function createDepartment(data) {
    return apiFetch('/medical-examination-types', { method: 'POST', body: data, auth: true });
}

export function updateDepartment(id, data) {
    return apiFetch(`/medical-examination-types/${id}`, { method: 'PUT', body: data, auth: true });
}
