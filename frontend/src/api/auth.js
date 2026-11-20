import { apiFetch } from './client.js';

function decodeRole(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        .padEnd(base64Url.length + (4 - (base64Url.length % 4)) % 4, '=');
    return JSON.parse(atob(base64)).role;
}

export async function login(email, password) {
    const { token } = await apiFetch('/auth/login', { method: 'POST', body: { email, password } });
    return { token, role: decodeRole(token) };
}

export function registerDoctor(data) {
    return apiFetch('/auth/register', { method: 'POST', body: { ...data, role: 'DOCTOR' }, auth: true });
}
