import { clearSession, getToken, hasValidSession } from './session.js';

export const API_BASE_URL = import.meta.env?.VITE_API_URL || 'http://localhost:8080/api/v1';

export class ApiError extends Error {
    constructor(status, serverMessage) {
        super(serverMessage || `Errore ${status}`);
        this.name = 'ApiError';
        this.status = status;
        this.serverMessage = serverMessage || null;
    }
}

function backToLogin() {
    clearSession();
    window.location.assign('/login');
}

function filenameFrom(response) {
    const disposition = response.headers.get('Content-Disposition') || '';
    const match = /filename="([^"]*)"/.exec(disposition);
    return match ? match[1] : null;
}

export async function apiFetch(path, { method = 'GET', body, auth = false, as = 'json' } = {}) {
    const headers = {};
    let payload = body;
    if (body !== undefined && !(body instanceof FormData)) {
        headers['Content-Type'] = 'application/json';
        payload = JSON.stringify(body);
    }
    if (auth) {
        if (!hasValidSession()) {
            backToLogin();
            throw new ApiError(401, 'Sessione scaduta');
        }
        headers['Authorization'] = `Bearer ${getToken()}`;
    }

    const response = await fetch(API_BASE_URL + path, { method, headers, body: payload });

    if (!response.ok) {
        if (auth && response.status === 401) {
            backToLogin();
        }
        const errorBody = await response.json().catch(() => null);
        throw new ApiError(response.status, errorBody?.message);
    }

    if (as === 'blob') {
        return { blob: await response.blob(), filename: filenameFrom(response) };
    }
    if (as === 'none') {
        return null;
    }
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}
