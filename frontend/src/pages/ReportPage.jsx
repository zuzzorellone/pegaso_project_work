import {
    Container, Typography, TextField, Button, Box, Paper, Alert, CircularProgress
} from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import { useState, useEffect, useRef } from 'react';
import Navbar from "../components/Navbar.jsx";
import { useSearchParams } from 'react-router-dom';
import { downloadPublicReport } from '../api/reports.js';
import '../styles/ReportPage.css';

function ReportPage() {
    const [searchParams] = useSearchParams();
    const tokenDallaUrl = searchParams.get('token') || '';
    const taxIdDallaUrl = (searchParams.get('taxId') || '').toUpperCase();

    const [formData, setFormData] = useState({
        taxIdentificationNumber: taxIdDallaUrl,
        token: tokenDallaUrl
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const downloadReport = async (token, taxId) => {
        setLoading(true);
        setError(null);
        setSuccess(false);

        try {
            const { blob, filename } = await downloadPublicReport(token, taxId);
            const downloadUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = downloadUrl;
            a.download = filename || 'referto.pdf';
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(downloadUrl);
            setSuccess(true);
            setFormData({ taxIdentificationNumber: '', token: '' });
        } catch (err) {
            const messages = {
                401: 'Link scaduto. Contatta il centro medico per richiedere nuovamente il referto.',
                403: 'Hai superato il numero massimo di tentativi consentiti. Contatta il centro medico per assistenza.',
                400: 'Nessun referto trovato. Controlla di aver scritto correttamente il Codice Fiscale e il Token.',
                404: 'Nessun referto trovato. Controlla di aver scritto correttamente il Codice Fiscale e il Token.'
            };
            setError(messages[err.status] || 'Errore di comunicazione col server. Riprova più tardi.');
        } finally {
            setLoading(false);
        }
    };

    const handleDownload = (event) => {
        event.preventDefault();
        downloadReport(formData.token, formData.taxIdentificationNumber);
    };

    const autoTriggered = useRef(false);
    useEffect(() => {
        if (!autoTriggered.current && tokenDallaUrl && taxIdDallaUrl) {
            autoTriggered.current = true;
            downloadReport(tokenDallaUrl, taxIdDallaUrl);
        }
    }, []);

    return (
        <>
            <Navbar />
            <Container maxWidth="sm" className="report-page">
                <Paper elevation={0} className="report-card">

                    <Typography variant="h5" className="report-card__title">
                        Scarica Referto Medico
                    </Typography>
                    <Typography variant="body2" className="report-card__subtitle">
                        Inserisci il tuo Codice Fiscale e il Token univoco ricevuto via email o SMS per scaricare in sicurezza il tuo referto in formato PDF.
                    </Typography>

                    {error && <Alert severity="error" className="report-alert">{error}</Alert>}
                    {success && <Alert severity="success" className="report-alert">Download avviato con successo!</Alert>}

                    <Box component="form" onSubmit={handleDownload} className="report-form">

                        <TextField
                            label="Codice Fiscale"
                            name="taxIdentificationNumber"
                            value={formData.taxIdentificationNumber}
                            onChange={(e) => setFormData({ ...formData, taxIdentificationNumber: e.target.value.toUpperCase() })}
                            variant="outlined"
                            fullWidth
                            required
                            inputProps={{ maxLength: 16 }}
                        />

                        <TextField
                            label="Token Referto"
                            name="token"
                            value={formData.token}
                            onChange={handleChange}
                            variant="outlined"
                            fullWidth
                            required
                            helperText="Il codice alfanumerico fornito dalla clinica"
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            fullWidth
                            disabled={loading || formData.taxIdentificationNumber.length < 16 || formData.token.length === 0}
                            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <DownloadIcon />}
                            className="report-form__submit"
                        >
                            {loading ? 'Ricerca in corso...' : 'Scarica PDF'}
                        </Button>

                    </Box>
                </Paper>
            </Container>
        </>
    );
}

export default ReportPage;
