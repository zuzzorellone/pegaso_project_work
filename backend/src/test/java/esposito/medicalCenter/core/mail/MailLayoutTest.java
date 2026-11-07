package esposito.medicalCenter.core.mail;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MailLayoutTest {

    @Test
    void pageHasBrandGreetingSignatureAndFooter() {
        String html = MailLayout.page("Marco Rossi", "<p>corpo</p>");

        assertThat(html)
                .contains("Salute e Benessere Lazio")
                .contains("Gentile Marco Rossi,")
                .contains("<p>corpo</p>")
                .contains("Cordiali saluti,")
                .contains("non rispondere")
                .doesNotContain("Il Centro Medico");
    }

    @Test
    void patientNameIsEscaped() {
        assertThat(MailLayout.page("<script>x</script>", ""))
                .contains("&lt;script&gt;x&lt;/script&gt;")
                .doesNotContain("<script>");
    }

    @Test
    void detailsShowEachRowEscaped() {
        Map<String, String> rows = new LinkedHashMap<>();
        rows.put("Visita", "Dermatologia");
        rows.put("Quando", "A & B");

        assertThat(MailLayout.details(rows))
                .contains("Visita").contains("Dermatologia")
                .contains("A &amp; B");
    }

    @Test
    void buttonLinksToTheEscapedUrl() {
        assertThat(MailLayout.button("Scarica il referto", "http://x/a?token=1&taxId=2"))
                .contains("href=\"http://x/a?token=1&amp;taxId=2\"")
                .contains("Scarica il referto");
    }
}
