import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Container, Typography, Box, Button, Paper, TextField, MenuItem,
    Alert, CircularProgress, Tabs, Tab, Table, TableHead, TableBody,
    TableRow, TableCell, TableContainer, Snackbar, Switch, Chip,
    FormControlLabel, Checkbox, Dialog, DialogTitle, DialogContent, DialogActions,
    useMediaQuery
} from '@mui/material';
import { BarChart } from '@mui/x-charts/BarChart';
import LogoutIcon from '@mui/icons-material/Logout';
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import SendIcon from '@mui/icons-material/Send';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import EditIcon from '@mui/icons-material/Edit';
import Navbar from '../components/Navbar.jsx';
import PendingAppointmentsPanel from '../components/PendingAppointmentsPanel.jsx';
import dayjs from 'dayjs';
import {
    getDoctors, setDoctorEnabled, getSentReports, resendReportEmail, getStats
} from '../api/admin.js';
import { getDepartments, getDepartmentLabels, createDepartment, updateDepartment } from '../api/departments.js';
import { registerDoctor } from '../api/auth.js';
import { clearSession } from '../api/session.js';
import '../styles/AdminDashboardPage.css';


const emptyDoctorForm = {
    name: '', surname: '', email: '', password: '', medicalExaminationTypeId: ''
};

const DAY_OPTIONS = [
    { value: 'MONDAY', iso: 1, label: 'Lun' },
    { value: 'TUESDAY', iso: 2, label: 'Mar' },
    { value: 'WEDNESDAY', iso: 3, label: 'Mer' },
    { value: 'THURSDAY', iso: 4, label: 'Gio' },
    { value: 'FRIDAY', iso: 5, label: 'Ven' },
    { value: 'SATURDAY', iso: 6, label: 'Sab' },
    { value: 'SUNDAY', iso: 7, label: 'Dom' }
];

const emptyDepartmentForm = {
    name: '', description: '', duration: '', openTime: '', closeTime: '',
    daysOfWeek: DAY_OPTIONS.map(d => d.value), active: true
};

function isoDaysToNames(isoDays) {
    return DAY_OPTIONS.filter(d => isoDays?.includes(d.iso)).map(d => d.value);
}

