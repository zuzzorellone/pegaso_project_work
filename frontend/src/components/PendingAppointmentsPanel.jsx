import { useState, useEffect, useCallback } from 'react';
import {
    Typography, Button, Paper, Alert, CircularProgress, Table, TableHead, TableBody,
    TableRow, TableCell, TableContainer, Snackbar, Dialog, DialogTitle, DialogContent,
    DialogContentText, DialogActions, Box
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import dayjs from 'dayjs';

import { getDepartmentLabels } from '../api/departments.js';
import { getPendingAppointments, confirmAppointment, rejectAppointment } from '../api/admin.js';

function PendingAppointmentsPanel() {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [deptLabels, setDeptLabels] = useState({});
    const [actingId, setActingId] = useState(null);
    const [rejectTarget, setRejectTarget] = useState(null);
    const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

    useEffect(() => {
        getDepartmentLabels().then(setDeptLabels).catch(() => {});
    }, []);

    const loadAppointments = useCallback(() => {
        setLoading(true);
        getPendingAppointments()
            .then(data => { setAppointments(data); setFetchError(null); })
            .catch(() => setFetchError('Errore nel caricamento delle prenotazioni.'))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => { loadAppointments(); }, [loadAppointments]);

    const closeSnackbar = () => setSnackbar(prev => ({ ...prev, open: false }));

    const handleAction = async (appointmentId, action) => {
        setActingId(appointmentId);
        try {
            await (action === 'confirm' ? confirmAppointment(appointmentId) : rejectAppointment(appointmentId));
            setAppointments(prev => prev.filter(a => a.id !== appointmentId));
            setSnackbar({
                open: true,
                severity: 'success',
                message: action === 'confirm'
                    ? 'Prenotazione confermata. Il paziente riceverà una email.'
                    : 'Prenotazione rifiutata. Lo slot è di nuovo disponibile.'
            });
        } catch (err) {
            if (err.status === 409) {
                setSnackbar({ open: true, severity: 'warning', message: 'La prenotazione è già stata gestita. Elenco aggiornato.' });
                loadAppointments();
            } else {
                setSnackbar({ open: true, severity: 'error', message: 'Operazione non riuscita. Riprova.' });
            }
        } finally {
            setActingId(null);
            setRejectTarget(null);
        }
    };

    return (
        <Paper elevation={0} className="admin-panel">
            <Typography variant="h6" className="admin-panel__title">
                Prenotazioni da confermare
            </Typography>
            <Typography variant="body2" className="admin-panel__subtitle">
                Una volta confermata, la visita compare nella dashboard dei medici del reparto.
            </Typography>

            {loading && <CircularProgress size={28} />}
            {fetchError && <Alert severity="error">{fetchError}</Alert>}

            {!loading && !fetchError && (
                <TableContainer className="admin-panel__table-container">
                    <Table size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell>Data visita</TableCell>
                                <TableCell>Reparto</TableCell>
                                <TableCell>Paziente</TableCell>
                                <TableCell>Codice fiscale</TableCell>
                                <TableCell>Telefono</TableCell>
                                <TableCell align="right">Azione</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {appointments.map(a => (
                                <TableRow key={a.id}>
                                    <TableCell>{dayjs(a.appointmentDateTime).format('DD/MM/YYYY HH:mm')}</TableCell>
                                    <TableCell>{deptLabels[a.medicalExaminationType] || a.medicalExaminationType}</TableCell>
                                    <TableCell>{a.patientName} {a.patientSurname}</TableCell>
                                    <TableCell>{a.patientTaxIdentificationNumber}</TableCell>
                                    <TableCell>{a.patientTelephoneNumber}</TableCell>
                                    <TableCell align="right">
                                        <Box className="admin-panel__row-actions">
                                            <Button
                                                size="small"
                                                variant="contained"
                                                startIcon={actingId === a.id ? <CircularProgress size={14} color="inherit" /> : <CheckIcon fontSize="small" />}
                                                disabled={actingId === a.id}
                                                onClick={() => handleAction(a.id, 'confirm')}
                                            >
                                                Conferma
                                            </Button>
                                            <Button
                                                size="small"
                                                variant="outlined"
                                                color="error"
                                                startIcon={<CloseIcon fontSize="small" />}
                                                disabled={actingId === a.id}
                                                onClick={() => setRejectTarget(a)}
                                            >
                                                Rifiuta
                                            </Button>
                                        </Box>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {appointments.length === 0 && (
                                <TableRow>
                                    <TableCell colSpan={6} className="admin-panel__empty-row">
                                        Nessuna prenotazione in attesa.
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={!!rejectTarget} onClose={() => setRejectTarget(null)}>
                <DialogTitle>Rifiutare la prenotazione?</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {rejectTarget && `${rejectTarget.patientName} ${rejectTarget.patientSurname}, ${dayjs(rejectTarget.appointmentDateTime).format('DD/MM/YYYY [alle] HH:mm')}. Il paziente riceverà una email e lo slot tornerà disponibile.`}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setRejectTarget(null)}>Annulla</Button>
                    <Button color="error" variant="contained" onClick={() => handleAction(rejectTarget.id, 'reject')}>
                        Rifiuta prenotazione
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={closeSnackbar}>
                <Alert onClose={closeSnackbar} severity={snackbar.severity}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Paper>
    );
}

export default PendingAppointmentsPanel;
