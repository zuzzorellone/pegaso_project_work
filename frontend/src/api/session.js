const TOKEN_KEY = 'authToken';
const ROLE_KEY = 'staffRole';

export function saveSession(token, role) {
    sessionStorage.setItem(TOKEN_KEY, token);
    sessionStorage.setItem(ROLE_KEY, role);
}

export function getToken() {
    return sessionStorage.getItem(TOKEN_KEY);
}

export function getRole() {
    return sessionStorage.getItem(ROLE_KEY);
}

export function clearSession() {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(ROLE_KEY);
}

function tokenExpiry(token) {
    try {
        const payload = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
        return JSON.parse(atob(payload)).exp;
    } catch {
        return null;
    }
}

// The backend answers an expired token with an error rather than 401, so the expiry is checked here
export function hasValidSession() {
    const token = getToken();
    const exp = token && tokenExpiry(token);
    return Boolean(exp) && exp * 1000 > Date.now();
}
