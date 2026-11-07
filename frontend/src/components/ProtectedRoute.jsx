import { Navigate } from 'react-router-dom';
import { getRole, hasValidSession } from '../api/session.js';

function ProtectedRoute({ children, role }) {
    if (!hasValidSession() || (role && getRole() !== role)) {
        return <Navigate to="/login" replace />;
    }
    return children;
}

export default ProtectedRoute;
