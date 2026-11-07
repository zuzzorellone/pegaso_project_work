package esposito.medicalCenter.report.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.report.entity.ReportEntity;
import esposito.medicalCenter.report.repository.ReportRepository;
import esposito.medicalCenter.report.repository.ReportTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    private final ReportRepository reportRepository = mock(ReportRepository.class);
    private final ReportTokenRepository tokenRepository = mock(ReportTokenRepository.class);
    private final AppointmentFacade appointmentFacade = mock(AppointmentFacade.class);
    private final PatientFacade patientFacade = mock(PatientFacade.class);
    private final ReportMailService mailService = mock(ReportMailService.class);

    private final ReportService service =
            new ReportService(reportRepository, tokenRepository, appointmentFacade, patientFacade, mailService);

    @Test
    void reportIsNullWhenTheAppointmentHasNoReport() {
        when(reportRepository.findByAppointmentId(10L)).thenReturn(Optional.empty());

        assertNull(service.getReportForAppointmentId(10L));
    }

    @Test
    void reportIsReturnedWhenTheAppointmentHasOne() {
        ReportEntity report = new ReportEntity();
        report.setId(1L);
        report.setAppointmentId(10L);
        report.setOriginalFilename("referto.pdf");
        when(reportRepository.findByAppointmentId(10L)).thenReturn(Optional.of(report));

        assertEquals("referto.pdf", service.getReportForAppointmentId(10L).getFilename());
    }

    @Test
    void generatingATokenReturnsANewToken() {
        ReportEntity report = new ReportEntity();
        report.setId(1L);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(tokenRepository.findByReportId(1L)).thenReturn(Optional.empty());

        String token = service.generateAndSaveTokenForReport(1L);

        assertNotNull(token);
    }

    @Test
    void generatingATokenForAMissingReportFails() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.generateAndSaveTokenForReport(1L));
    }

    @Test
    void resendWithoutReportFails() {
        when(reportRepository.findByAppointmentId(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.resendReportNotification(10L));
    }
}
