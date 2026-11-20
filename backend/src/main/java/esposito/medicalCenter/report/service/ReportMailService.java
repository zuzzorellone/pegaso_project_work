package esposito.medicalCenter.report.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import esposito.medicalCenter.core.mail.MailLayout;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportMailService {

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    private final JavaMailSender mailSender;

    public void sendReportAvailableEmail(String toEmail, String patientFullName, String downloadLink, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Il tuo referto medico è pronto per il download");

            String manualDownloadPage = frontendBaseUrl + "/downloadReport";
            String body = """
                    <p style="margin:0;">il referto della tua visita è pronto. Puoi scaricarlo da qui:</p>
                    %s
                    <p style="margin:0 0 8px;">In alternativa puoi aprire la pagina
                    <a href="%s" style="color:#2A74AD;">%s</a> e inserire questo codice:</p>
                    <p style="margin:0 0 16px;padding:10px 16px;background:#EAF4FC;border-radius:8px;font-family:monospace;font-size:15px;font-weight:bold;">%s</p>
                    <p style="margin:0;">Per completare il download ti verrà chiesto il tuo codice fiscale.
                    Il link resta valido per %d ore.</p>
                    """.formatted(MailLayout.button("Scarica il referto", downloadLink),
                    MailLayout.escape(manualDownloadPage), MailLayout.escape(manualDownloadPage),
                    MailLayout.escape(token), ReportService.TOKEN_VALIDITY_HOURS);
            String html = MailLayout.page(patientFullName, body);

            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.error("Failed to send report notification email to {}: {}", toEmail, e.getMessage());
        }
    }
}
