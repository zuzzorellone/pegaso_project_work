import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Container, Typography, TextField, Button, Box, Paper, Alert,
    CircularProgress, InputAdornment, IconButton
} from '@mui/material';
import MedicalServicesIcon from '@mui/icons-material/MedicalServices';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Navbar from '../components/Navbar.jsx';
import { login } from '../api/auth.js';
import { saveSession } from '../api/session.js';
import '../styles/LoginPage.css';

function LoginPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({ email: '', password: '' });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showPassword, setShowPassword] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const { token, role } = await login(formData.email, formData.password);
            saveSession(token, role);
            navigate(role === 'ADMIN' ? '/admin/dashboard' : '/doctor/dashboard');
        } catch (err) {
            setError(err.status
                ? 'Credenziali non valide. Riprova.'
                : 'Impossibile contattare il server. Riprova più tardi.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Navbar />
            <Container maxWidth="xs" className="login">
                <Paper elevation={0} className="login__card">

                    <Box className="login__icon-wrapper">
                        <MedicalServicesIcon className="login__icon" />
                    </Box>

                    <Typography variant="h5" className="login__title">
                        Area Riservata
                    </Typography>
                    <Typography variant="body2" className="login__subtitle">
                        Accedi con le tue credenziali di staff per continuare.
                    </Typography>

                    {error && (
                        <Alert severity="error" className="login__alert">{error}</Alert>
                    )}

                    <Box component="form" onSubmit={handleSubmit} className="login__form">

                        <TextField
                            label="E-mail"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            variant="outlined"
                            fullWidth
                            required
                            autoFocus
                        />

                        <TextField
                            label="Password"
                            name="password"
                            type={showPassword ? 'text' : 'password'}
                            value={formData.password}
                            onChange={handleChange}
                            variant="outlined"
                            fullWidth
                            required
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            onClick={() => setShowPassword(prev => !prev)}
                                            edge="end"
                                        >
                                            {showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                )
                            }}
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            fullWidth
                            disabled={loading || !formData.email || !formData.password}
                            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : null}
                            className="login__submit"
                        >
                            {loading ? 'Accesso in corso...' : 'Accedi'}
                        </Button>

                    </Box>
                </Paper>
            </Container>
        </>
    );
}

export default LoginPage;
