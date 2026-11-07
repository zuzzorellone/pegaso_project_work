import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Container, Typography, Box, Button, Paper, Chip, IconButton,
    Dialog, DialogTitle, DialogContent, DialogActions,
    Alert, CircularProgress, Divider, Snackbar, Tooltip
} from '@mui/material';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import DownloadIcon from '@mui/icons-material/Download';
import SendIcon from '@mui/icons-material/Send';
import LogoutIcon from '@mui/icons-material/Logout';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import PersonIcon from '@mui/icons-material/Person';
import HowToRegIcon from '@mui/icons-material/HowToReg';
import Navbar from '../components/Navbar.jsx';
import dayjs from 'dayjs';
import { getDepartmentLabels } from '../api/departments.js';
import {
    getUpcomingAppointments, getPastAppointments, confirmAttendance, uploadReport, resendReport, downloadReport
} from '../api/staff.js';
import { clearSession } from '../api/session.js';
import '../styles/DoctorDashboardPage.css';

const STATUS_LABELS = {
    CONFIRMED: 'Confermata',
    WAITING_FOR_REPORT: 'In attesa di referto',
    COMPLETED: 'Completata'
};

const STATUS_CHIP_MODIFIER = {
    CONFIRMED: 'confirmed',
    WAITING_FOR_REPORT: 'waiting',
    COMPLETED: 'completed'
};

const canMarkAttendance = (appointment) =>
    appointment.status === 'CONFIRMED' && !dayjs(appointment.appointmentDateTime).isAfter(dayjs(), 'day');

const canUploadReport = (appointment) =>
    appointment.status === 'WAITING_FOR_REPORT' || appointment.status === 'COMPLETED';

function AppointmentCard({ appointment, deptLabels, onUploadClick, onDownloadClick, onResendClick, onAttendanceClick, attendanceLoading }) {
    const isPast = !!onUploadClick;
    const hasReport = !!appointment.hasReport;
    return (
        <Paper elevation={0} className={`appt-card${isPast ? ' appt-card--past' : ''}`}>
            <Box className="appt-card__header">
                <Box className="appt-card__info">
                    <Chip
                        label={deptLabels[appointment.medicalExaminationType] || appointment.medicalExaminationType}
                        size="small"
                        className={`appt-card__type-chip${isPast ? ' appt-card__type-chip--past' : ''}`}
                    />
                    <Typography className="appt-card__datetime">
                        <CalendarTodayIcon className="appt-card__datetime-icon" />
                        {dayjs(appointment.appointmentDateTime).format('DD/MM/YYYY [alle] HH:mm')}
                    </Typography>
                    {STATUS_LABELS[appointment.status] && (
                        <Chip
                            label={STATUS_LABELS[appointment.status]}
                            size="small"
                            className={`appt-card__status-chip appt-card__status-chip--${STATUS_CHIP_MODIFIER[appointment.status]}`}
                        />
                    )}
                    {isPast && (
                        <Chip
                            label={hasReport ? (appointment.reportFilename ? `Referto caricato: ${appointment.reportFilename}` : 'Referto caricato') : 'Nessun referto'}
                            size="small"
                            className={`appt-card__report-chip${hasReport ? ' appt-card__report-chip--uploaded' : ''}`}
                        />
                    )}
                </Box>
                {(isPast || canMarkAttendance(appointment)) && (
                    <Box className="appt-card__actions">
                        {canMarkAttendance(appointment) && (
                            <Button
                                variant="outlined"
                                size="small"
                                startIcon={attendanceLoading ? <CircularProgress size={14} color="inherit" /> : <HowToRegIcon />}
                                disabled={attendanceLoading}
                                onClick={() => onAttendanceClick(appointment.id)}
                                className="appt-card__attendance-btn"
                            >
                                Conferma presenza
                            </Button>
                        )}
                        {isPast && hasReport && (
                            <>
                                <IconButton
                                    size="small"
                                    onClick={() => onDownloadClick(appointment.id)}
                                    className="appt-card__icon-btn"
                                    aria-label="Scarica referto"
                                >
                                    <DownloadIcon fontSize="small" />
                                </IconButton>
                                <IconButton
                                    size="small"
                                    onClick={() => onResendClick(appointment.id)}
                                    className="appt-card__icon-btn"
                                    aria-label="Reinvia email al paziente"
                                >
                                    <SendIcon fontSize="small" />
                                </IconButton>
                            </>
                        )}
                        {isPast && (
                            <Tooltip title={canUploadReport(appointment) ? '' : 'Conferma prima la presenza del paziente'}>
                                <span>
                                    <Button
                                        variant="contained"
                                        size="small"
                                        startIcon={<UploadFileIcon />}
                                        onClick={() => onUploadClick(appointment.id, hasReport)}
                                        disabled={!canUploadReport(appointment)}
                                        className="appt-card__upload-btn"
                                    >
                                        {hasReport ? 'Sostituisci Referto' : 'Carica Referto'}
                                    </Button>
                                </span>
                            </Tooltip>
                        )}
                    </Box>
                )}
            </Box>
            <Divider className="appt-card__divider" />
            <Box className="appt-card__patient">
                <PersonIcon className="appt-card__patient-icon" />
                <Box>
                    <Typography className="appt-card__patient-name">
                        {appointment.patientName} {appointment.patientSurname}
                    </Typography>
                    <Typography className="appt-card__patient-phone">
                        Tel: {appointment.patientTelephoneNumber}
                    </Typography>
                    <Typography className="appt-card__patient-cf">
                        CF: {appointment.patientTaxIdentificationNumber}
                    </Typography>
                </Box>
            </Box>
        </Paper>
    );
}

