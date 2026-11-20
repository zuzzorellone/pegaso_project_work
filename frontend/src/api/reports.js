import { apiFetch } from './client.js';

export function downloadPublicReport(token, taxId) {
    const query = new URLSearchParams({ token, taxId: taxId.toUpperCase() });
    return apiFetch(`/public/reports/download?${query}`, { as: 'blob' });
}