function DoctorRegistrationPanel() {
    const [medicalExaminationTypes, setMedicalExaminationTypes] = useState([]);
    const deptLabelByName = Object.fromEntries(
        medicalExaminationTypes.map(type => [type.name, type.description || type.name])
    );
    const [formData, setFormData] = useState(emptyDoctorForm);
    const [submitting, setSubmitting] = useState(false);
    const [feedback, setFeedback] = useState(null);

    const [doctors, setDoctors] = useState([]);
    const [doctorsLoading, setDoctorsLoading] = useState(true);
    const [doctorsError, setDoctorsError] = useState(null);
    const [togglingId, setTogglingId] = useState(null);
    const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

    const fetchDoctors = useCallback(() => {
        setDoctorsLoading(true);
        getDoctors()
            .then(setDoctors)
            .catch(() => setDoctorsError('Errore nel caricamento dei medici.'))
            .finally(() => setDoctorsLoading(false));
    }, []);

    useEffect(() => {
        getDepartments().then(setMedicalExaminationTypes).catch(() => {});
    }, []);

    useEffect(() => {
        fetchDoctors();
    }, [fetchDoctors]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setFeedback(null);

        try {
            await registerDoctor(formData);
            setFeedback({ severity: 'success', message: `Dottore ${formData.name} ${formData.surname} censito con successo.` });
            setFormData(emptyDoctorForm);
            fetchDoctors();
        } catch (err) {
            setFeedback({
                severity: 'error',
                message: err.status === 409 ? 'Esiste già un account con questa email.'
                    : err.status ? err.serverMessage || 'Errore durante la registrazione del medico.'
                    : 'Errore di connessione al server.'
            });
        } finally {
            setSubmitting(false);
        }
    };

    const closeSnackbar = () => setSnackbar(prev => ({ ...prev, open: false }));

    const handleToggleActive = async (doctor) => {
        setTogglingId(doctor.id);
        try {
            await setDoctorEnabled(doctor.id, !doctor.active);
            setDoctors(prev => prev.map(d => d.id === doctor.id ? { ...d, active: !d.active } : d));
            setSnackbar({
                open: true,
                severity: 'success',
                message: doctor.active
                    ? `${doctor.name} ${doctor.surname} disabilitato.`
                    : `${doctor.name} ${doctor.surname} riattivato.`
            });
        } catch {
            setSnackbar({ open: true, severity: 'error', message: 'Impossibile aggiornare lo stato del medico.' });
        } finally {
            setTogglingId(null);
        }
    };

    const isValid = formData.name && formData.surname && formData.email
        && formData.password.length >= 8 && formData.medicalExaminationTypeId;

    return (
        <>
            <Paper elevation={0} className="admin-panel">
                <Typography variant="h6" className="admin-panel__title">
                    Censimento Medico
                </Typography>
                <Typography variant="body2" className="admin-panel__subtitle">
                    Registra un nuovo account medico e assegnalo a un reparto.
                </Typography>

                {feedback && (
                    <Alert severity={feedback.severity} className="admin-panel__alert">
                        {feedback.message}
                    </Alert>
                )}

                <Box component="form" onSubmit={handleSubmit} className="admin-panel__form">
                    <TextField label="Nome" name="name" value={formData.name} onChange={handleChange} required fullWidth />
                    <TextField label="Cognome" name="surname" value={formData.surname} onChange={handleChange} required fullWidth />
                    <TextField label="E-mail" name="email" type="email" value={formData.email} onChange={handleChange} required fullWidth />
                    <TextField
                        label="Password"
                        name="password"
                        type="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        fullWidth
                        helperText="Almeno 8 caratteri"
                    />
                    <TextField
                        select
                        label="Reparto"
                        name="medicalExaminationTypeId"
                        value={formData.medicalExaminationTypeId}
                        onChange={handleChange}
                        required
                        fullWidth
                    >
                        {medicalExaminationTypes.map(type => (
                            <MenuItem key={type.id} value={type.id}>{type.description || type.name}</MenuItem>
                        ))}
                    </TextField>

                    <Button
                        type="submit"
                        variant="contained"
                        disabled={!isValid || submitting}
                        startIcon={submitting ? <CircularProgress size={18} color="inherit" /> : <PersonAddAlt1Icon />}
                        className="admin-panel__submit"
                    >
                        {submitting ? 'Registrazione in corso...' : 'Censisci Medico'}
                    </Button>
                </Box>
            </Paper>

            <Paper elevation={0} className="admin-panel">
                <Typography variant="h6" className="admin-panel__title">
                    Medici Censiti
                </Typography>
                <Typography variant="body2" className="admin-panel__subtitle">
                    Un medico disabilitato non può più accedere; lo storico visite/referti resta invariato.
                </Typography>

                {doctorsLoading && <CircularProgress size={28} />}
                {doctorsError && <Alert severity="error">{doctorsError}</Alert>}

                {!doctorsLoading && !doctorsError && (
                    <TableContainer className="admin-panel__table-container">
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Nome</TableCell>
                                    <TableCell>E-mail</TableCell>
                                    <TableCell>Reparto</TableCell>
                                    <TableCell align="right">Attivo</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {doctors.map(doctor => (
                                    <TableRow key={doctor.id}>
                                        <TableCell>{doctor.name} {doctor.surname}</TableCell>
                                        <TableCell>{doctor.email}</TableCell>
                                        <TableCell>{deptLabelByName[doctor.medicalExaminationTypeName] || doctor.medicalExaminationTypeName}</TableCell>
                                        <TableCell align="right">
                                            <Switch
                                                checked={doctor.active}
                                                onChange={() => handleToggleActive(doctor)}
                                                disabled={togglingId === doctor.id}
                                                size="small"
                                            />
                                        </TableCell>
                                    </TableRow>
                                ))}
                                {doctors.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={4} className="admin-panel__empty-row">
                                            Nessun medico censito.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>

            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={closeSnackbar}>
                <Alert onClose={closeSnackbar} severity={snackbar.severity}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </>
    );
}

function DepartmentFormFields({ formData, onChange, onDayToggle }) {
    return (
        <>
            <TextField label="Nome reparto" name="name" value={formData.name} onChange={onChange} required fullWidth />
            <TextField label="Descrizione" name="description" value={formData.description} onChange={onChange} fullWidth />
            <Box className="admin-panel__form-row">
                <TextField
                    label="Durata visita (min)"
                    name="duration"
                    type="number"
                    value={formData.duration}
                    onChange={onChange}
                    required
                    slotProps={{ htmlInput: { min: 5 } }}
                />
                <TextField
                    label="Apertura"
                    name="openTime"
                    type="time"
                    value={formData.openTime}
                    onChange={onChange}
                    required
                    slotProps={{ inputLabel: { shrink: true } }}
                />
                <TextField
                    label="Chiusura"
                    name="closeTime"
                    type="time"
                    value={formData.closeTime}
                    onChange={onChange}
                    required
                    slotProps={{ inputLabel: { shrink: true } }}
                />
            </Box>
            <Box>
                <Typography variant="body2" className="admin-panel__days-label">Giorni attivi</Typography>
                <Box className="admin-panel__days-row">
                    {DAY_OPTIONS.map(day => (
                        <FormControlLabel
                            key={day.value}
                            label={day.label}
                            control={
                                <Checkbox
                                    size="small"
                                    checked={formData.daysOfWeek.includes(day.value)}
                                    onChange={() => onDayToggle(day.value)}
                                />
                            }
                        />
                    ))}
                </Box>
            </Box>
        </>
    );
}

function DepartmentsPanel() {
    const [departments, setDepartments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [formData, setFormData] = useState(emptyDepartmentForm);
    const [submitting, setSubmitting] = useState(false);
    const [feedback, setFeedback] = useState(null);
    const [editState, setEditState] = useState({ open: false, id: null, formData: emptyDepartmentForm, submitting: false, error: null });
    const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });
    const [statusFilter, setStatusFilter] = useState('all');

    const fetchDepartments = useCallback(() => {
        setLoading(true);
        getDepartments()
            .then(setDepartments)
            .catch(() => setFetchError('Errore nel caricamento dei reparti.'))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        fetchDepartments();
    }, [fetchDepartments]);

    const closeSnackbar = () => setSnackbar(prev => ({ ...prev, open: false }));

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleDayToggle = (day) => {
        setFormData(prev => ({
            ...prev,
            daysOfWeek: prev.daysOfWeek.includes(day)
                ? prev.daysOfWeek.filter(d => d !== day)
                : [...prev.daysOfWeek, day]
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setFeedback(null);
        try {
            await createDepartment({
                name: formData.name,
                description: formData.description,
                duration: Number(formData.duration),
                openTime: formData.openTime,
                closeTime: formData.closeTime,
                daysOfWeek: formData.daysOfWeek
            });
            setFeedback({ severity: 'success', message: `Reparto "${formData.name}" creato con successo.` });
            setFormData(emptyDepartmentForm);
            fetchDepartments();
        } catch (err) {
            setFeedback({ severity: 'error', message: err.status ? err.serverMessage || 'Errore durante la creazione del reparto.' : 'Errore di connessione al server.' });
        } finally {
            setSubmitting(false);
        }
    };

    const openEditDialog = (dept) => {
        setEditState({
            open: true,
            id: dept.id,
            submitting: false,
            error: null,
            formData: {
                name: dept.name,
                description: dept.description || '',
                duration: dept.examinationDuration,
                openTime: (dept.openTime || '').slice(0, 5),
                closeTime: (dept.closeTime || '').slice(0, 5),
                daysOfWeek: isoDaysToNames(dept.daysOfWeek),
                active: dept.isActive
            }
        });
    };

    const closeEditDialog = () => setEditState(prev => ({ ...prev, open: false }));

    const handleEditChange = (e) => {
        const { name, value } = e.target;
        setEditState(prev => ({ ...prev, formData: { ...prev.formData, [name]: value } }));
    };

    const handleEditDayToggle = (day) => {
        setEditState(prev => ({
            ...prev,
            formData: {
                ...prev.formData,
                daysOfWeek: prev.formData.daysOfWeek.includes(day)
                    ? prev.formData.daysOfWeek.filter(d => d !== day)
                    : [...prev.formData.daysOfWeek, day]
            }
        }));
    };

    const handleEditSubmit = async (e) => {
        e.preventDefault();
        setEditState(prev => ({ ...prev, submitting: true, error: null }));
        try {
            await updateDepartment(editState.id, {
                name: editState.formData.name,
                description: editState.formData.description,
                active: editState.formData.active,
                duration: Number(editState.formData.duration),
                openTime: editState.formData.openTime,
                closeTime: editState.formData.closeTime,
                daysOfWeek: editState.formData.daysOfWeek
            });
            setSnackbar({ open: true, severity: 'success', message: `Reparto "${editState.formData.name}" aggiornato.` });
            closeEditDialog();
            fetchDepartments();
        } catch (err) {
            setEditState(prev => ({ ...prev, error: err.status ? err.serverMessage || 'Errore durante l\'aggiornamento del reparto.' : 'Errore di connessione al server.' }));
        } finally {
            setEditState(prev => ({ ...prev, submitting: false }));
        }
    };

    const filteredDepartments = departments.filter(dept => {
        if (statusFilter === 'active') return dept.isActive;
        if (statusFilter === 'inactive') return !dept.isActive;
        return true;
    });

    const isCreateValid = formData.name && formData.duration && formData.openTime && formData.closeTime;
    const isEditValid = editState.formData.name && editState.formData.duration && editState.formData.openTime && editState.formData.closeTime;

    return (
        <>
            <Paper elevation={0} className="admin-panel">
                <Typography variant="h6" className="admin-panel__title">
                    Nuovo Reparto
                </Typography>
                <Typography variant="body2" className="admin-panel__subtitle">
                    Definisce orari, durata visita e giorni in cui il reparto è attivo.
                </Typography>

                {feedback && (
                    <Alert severity={feedback.severity} className="admin-panel__alert">
                        {feedback.message}
                    </Alert>
                )}

                <Box component="form" onSubmit={handleSubmit} className="admin-panel__form">
                    <DepartmentFormFields formData={formData} onChange={handleChange} onDayToggle={handleDayToggle} />
                    <Button
                        type="submit"
                        variant="contained"
                        disabled={!isCreateValid || submitting}
                        startIcon={submitting ? <CircularProgress size={18} color="inherit" /> : <AddCircleOutlineIcon />}
                        className="admin-panel__submit"
                    >
                        {submitting ? 'Creazione in corso...' : 'Crea Reparto'}
                    </Button>
                </Box>
            </Paper>

            <Paper elevation={0} className="admin-panel">
                <Typography variant="h6" className="admin-panel__title">
                    Reparti
                </Typography>

                {loading && <CircularProgress size={28} />}
                {fetchError && <Alert severity="error">{fetchError}</Alert>}

                {!loading && !fetchError && (
                    <>
                        <Box className="admin-panel__filters">
                            <TextField
                                select
                                label="Stato"
                                value={statusFilter}
                                onChange={(e) => setStatusFilter(e.target.value)}
                                className="admin-panel__filter-select"
                            >
                                <MenuItem value="all">Tutti</MenuItem>
                                <MenuItem value="active">Attivi</MenuItem>
                                <MenuItem value="inactive">Disattivi</MenuItem>
                            </TextField>
                        </Box>

                        <TableContainer className="admin-panel__table-container">
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Nome</TableCell>
                                    <TableCell>Orario</TableCell>
                                    <TableCell>Durata</TableCell>
                                    <TableCell>Giorni</TableCell>
                                    <TableCell>Stato</TableCell>
                                    <TableCell align="right">Azione</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredDepartments.map(dept => (
                                    <TableRow key={dept.id}>
                                        <TableCell>{dept.name}</TableCell>
                                        <TableCell>{(dept.openTime || '').slice(0, 5)}–{(dept.closeTime || '').slice(0, 5)}</TableCell>
                                        <TableCell>{dept.examinationDuration} min</TableCell>
                                        <TableCell>
                                            {DAY_OPTIONS.filter(d => dept.daysOfWeek?.includes(d.iso)).map(d => d.label).join(', ') || '—'}
                                        </TableCell>
                                        <TableCell>
                                            <Chip
                                                size="small"
                                                label={dept.isActive ? 'Attivo' : 'Disattivo'}
                                                className={`admin-panel__status-chip ${dept.isActive ? 'admin-panel__status-chip--active' : 'admin-panel__status-chip--inactive'}`}
                                            />
                                        </TableCell>
                                        <TableCell align="right">
                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<EditIcon fontSize="small" />}
                                                onClick={() => openEditDialog(dept)}
                                                className="admin-panel__row-action"
                                            >
                                                Modifica
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))}
                                {filteredDepartments.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={6} className="admin-panel__empty-row">
                                            Nessun reparto trovato.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                        </TableContainer>
                    </>
                )}
            </Paper>

            <Dialog open={editState.open} onClose={closeEditDialog} maxWidth="sm" fullWidth>
                <DialogTitle className="department-dialog__title">
                    Modifica Reparto
                </DialogTitle>
                <Box component="form" onSubmit={handleEditSubmit}>
                    <DialogContent>
                        {editState.error && (
                            <Alert severity="error" className="admin-panel__alert">
                                {editState.error}
                            </Alert>
                        )}
                        <Box className="department-dialog__form">
                            <DepartmentFormFields
                                formData={editState.formData}
                                onChange={handleEditChange}
                                onDayToggle={handleEditDayToggle}
                            />
                            <FormControlLabel
                                label="Reparto attivo"
                                control={
                                    <Checkbox
                                        checked={editState.formData.active}
                                        onChange={(e) => setEditState(prev => ({ ...prev, formData: { ...prev.formData, active: e.target.checked } }))}
                                    />
                                }
                            />
                        </Box>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={closeEditDialog} className="department-dialog__cancel">
                            Annulla
                        </Button>
                        <Button
                            type="submit"
                            variant="contained"
                            disabled={!isEditValid || editState.submitting}
                            startIcon={editState.submitting ? <CircularProgress size={18} color="inherit" /> : null}
                            className="department-dialog__submit"
                        >
                            {editState.submitting ? 'Salvataggio...' : 'Salva Modifiche'}
                        </Button>
                    </DialogActions>
                </Box>
            </Dialog>

            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={closeSnackbar}>
                <Alert onClose={closeSnackbar} severity={snackbar.severity}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </>
    );
}

function ReportResendPanel() {
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [departmentFilter, setDepartmentFilter] = useState('');
    const [searchText, setSearchText] = useState('');
    const [resendingId, setResendingId] = useState(null);
    const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });
    const [deptLabels, setDeptLabels] = useState({});

    useEffect(() => {
        getDepartmentLabels().then(setDeptLabels).catch(() => {});
    }, []);

    useEffect(() => {
        getSentReports()
            .then(data => setReports(data.map(item => ({ ...item.appointment, reportFilename: item.reportFilename }))))
            .catch(() => setFetchError('Errore nel caricamento dei referti.'))
            .finally(() => setLoading(false));
    }, []);

    const departments = [...new Set(reports.map(r => r.medicalExaminationType))].sort();

    const filteredReports = reports.filter(r => {
        const matchesDepartment = !departmentFilter || r.medicalExaminationType === departmentFilter;
        const fullName = `${r.patientName} ${r.patientSurname}`.toLowerCase();
        const matchesSearch = !searchText || fullName.includes(searchText.toLowerCase());
        return matchesDepartment && matchesSearch;
    });

    const closeSnackbar = () => setSnackbar(prev => ({ ...prev, open: false }));

    const handleResend = async (appointmentId) => {
        setResendingId(appointmentId);
        try {
            await resendReportEmail(appointmentId);
            setSnackbar({ open: true, severity: 'success', message: 'Email reinviata al paziente con un nuovo link.' });
        } catch {
            setSnackbar({ open: true, severity: 'error', message: 'Impossibile reinviare la mail al paziente.' });
        } finally {
            setResendingId(null);
        }
    };

    return (
        <Paper elevation={0} className="admin-panel">
            <Typography variant="h6" className="admin-panel__title">
                Reinvio Referti
            </Typography>
            <Typography variant="body2" className="admin-panel__subtitle">
                Tutte le visite con referto già inviato, di ogni reparto.
            </Typography>

            {loading && <CircularProgress size={28} />}
            {fetchError && <Alert severity="error">{fetchError}</Alert>}

            {!loading && !fetchError && (
                <>
                    <Box className="admin-panel__filters">
                        <TextField
                            select
                            label="Reparto"
                            value={departmentFilter}
                            onChange={(e) => setDepartmentFilter(e.target.value)}
                            className="admin-panel__filter-select"
                        >
                            <MenuItem value="">Tutti i reparti</MenuItem>
                            {departments.map(dep => (
                                <MenuItem key={dep} value={dep}>{deptLabels[dep] || dep}</MenuItem>
                            ))}
                        </TextField>
                        <TextField
                            label="Cerca paziente"
                            value={searchText}
                            onChange={(e) => setSearchText(e.target.value)}
                            className="admin-panel__filter-search"
                        />
                    </Box>

                    <TableContainer className="admin-panel__table-container">
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Paziente</TableCell>
                                    <TableCell>Data visita</TableCell>
                                    <TableCell>Reparto</TableCell>
                                    <TableCell>Referto</TableCell>
                                    <TableCell align="right">Azione</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredReports.map(r => (
                                    <TableRow key={r.id}>
                                        <TableCell>{r.patientName} {r.patientSurname}</TableCell>
                                        <TableCell>{dayjs(r.appointmentDateTime).format('DD/MM/YYYY HH:mm')}</TableCell>
                                        <TableCell>{deptLabels[r.medicalExaminationType] || r.medicalExaminationType}</TableCell>
                                        <TableCell>{r.reportFilename}</TableCell>
                                        <TableCell align="right">
                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={
                                                    resendingId === r.id
                                                        ? <CircularProgress size={14} color="inherit" />
                                                        : <SendIcon fontSize="small" />
                                                }
                                                onClick={() => handleResend(r.id)}
                                                disabled={resendingId === r.id}
                                                className="admin-panel__resend-btn"
                                            >
                                                Reinvia
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))}
                                {filteredReports.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={5} className="admin-panel__empty-row">
                                            Nessun referto trovato.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </>
            )}

            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={closeSnackbar}>
                <Alert onClose={closeSnackbar} severity={snackbar.severity}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Paper>
    );
}

function truncateLabel(value, maxLength) {
    const text = String(value);
    return text.length > maxLength ? `${text.slice(0, maxLength - 1)}…` : text;
}

function StatBarChart({ title, dataset, categoryKey, label, color, horizontal = false }) {
    const isNarrow = useMediaQuery('(max-width:600px)');
    const effectiveHorizontal = horizontal && !isNarrow;

    const categoryAxis = [{
        dataKey: categoryKey,
        scaleType: 'band',
        width: effectiveHorizontal ? 'auto' : undefined,
        tickLabelStyle: !effectiveHorizontal ? { fontSize: 11 } : undefined,
        valueFormatter: !effectiveHorizontal ? (v) => truncateLabel(v, 8) : undefined
    }];

    return (
        <Paper elevation={0} className="admin-panel">
            <Typography variant="h6" className="admin-panel__title">
                {title}
            </Typography>
            {dataset.length === 0 ? (
                <Typography variant="body2" className="admin-panel__subtitle">
                    Nessun dato disponibile.
                </Typography>
            ) : (
                <BarChart
                    dataset={dataset}
                    layout={effectiveHorizontal ? 'horizontal' : 'vertical'}
                    xAxis={effectiveHorizontal ? undefined : categoryAxis}
                    yAxis={effectiveHorizontal ? categoryAxis : undefined}
                    series={[{ dataKey: 'count', label, color }]}
                    height={effectiveHorizontal ? Math.max(220, dataset.length * 60) : 260}
                    borderRadius={4}
                    grid={{ horizontal: !effectiveHorizontal, vertical: effectiveHorizontal }}
                    slotProps={{ legend: { hidden: true } }}
                />
            )}
        </Paper>
    );
}

const CHART_COLOR = '#5AA8E6';

const TREND_PERIODS = [
    { value: 'week', label: 'Ultima settimana', title: 'Andamento Prenotazioni (ultimi 7 giorni)', format: 'DD/MM' },
    { value: 'month', label: 'Ultimo mese', title: 'Andamento Prenotazioni (ultimi 30 giorni)', format: 'DD/MM' },
    { value: 'year', label: 'Ultimo anno', title: 'Andamento Prenotazioni (ultimi 12 mesi)', format: 'MMM' }
];

function formatTrendDataset(period, dataset) {
    const config = TREND_PERIODS.find(p => p.value === period);
    const parse = period === 'year' ? (label) => dayjs(`${label}-01`) : (label) => dayjs(label);
    return dataset.map(point => ({ ...point, label: parse(point.label).format(config.format) }));
}

function StatsPanel() {
    const isNarrow = useMediaQuery('(max-width:600px)');

    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [period, setPeriod] = useState('year');
    const [deptLabels, setDeptLabels] = useState({});

    useEffect(() => {
        getDepartmentLabels().then(setDeptLabels).catch(() => {});
    }, []);

    const fetchStats = useCallback(() => {
        getStats(period)
            .then(setStats)
            .catch(() => setFetchError('Errore nel caricamento delle statistiche.'))
            .finally(() => setLoading(false));
    }, [period]);

    useEffect(() => {
        fetchStats();
    }, [fetchStats]);

    if (loading && !stats) return <CircularProgress size={28} />;
    if (fetchError) return <Alert severity="error">{fetchError}</Alert>;

    const trendConfig = TREND_PERIODS.find(p => p.value === period);
    const withDeptLabels = (dataset) => dataset.map(point => ({
        ...point,
        department: deptLabels[point.department] || point.department
    }));

    return (
        <Box className="stats-grid">
            <StatBarChart
                title="Visite per Reparto"
                dataset={withDeptLabels(stats.appointmentsByDepartment)}
                categoryKey="department"
                label="Visite"
                color={CHART_COLOR}
                horizontal
            />
            <StatBarChart
                title="Referti in Sospeso per Reparto"
                dataset={withDeptLabels(stats.pendingReportsByDepartment)}
                categoryKey="department"
                label="Referti in sospeso"
                color={CHART_COLOR}
                horizontal
            />
            <StatBarChart
                title="Medici Attivi per Reparto"
                dataset={withDeptLabels(stats.activeDoctorsByDepartment)}
                categoryKey="department"
                label="Medici"
                color={CHART_COLOR}
                horizontal
            />
            <Paper elevation={0} className="admin-panel">
                <Box className="admin-panel__trend-header">
                    <Typography variant="h6" className="admin-panel__title admin-panel__trend-title">
                        {trendConfig.title}
                    </Typography>
                    <TextField
                        select
                        size="small"
                        value={period}
                        onChange={(e) => { setPeriod(e.target.value); setLoading(true); }}
                        disabled={loading}
                        className="admin-panel__filter-select"
                    >
                        {TREND_PERIODS.map(p => (
                            <MenuItem key={p.value} value={p.value}>{p.label}</MenuItem>
                        ))}
                    </TextField>
                </Box>
                {stats.appointmentsTrend.length === 0 ? (
                    <Typography variant="body2" className="admin-panel__subtitle">
                        Nessun dato disponibile.
                    </Typography>
                ) : (
                    <BarChart
                        dataset={formatTrendDataset(period, stats.appointmentsTrend)}
                        xAxis={[{
                            dataKey: 'label',
                            scaleType: 'band',
                            tickLabelStyle: isNarrow ? { fontSize: 11 } : undefined,
                            tickLabelInterval: isNarrow
                                ? (value, index) => index % Math.ceil(stats.appointmentsTrend.length / 6) === 0
                                : undefined
                        }]}
                        series={[{ dataKey: 'count', label: 'Prenotazioni', color: CHART_COLOR }]}
                        height={260}
                        borderRadius={4}
                        grid={{ horizontal: true }}
                        slotProps={{ legend: { hidden: true } }}
                    />
                )}
            </Paper>
        </Box>
    );
}

function AdminDashboardPage() {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState(0);

    const handleLogout = () => {
        clearSession();
        navigate('/login');
    };

    return (
        <>
            <Navbar />
            <Container maxWidth="lg" className="admin-dashboard">
                <Box className="admin-dashboard__header">
                    <Box>
                        <Typography variant="h4" className="admin-dashboard__title">
                            Dashboard Amministrazione
                        </Typography>
                        <Typography variant="body2" className="admin-dashboard__subtitle">
                            Prenotazioni, medici e referti
                        </Typography>
                    </Box>
                    <Button
                        variant="outlined"
                        startIcon={<LogoutIcon />}
                        onClick={handleLogout}
                        className="admin-dashboard__logout"
                    >
                        Esci
                    </Button>
                </Box>

                <Tabs
                    value={activeTab}
                    onChange={(e, newValue) => setActiveTab(newValue)}
                    className="admin-dashboard__tabs"
                    variant="scrollable"
                    scrollButtons="auto"
                    allowScrollButtonsMobile
                >
                    <Tab label="Prenotazioni" />
                    <Tab label="Statistiche" />
                    <Tab label="Reinvio Referti" />
                    <Tab label="Censimento Medico" />
                    <Tab label="Reparti" />
                </Tabs>

                {activeTab === 0 && <PendingAppointmentsPanel />}
                {activeTab === 1 && <StatsPanel />}
                {activeTab === 2 && <ReportResendPanel />}
                {activeTab === 3 && <DoctorRegistrationPanel />}
                {activeTab === 4 && <DepartmentsPanel />}
            </Container>
        </>
    );
}

export default AdminDashboardPage;
