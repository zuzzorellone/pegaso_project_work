package esposito.medicalCenter.report.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportMailServiceTest {

    @Test
    void reportEmailHasDownloadButtonCodeAndBrand() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage(Session.getInstance(new Properties())));
        ReportMailService service = new ReportMailService(mailSender);
        ReflectionTestUtils.setField(service, "frontendBaseUrl", "http://localhost:5173");

        service.sendReportAvailableEmail("a@example.com", "Marco Rossi",
                "http://localhost:5173/downloadReport?token=abc&taxId=RSS", "abc");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        MimeMessage message = captor.getValue();
        message.saveChanges();
        assertThat((String) message.getContent())
                .contains("Gentile Marco Rossi,")
                .contains("href=\"http://localhost:5173/downloadReport?token=abc&amp;taxId=RSS\"")
                .contains("Scarica il referto")
                .contains("abc")
                .contains("48 ore")
                .contains("puoi")
                .doesNotContain("può recarsi")
                .contains("Salute e Benessere Lazio");
    }
}
