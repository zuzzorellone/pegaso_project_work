import { BrowserRouter, Routes, Route } from 'react-router-dom';
import HomePage from "./pages/HomePage.jsx";
import BookingPage from "./pages/BookingPage.jsx";
import ReportPage from "./pages/ReportPage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import DoctorDashboardPage from "./pages/DoctorDashboardPage.jsx";
import AdminDashboardPage from "./pages/AdminDashboardPage.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/bookAppointment" element={<BookingPage />} />
                <Route path="/downloadReport" element={<ReportPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/doctor/login" element={<LoginPage />} />
                <Route path="/doctor/dashboard" element={
                    <ProtectedRoute role="DOCTOR"><DoctorDashboardPage /></ProtectedRoute>
                } />
                <Route path="/admin/dashboard" element={
                    <ProtectedRoute role="ADMIN"><AdminDashboardPage /></ProtectedRoute>
                } />
            </Routes>
        </BrowserRouter>
    );
}

export default App
