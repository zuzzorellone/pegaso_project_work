package esposito.medicalCenter.report.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.core.exception.ExpiredTokenException;
import esposito.medicalCenter.core.exception.FailedAttemptsException;
import esposito.medicalCenter.core.exception.GenericReportDownloadException;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import esposito.medicalCenter.report.entity.ReportEntity;
import esposito.medicalCenter.report.entity.ReportTokenEntity;
import esposito.medicalCenter.report.repository.ReportTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicReportServiceTest {

    private final ReportTokenRepository tokenRepository = mock(ReportTokenRepository.class);
    private final PatientFacade patientFacade = mock(PatientFacade.class);
    private final AppointmentFacade appointmentFacade = mock(AppointmentFacade.class);

    private final PublicReportService service =
            new PublicReportService(tokenRepository, patientFacade, appointmentFacade);

    // Creates a token "abc" for the report of appointment 10
    private ReportTokenEntity createToken(LocalDateTime expiration, int failedAttempts) {
        ReportEntity report = new ReportEntity();
        report.setAppointmentId(10L);

        ReportTokenEntity token = new ReportTokenEntity();
        token.setToken("abc");
        token.setExpirationDate(expiration);
        token.setFailedAttempts(failedAttempts);
        token.setReport(report);

        when(tokenRepository.findReportTokenEntityByToken("abc")).thenReturn(Optional.of(token));
        return token;
    }

    @Test
    void unknownTokenIsNotFound() {
        when(tokenRepository.findReportTokenEntityByToken("nope")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.validateTokenAndGetReport("nope", "RSSMRC85C15F205K"));
    }

    @Test
    void expiredTokenIsRejected() {
        createToken(LocalDateTime.now().minusMinutes(1), 0);

        assertThrows(ExpiredTokenException.class,
                () -> service.validateTokenAndGetReport("abc", "RSSMRC85C15F205K"));
    }

    @Test
    void tokenIsBlockedAfterThreeFailedAttempts() {
        createToken(LocalDateTime.now().plusHours(1), 3);

        assertThrows(FailedAttemptsException.class,
                () -> service.validateTokenAndGetReport("abc", "RSSMRC85C15F205K"));
    }

    @Test
    void taxCodeOfAnotherPatientCountsAsAFailedAttempt() {
        ReportTokenEntity token = createToken(LocalDateTime.now().plusHours(1), 0);
        PatientIntegrationDTO otherPatient =
                new PatientIntegrationDTO(5L, "Marco", "Rossi", "m@example.com", "333", "RSSMRC85C15F205K");
        when(patientFacade.getPatientByTaxIdentificationNumber("RSSMRC85C15F205K")).thenReturn(otherPatient);
        when(appointmentFacade.getPatientIdByAppointment(10L)).thenReturn(99L); // the appointment belongs to patient 99

        assertThrows(GenericReportDownloadException.class,
                () -> service.validateTokenAndGetReport("abc", "RSSMRC85C15F205K"));
        assertEquals(1, token.getFailedAttempts());
    }
}
