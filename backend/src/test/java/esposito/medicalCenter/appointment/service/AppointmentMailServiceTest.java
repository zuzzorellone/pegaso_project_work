package esposito.medicalCenter.appointment.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AppointmentMailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final AppointmentMailService service = new AppointmentMailService(mailSender);

    private MimeMessage sent() throws Exception {
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        return captor.getValue();
    }

    private String body(MimeMessage message) throws Exception {
        message.saveChanges();
        return (String) message.getContent();
    }

    @Test
    void bookingEmailShowsDepartmentDateInItalianAndBrand() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage(Session.getInstance(new Properties())));

        service.sendBookingConfirmationEmail("marco@example.com", "Marco Rossi", "Dermatologia",
                LocalDateTime.of(2026, 10, 21, 9, 20));

        MimeMessage message = sent();
        assertThat(message.getSubject()).isEqualTo("Richiesta di prenotazione ricevuta");
        assertThat(body(message))
                .contains("Gentile Marco Rossi,")
                .contains("Dermatologia")
                .contains("mercoledì 21 ottobre 2026, ore 09:20")
                .contains("Salute e Benessere Lazio")
                .doesNotContain("Il Centro Medico");
    }

    @Test
    void confirmedAndRejectedEmailsUseTheSameLayout() throws Exception {
        when(mailSender.createMimeMessage())
                .thenReturn(new MimeMessage(Session.getInstance(new Properties())))
                .thenReturn(new MimeMessage(Session.getInstance(new Properties())));
        LocalDateTime when = LocalDateTime.of(2026, 10, 21, 9, 20);

        service.sendAppointmentConfirmedEmail("a@example.com", "A B", "Cardiologia", when);
        service.sendAppointmentRejectedEmail("a@example.com", "A B", "Cardiologia", when);

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(2)).send(captor.capture());
        for (MimeMessage message : captor.getAllValues()) {
            assertThat(body(message)).contains("Cardiologia").contains("Salute e Benessere Lazio");
        }
    }
}
