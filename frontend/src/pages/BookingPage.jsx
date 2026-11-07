import {
    Container,
    Typography,
    TextField,
    Button,
    Box,
    Paper,
    Chip,
    Stepper,
    Step,
    StepLabel,
    DialogTitle,
    Dialog,
    DialogContent, DialogContentText, DialogActions,
    Alert
} from '@mui/material';
import { useState, useEffect, useRef } from 'react';
import Navbar from "../components/Navbar.jsx";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";
import {useNavigate} from "react-router-dom";
import { getDepartments } from '../api/departments.js';
import { getBookedDates, getAvailableTimes, createAppointment } from '../api/appointments.js';
import '../styles/BookingPage.css';

const STEP_LABELS = ['Reparto', 'Data e ora', 'I tuoi dati'];
const DAY_ABBR = { 1: 'Lun', 2: 'Mar', 3: 'Mer', 4: 'Gio', 5: 'Ven', 6: 'Sab', 7: 'Dom' };

function formatDaysSummary(daysOfWeek) {
    if (!daysOfWeek || daysOfWeek.length === 0 || daysOfWeek.length === 7) return 'Tutti i giorni';
    return [...daysOfWeek].sort((a, b) => a - b).map(d => DAY_ABBR[d]).join(', ');
}

function formatTimeRange(openTime, closeTime) {
    return `${(openTime || '').slice(0, 5)} – ${(closeTime || '').slice(0, 5)}`;
}

