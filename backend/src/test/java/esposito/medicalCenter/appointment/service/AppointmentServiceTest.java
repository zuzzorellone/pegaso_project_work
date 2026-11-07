package esposito.medicalCenter.appointment.service;

import esposito.medicalCenter.appointment.entity.AppointmentEntity;
import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import esposito.medicalCenter.appointment.repository.AppointmentRepository;
import esposito.medicalCenter.appointment.repository.MedicalExaminationTypeRepository;
import esposito.medicalCenter.patient.api.PatientFacade;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final MedicalExaminationTypeRepository typeRepository = mock(MedicalExaminationTypeRepository.class);
    private final PatientFacade patientFacade = mock(PatientFacade.class);
    private final AppointmentMailService mailService = mock(AppointmentMailService.class);

    private final AppointmentService service =
            new AppointmentService(appointmentRepository, typeRepository, patientFacade, mailService);

    // Cardiology is open from 09:00 to 11:00 with 30 minute visits: 09:00, 09:30, 10:00, 10:30
    private MedicalExaminationTypeEntity createCardiology() {
        MedicalExaminationTypeEntity cardiology = new MedicalExaminationTypeEntity();
        cardiology.setName("CARDIOLOGY");
        cardiology.setOpenTime(LocalTime.of(9, 0));
        cardiology.setCloseTime(LocalTime.of(11, 0));
        cardiology.setExaminationDuration(30);
        return cardiology;
    }

    private AppointmentEntity createAppointment(LocalDateTime date) {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setAppointmentDate(date);
        return appointment;
    }

    @Test
    void freeTimesDoNotIncludeTheBookedOnes() {
        LocalDate day = LocalDate.of(2026, 12, 1);
        when(typeRepository.findMedicalExaminationTypeEntityByName("CARDIOLOGY"))
                .thenReturn(Optional.of(createCardiology()));
        when(appointmentRepository.findByAppointmentDateBetweenAndExaminationTypeName(
                day.atStartOfDay(), day.atTime(LocalTime.MAX), "CARDIOLOGY"))
                .thenReturn(List.of(createAppointment(day.atTime(9, 30)), createAppointment(day.atTime(10, 30))));

        List<String> freeTimes = service.getOccupiedTimesForDate("2026-12-01", "CARDIOLOGY");

        assertEquals(List.of("09:00", "10:00"), freeTimes);
    }

    @Test
    void bookedDatesWithUnknownExaminationTypeFails() {
        when(typeRepository.findMedicalExaminationTypeEntityByName("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getBookedDates("UNKNOWN"));
    }
}
