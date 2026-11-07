package esposito.medicalCenter.appointment.service;

import esposito.medicalCenter.appointment.AppointmentStatus;
import esposito.medicalCenter.appointment.entity.AppointmentEntity;
import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import esposito.medicalCenter.appointment.repository.AppointmentRepository;
import esposito.medicalCenter.appointment.repository.MedicalExaminationTypeRepository;
import esposito.medicalCenter.core.exception.InvalidAppointmentStatusException;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentFacadeImplTest {

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final MedicalExaminationTypeRepository typeRepository = mock(MedicalExaminationTypeRepository.class);
    private final PatientFacade patientFacade = mock(PatientFacade.class);
    private final AppointmentMailService mailService = mock(AppointmentMailService.class);

    private final AppointmentFacadeImpl facade =
            new AppointmentFacadeImpl(appointmentRepository, typeRepository, patientFacade, mailService);

    // Creates appointment 10 of patient 5 in the dermatology department (id 3)
    private AppointmentEntity createAppointment(AppointmentStatus status, LocalDateTime date) {
        MedicalExaminationTypeEntity dermatology = new MedicalExaminationTypeEntity();
        dermatology.setId(3L);
        dermatology.setName("DERMATOLOGY");
        dermatology.setDescription("Dermatologia");

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(10L);
        appointment.setPatientId(5L);
        appointment.setAppointmentStatus(status);
        appointment.setAppointmentDate(date);
        appointment.setMedicalExaminationType(dermatology);

        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));
        return appointment;
    }

    @Test
    void missingAppointmentIsNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> facade.existsById(1L));
    }

    @Test
    void returnsThePatientAndTheDepartmentOfTheAppointment() {
        createAppointment(AppointmentStatus.CONFIRMED, LocalDateTime.now());

        assertEquals(5L, facade.getPatientIdByAppointment(10L));
        assertEquals(3L, facade.getMedicalExaminationTypeIdByAppointmentId(10L));
    }

    @Test
    void confirmingAnAppointmentEmailsThePatient() {
        LocalDateTime date = LocalDateTime.of(2026, 12, 1, 9, 0);
        createAppointment(AppointmentStatus.WAITING_FOR_CONFIRMATION, date);
        when(appointmentRepository.updateStatusIfCurrent(
                10L, AppointmentStatus.WAITING_FOR_CONFIRMATION, AppointmentStatus.CONFIRMED)).thenReturn(1);
        when(patientFacade.getPatientById(5L)).thenReturn(
                new PatientIntegrationDTO(5L, "Marco", "Rossi", "marco@example.com", "333", "RSSMRC85C15F205K"));

        facade.confirmAppointment(10L);

        verify(mailService).sendAppointmentConfirmedEmail("marco@example.com", "Marco Rossi", "Dermatologia", date);
    }

    @Test
    void uploadingTheReportCompletesTheAppointment() {
        AppointmentEntity appointment =
                createAppointment(AppointmentStatus.WAITING_FOR_REPORT, LocalDateTime.now().minusDays(1));

        facade.markReportUploaded(10L);

        assertEquals(AppointmentStatus.COMPLETED, appointment.getAppointmentStatus());
    }

    @Test
    void reportCannotBeUploadedBeforeThePatientAttended() {
        createAppointment(AppointmentStatus.CONFIRMED, LocalDateTime.now());

        assertThrows(InvalidAppointmentStatusException.class, () -> facade.markReportUploaded(10L));
    }
}