function BookingPage() {

    const navigate = useNavigate();

    const [activeStep, setActiveStep] = useState(0);

    const [formData, setFormData] = useState({
        taxIdentificationNumber: '',
        name: '',
        surname: '',
        email: '',
        telephoneNumber: '',
        selectedMedicalExaminationType: '',
        selectedDate: '',
        selectedTime: ''
    });

    const [medicalExaminationTypes, setMedicalExaminationTypes] = useState([]);
    const [bookedDates, setBookedDates] = useState([]);
    const [availableTimes, setAvailableTimes] = useState([]);
    const [availableDaysOfWeek, setAvailableDaysOfWeek] = useState([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [loadError, setLoadError] = useState(null);
    const [submitError, setSubmitError] = useState(null);
    const submitErrorRef = useRef(null);

    useEffect(() => {
        if (submitError) submitErrorRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, [submitError]);

    useEffect(() => {
        getDepartments()
            .then(setMedicalExaminationTypes)
            .catch(() => setLoadError('Impossibile caricare i reparti. Riprova più tardi.'));
    }, []);

    const selectedType = medicalExaminationTypes.find(
        type => type.name === formData.selectedMedicalExaminationType
    );

    const handleSelectDepartment = (type) => {
        setFormData(prev => ({
            ...prev,
            selectedMedicalExaminationType: type.name,
            selectedDate: '',
            selectedTime: ''
        }));
        setAvailableDaysOfWeek(type.daysOfWeek || []);

        setLoadError(null);
        getBookedDates(type.name)
            .then(setBookedDates)
            .catch(() => setLoadError('Impossibile caricare il calendario del reparto.'));
    };

    const handleDataChange = (selectedDateDayjs) => {
        const formattedDate = selectedDateDayjs ? selectedDateDayjs.format('YYYY-MM-DD') : null;

        setFormData(prev => ({ ...prev, selectedDate: formattedDate, selectedTime: '' }));

        setLoadError(null);
        getAvailableTimes(formData.selectedMedicalExaminationType, formattedDate)
            .then(setAvailableTimes)
            .catch(() => setLoadError('Impossibile caricare gli orari disponibili.'));
    };

    const handleSelectTime = (time) => {
        setFormData(prev => ({ ...prev, selectedTime: time }));
    };

    const isDateDisabled = (date) => {
        const dayOfWeek = date.day() === 0 ? 7 : date.day();
        if (availableDaysOfWeek.length > 0 && !availableDaysOfWeek.includes(dayOfWeek)) {
            return true;
        }
        const formattedDate = date.format('YYYY-MM-DD');
        return bookedDates.includes(formattedDate);
    };

    const handleChange = (event) => {
        setFormData(prev => ({ ...prev, [event.target.name]: event.target.value }));
    };

    const goNext = () => setActiveStep(prev => prev + 1);
    const goBack = () => setActiveStep(prev => prev - 1);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const appointmentDateTime = `${formData.selectedDate}T${formData.selectedTime}:00`;

        const requestAppointmentDTO = {
            patient: {
                taxIdentificationNumber: formData.taxIdentificationNumber,
                name: formData.name,
                surname: formData.surname,
                email: formData.email,
                telephoneNumber: formData.telephoneNumber
            },
            medicalExaminationType: formData.selectedMedicalExaminationType,
            appointmentDate: appointmentDateTime
        };

        setSubmitError(null);
        try {
            await createAppointment(requestAppointmentDTO);
            setOpenDialog(true);
        } catch (err) {
            setSubmitError(err.status
                ? 'Prenotazione non riuscita. Controlla i dati inseriti e riprova.'
                : 'Impossibile contattare il server. Riprova più tardi.');
        }
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        navigate('/');
    };

    const isStep1Valid = !!formData.selectedMedicalExaminationType;
    const isStep2Valid = !!formData.selectedDate && !!formData.selectedTime;
    const isStep3Valid = formData.taxIdentificationNumber && formData.name
        && formData.surname && formData.email && formData.telephoneNumber;

    return (
        <>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <Navbar />
                <Container maxWidth="md" className="booking-page">
                    <Paper elevation={0} className="booking-card">

                        <Typography variant="h5" className="booking-card__title">
                            Prenota una Visita Medica
                        </Typography>
                        <Typography variant="body2" className="booking-card__subtitle">
                            Tre passaggi: reparto, data e ora, i tuoi dati.
                        </Typography>

                        {loadError && (
                            <Alert severity="error" className="booking-alert" onClose={() => setLoadError(null)}>
                                {loadError}
                            </Alert>
                        )}

                        <Stepper activeStep={activeStep} className="booking-stepper">
                            {STEP_LABELS.map(label => (
                                <Step key={label}>
                                    <StepLabel>{label}</StepLabel>
                                </Step>
                            ))}
                        </Stepper>

                        {activeStep === 0 && (
                            <Box className="booking-step">
                                <Box className="booking-dept-grid">
                                    {medicalExaminationTypes.map(type => (
                                        <Paper
                                            key={type.id}
                                            elevation={0}
                                            onClick={() => handleSelectDepartment(type)}
                                            className={`booking-dept-card${formData.selectedMedicalExaminationType === type.name ? ' booking-dept-card--selected' : ''}`}
                                        >
                                            <Typography className="booking-dept-card__name">
                                                {type.description || type.name}
                                            </Typography>
                                            <Typography className="booking-dept-card__meta">
                                                {formatTimeRange(type.openTime, type.closeTime)}
                                            </Typography>
                                            <Typography className="booking-dept-card__meta">
                                                {formatDaysSummary(type.daysOfWeek)}
                                            </Typography>
                                            <Typography className="booking-dept-card__meta">
                                                Visita da {type.examinationDuration} min
                                            </Typography>
                                        </Paper>
                                    ))}
                                </Box>
                                <Box className="booking-step__actions">
                                    <Button
                                        variant="contained"
                                        disabled={!isStep1Valid}
                                        onClick={goNext}
                                        className="booking-step__next"
                                    >
                                        Continua
                                    </Button>
                                </Box>
                            </Box>
                        )}

                        {activeStep === 1 && (
                            <Box className="booking-step">
                                <DatePicker
                                    label="Seleziona Data"
                                    value={formData.selectedDate ? dayjs(formData.selectedDate) : null}
                                    onChange={handleDataChange}
                                    disablePast
                                    shouldDisableDate={isDateDisabled}
                                    format="DD/MM/YYYY"
                                    slotProps={{ textField: { fullWidth: true } }}
                                    className="booking-step__date"
                                />

                                {formData.selectedDate && (
                                    <Box className="booking-time-grid">
                                        {availableTimes.length === 0 ? (
                                            <Typography className="booking-time-empty">
                                                Nessun orario disponibile per questa data.
                                            </Typography>
                                        ) : availableTimes.map((time, index) => (
                                            <Chip
                                                key={index}
                                                label={time}
                                                clickable
                                                onClick={() => handleSelectTime(time)}
                                                className={`booking-time-chip${formData.selectedTime === time ? ' booking-time-chip--selected' : ''}`}
                                            />
                                        ))}
                                    </Box>
                                )}

                                <Box className="booking-step__actions booking-step__actions--split">
                                    <Button onClick={goBack} className="booking-step__back">Indietro</Button>
                                    <Button
                                        variant="contained"
                                        disabled={!isStep2Valid}
                                        onClick={goNext}
                                        className="booking-step__next"
                                    >
                                        Continua
                                    </Button>
                                </Box>
                            </Box>
                        )}

                        {activeStep === 2 && (
                            <Box component="form" onSubmit={handleSubmit} className="booking-step">
                                <Box className="booking-summary">
                                    <Typography className="booking-summary__item booking-summary__item--strong">
                                        {selectedType?.description || formData.selectedMedicalExaminationType}
                                    </Typography>
                                    <Typography className="booking-summary__item">
                                        {dayjs(formData.selectedDate).format('DD/MM/YYYY')} alle {formData.selectedTime}
                                    </Typography>
                                </Box>

                                <TextField
                                    label="Codice Fiscale"
                                    name="taxIdentificationNumber"
                                    value={formData.taxIdentificationNumber}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                />

                                <Box className="booking-form__row">
                                    <TextField label="Nome" name="name" value={formData.name} onChange={handleChange} fullWidth required />
                                    <TextField label="Cognome" name="surname" value={formData.surname} onChange={handleChange} fullWidth required />
                                </Box>

                                <Box className="booking-form__row">
                                    <TextField label="Email" type="email" name="email" value={formData.email} onChange={handleChange} fullWidth required />
                                    <TextField label="Telefono" type="tel" name="telephoneNumber" value={formData.telephoneNumber} onChange={handleChange} fullWidth required />
                                </Box>

                                {submitError && (
                                    <Alert severity="error" className="booking-submit-error" ref={submitErrorRef}>{submitError}</Alert>
                                )}
                                <Box className="booking-step__actions booking-step__actions--split">
                                    <Button onClick={goBack} className="booking-step__back">Indietro</Button>
                                    <Button
                                        type="submit"
                                        variant="contained"
                                        disabled={!isStep3Valid}
                                        className="booking-form__submit"
                                    >
                                        Invia richiesta
                                    </Button>
                                </Box>
                            </Box>
                        )}

                    </Paper>
                </Container>

                <Dialog open={openDialog} onClose={handleCloseDialog} disableEscapeKeyDown>
                    <DialogTitle className="booking-dialog__title">
                        Richiesta di prenotazione inviata
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Gentile {formData.name}, abbiamo ricevuto la tua richiesta per il giorno {dayjs(formData.selectedDate).format('DD/MM/YYYY')} alle ore {formData.selectedTime}: la visita è in attesa di conferma da parte del centro.
                            <br /><br />
                            Riceverai un'email all'indirizzo {formData.email} non appena sarà confermata.
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog} color="primary" variant="contained">
                            Chiudi e torna alla Home
                        </Button>
                    </DialogActions>
                </Dialog>
            </LocalizationProvider>
        </>
    );
}

export default BookingPage;
