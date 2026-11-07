package esposito.medicalCenter.appointment.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import esposito.medicalCenter.core.mail.MailLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentMailService {

    private static final DateTimeFormatter APPOINTMENT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE d MMMM yyyy', ore' HH:mm", Locale.ITALIAN);

    private final JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(String toEmail, String patientFullName,
                                              String medicalExaminationTypeName, LocalDateTime appointmentDateTime) {
        send(toEmail, "Richiesta di prenotazione ricevuta", patientFullName, """
                <p style="margin:0;">abbiamo ricevuto la tua richiesta di prenotazione:</p>
                %s
                <p>Riceverai una nuova email non appena il centro l'avrà confermata.</p>
                """.formatted(summary(medicalExaminationTypeName, appointmentDateTime)));
    }

    public void sendAppointmentConfirmedEmail(String toEmail, String patientFullName,
                                              String medicalExaminationTypeName, LocalDateTime appointmentDateTime) {
        send(toEmail, "Prenotazione confermata", patientFullName, """
                <p style="margin:0;">la tua visita è stata confermata:</p>
                %s
                <p>Ti aspettiamo.</p>
                """.formatted(summary(medicalExaminationTypeName, appointmentDateTime)));
    }

    public void sendAppointmentRejectedEmail(String toEmail, String patientFullName,
                                             String medicalExaminationTypeName, LocalDateTime appointmentDateTime) {
        send(toEmail, "Prenotazione non confermata", patientFullName, """
                <p style="margin:0;">purtroppo non è stato possibile confermare la tua richiesta di prenotazione:</p>
                %s
                <p>Puoi prenotare un'altra data dal nostro sito.</p>
                """.formatted(summary(medicalExaminationTypeName, appointmentDateTime)));
    }

    private String summary(String medicalExaminationTypeName, LocalDateTime appointmentDateTime) {
        Map<String, String> rows = new LinkedHashMap<>();
        rows.put("Visita", medicalExaminationTypeName);
        rows.put("Quando", appointmentDateTime.format(APPOINTMENT_DATE_TIME_FORMATTER));
        return MailLayout.details(rows);
    }

    private void send(String toEmail, String subject, String patientFullName, String bodyHtml) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(MailLayout.page(patientFullName, bodyHtml), true);
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.error("Failed to send '{}' email to {}: {}", subject, toEmail, e.getMessage());
        }
    }
}