function DoctorDashboardPage() {
    const navigate = useNavigate();
    const [upcomingAppointments, setUpcomingAppointments] = useState([]);
    const [pastAppointments, setPastAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [deptLabels, setDeptLabels] = useState({});

    useEffect(() => {
        getDepartmentLabels().then(setDeptLabels).catch(() => {});
    }, []);

    const [uploadState, setUploadState] = useState({
        open: false,
        appointmentId: null,
        hasReport: false,
        file: null,
        uploading: false,
        error: null,
        success: false
    });

    useEffect(() => {
        const flattenStaffAppointment = (item) => ({
            ...item.appointment,
            hasReport: item.hasReport,
            reportFilename: item.reportFilename
        });

        Promise.all([getUpcomingAppointments(), getPastAppointments()])
            .then(([futureData, pastData]) => {
                setUpcomingAppointments(futureData.map(flattenStaffAppointment));
                setPastAppointments(pastData.map(flattenStaffAppointment));
            })
            .catch(() => setFetchError('Errore nel caricamento delle visite.'))
            .finally(() => setLoading(false));
    }, []);

    const [actionSnackbar, setActionSnackbar] = useState({ open: false, severity: 'success', message: '' });

    const closeActionSnackbar = () => setActionSnackbar(prev => ({ ...prev, open: false }));

    const handleLogout = () => {
        clearSession();
        navigate('/login');
    };

    const handleDownloadReport = async (appointmentId) => {
        try {
            const { blob } = await downloadReport(appointmentId);
            window.open(window.URL.createObjectURL(blob), '_blank');
        } catch {
            setActionSnackbar({ open: true, severity: 'error', message: 'Impossibile scaricare il referto.' });
        }
    };

    const handleResendReport = async (appointmentId) => {
        try {
            await resendReport(appointmentId);
            setActionSnackbar({ open: true, severity: 'success', message: 'Email reinviata al paziente con un nuovo link.' });
        } catch {
            setActionSnackbar({ open: true, severity: 'error', message: 'Impossibile reinviare la mail al paziente.' });
        }
    };

    const [attendanceLoadingId, setAttendanceLoadingId] = useState(null);

    const handleAttendance = async (appointmentId) => {
        setAttendanceLoadingId(appointmentId);
        try {
            await confirmAttendance(appointmentId);
            const markAttended = list => list.map(appt =>
                appt.id === appointmentId ? { ...appt, status: 'WAITING_FOR_REPORT' } : appt
            );
            setUpcomingAppointments(markAttended);
            setPastAppointments(markAttended);
            setActionSnackbar({ open: true, severity: 'success', message: 'Presenza confermata: ora puoi caricare il referto.' });
        } catch (err) {
            setActionSnackbar({
                open: true,
                severity: 'error',
                message: err.status === 409
                    ? 'La presenza può essere confermata solo dal giorno della visita.'
                    : 'Impossibile confermare la presenza.'
            });
        } finally {
            setAttendanceLoadingId(null);
        }
    };

    const openUploadDialog = (appointmentId, hasReport = false) => {
        setUploadState({
            open: true,
            appointmentId,
            hasReport,
            file: null,
            uploading: false,
            error: null,
            success: false
        });
    };

    const closeUploadDialog = () => {
        setUploadState(prev => ({ ...prev, open: false }));
    };

    const handleFileChange = (e) => {
        setUploadState(prev => ({ ...prev, file: e.target.files[0] || null }));
    };

    const handleUpload = async () => {
        if (!uploadState.file) return;
        setUploadState(prev => ({ ...prev, uploading: true, error: null }));

        try {
            const created = await uploadReport(uploadState.appointmentId, uploadState.file);
            setUploadState(prev => ({ ...prev, uploading: false, success: true }));
            setPastAppointments(prev => prev.map(appt =>
                appt.id === uploadState.appointmentId
                    ? { ...appt, hasReport: true, reportFilename: created.filename, status: 'COMPLETED' }
                    : appt
            ));
        } catch (err) {
            const messages = {
                403: 'Non hai i permessi per caricare un referto per questo appuntamento.',
                409: 'Conferma prima la presenza del paziente.'
            };
            setUploadState(prev => ({ ...prev, uploading: false, error: messages[err.status] || 'Errore durante il caricamento. Riprova.' }));
        }
    };

    const totalAppointments = upcomingAppointments.length + pastAppointments.length;
    const totalLabel = `${totalAppointments} ${totalAppointments === 1 ? 'visita' : 'visite'} totali`;

    return (
        <>
            <Navbar />
            <Container maxWidth="lg" className="doctor-dashboard">

                <Box className="doctor-dashboard__header">
                    <Box>
                        <Typography variant="h4" className="doctor-dashboard__title">
                            Panoramica Visite
                        </Typography>
                        <Typography variant="body2" className="doctor-dashboard__subtitle">
                            {loading ? '' : totalLabel}
                        </Typography>
                    </Box>
                    <Button
                        variant="outlined"
                        startIcon={<LogoutIcon />}
                        onClick={handleLogout}
                        className="doctor-dashboard__logout"
                    >
                        Esci
                    </Button>
                </Box>

                {loading && (
                    <Box className="doctor-dashboard__loading">
                        <CircularProgress />
                    </Box>
                )}

                {fetchError && <Alert severity="error">{fetchError}</Alert>}

                {!loading && !fetchError && (
                    <>
                        <Box className="doctor-dashboard__section">
                            <Typography className="doctor-dashboard__section-title doctor-dashboard__section-title--upcoming">
                                Visite Programmate ({upcomingAppointments.length})
                            </Typography>
                            {upcomingAppointments.length === 0 ? (
                                <Typography className="doctor-dashboard__empty">
                                    Nessuna visita programmata.
                                </Typography>
                            ) : (
                                <Box className="doctor-dashboard__grid">
                                    {upcomingAppointments.map(appt => (
                                        <AppointmentCard
                                            key={appt.id}
                                            appointment={appt}
                                            deptLabels={deptLabels}
                                            onAttendanceClick={handleAttendance}
                                            attendanceLoading={attendanceLoadingId === appt.id}
                                        />
                                    ))}
                                </Box>
                            )}
                        </Box>

                        <Box className="doctor-dashboard__section">
                            <Typography className="doctor-dashboard__section-title doctor-dashboard__section-title--past">
                                Visite Passate ({pastAppointments.length})
                            </Typography>
                            {pastAppointments.length === 0 ? (
                                <Typography className="doctor-dashboard__empty">
                                    Nessuna visita passata.
                                </Typography>
                            ) : (
                                <Box className="doctor-dashboard__grid">
                                    {pastAppointments.map(appt => (
                                        <AppointmentCard
                                            key={appt.id}
                                            appointment={appt}
                                            deptLabels={deptLabels}
                                            onUploadClick={openUploadDialog}
                                            onDownloadClick={handleDownloadReport}
                                            onResendClick={handleResendReport}
                                            onAttendanceClick={handleAttendance}
                                            attendanceLoading={attendanceLoadingId === appt.id}
                                        />
                                    ))}
                                </Box>
                            )}
                        </Box>
                    </>
                )}
            </Container>

            <Dialog open={uploadState.open} onClose={closeUploadDialog} maxWidth="sm" fullWidth>
                <DialogTitle className="upload-dialog__title">
                    {uploadState.hasReport ? 'Sostituisci Referto Medico' : 'Carica Referto Medico'}
                </DialogTitle>
                <DialogContent>
                    {uploadState.success ? (
                        <Alert severity="success">Referto caricato con successo!</Alert>
                    ) : (
                        <>
                            {uploadState.error && (
                                <Alert severity="error" className="upload-dialog__alert">
                                    {uploadState.error}
                                </Alert>
                            )}
                            <Typography variant="body2" className="upload-dialog__hint">
                                {uploadState.hasReport
                                    ? 'Il referto precedente verrà sostituito e il paziente riceverà una nuova email con un nuovo link di download.'
                                    : 'Seleziona il file PDF del referto da allegare a questa visita. Il paziente riceverà una notifica con il link per scaricarlo.'}
                            </Typography>
                            <Button
                                component="label"
                                variant="outlined"
                                startIcon={<UploadFileIcon />}
                                fullWidth
                                className={`upload-dialog__file-btn${uploadState.file ? ' upload-dialog__file-btn--selected' : ''}`}
                            >
                                {uploadState.file ? uploadState.file.name : 'Seleziona file PDF'}
                                <input type="file" accept=".pdf" hidden onChange={handleFileChange} />
                            </Button>
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={closeUploadDialog} className="upload-dialog__cancel">
                        {uploadState.success ? 'Chiudi' : 'Annulla'}
                    </Button>
                    {!uploadState.success && (
                        <Button
                            onClick={handleUpload}
                            variant="contained"
                            disabled={!uploadState.file || uploadState.uploading}
                            startIcon={
                                uploadState.uploading
                                    ? <CircularProgress size={18} color="inherit" />
                                    : <UploadFileIcon />
                            }
                            className="upload-dialog__submit"
                        >
                            {uploadState.uploading ? 'Caricamento...' : 'Carica'}
                        </Button>
                    )}
                </DialogActions>
            </Dialog>

            <Snackbar open={actionSnackbar.open} autoHideDuration={4000} onClose={closeActionSnackbar}>
                <Alert onClose={closeActionSnackbar} severity={actionSnackbar.severity} className="dashboard-action-alert">
                    {actionSnackbar.message}
                </Alert>
            </Snackbar>
        </>
    );
}

export default DoctorDashboardPage;
