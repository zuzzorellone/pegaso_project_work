package esposito.medicalCenter.core.mail;

import org.springframework.web.util.HtmlUtils;

import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor 
public final class MailLayout {

    public static final String BRAND = "Salute e Benessere Lazio";

    private static final String BLUE = "#5AA8E6";
    private static final String BLUE_LIGHT = "#EAF4FC";
    private static final String BACKGROUND = "#F5FAFE";
    private static final String BORDER = "#D6E4F0";
    private static final String TEXT = "#1F2D3D";
    private static final String TEXT_SOFT = "#5B6B7C";

    public static String escape(String text) {
        return HtmlUtils.htmlEscape(text, "UTF-8");
    }

    public static String page(String patientFullName, String bodyHtml) {
        return """
                <!DOCTYPE html>
                <html lang="it">
                <body style="margin:0;padding:24px 12px;background:%s;font-family:Arial,Helvetica,sans-serif;color:%s;">
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0"
                       style="max-width:560px;margin:0 auto;background:#FFFFFF;border:1px solid %s;border-radius:8px;border-collapse:separate;">
                  <tr><td style="background:%s;padding:18px 24px;border-radius:8px 8px 0 0;font-size:20px;font-weight:bold;color:%s;">&#10010; %s</td></tr>
                  <tr><td style="padding:24px;font-size:15px;line-height:1.5;">
                    <p style="margin:0 0 12px;">Gentile %s,</p>
                    %s
                    <p style="margin:24px 0 0;">Cordiali saluti,<br><b>%s</b></p>
                  </td></tr>
                  <tr><td style="padding:14px 24px;border-top:1px solid %s;font-size:12px;color:%s;">
                    %s &middot; Poliambulatorio specialistico<br>Questa è un'email automatica: ti chiediamo di non rispondere.
                  </td></tr>
                </table>
                </body>
                </html>
                """.formatted(BACKGROUND, TEXT, BORDER, BLUE, TEXT, BRAND,
                escape(patientFullName), bodyHtml, BRAND, BORDER, TEXT_SOFT, BRAND);
    }

    public static String details(Map<String, String> rows) {
        String cells = rows.entrySet().stream()
                .map(row -> "<tr><td style=\"padding:4px 16px 4px 0;color:%s;\">%s</td><td style=\"padding:4px 0;font-weight:bold;\">%s</td></tr>"
                        .formatted(TEXT_SOFT, escape(row.getKey()), escape(row.getValue())))
                .collect(Collectors.joining());
        return "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%%;margin:16px 0;padding:12px 16px;background:%s;border-radius:8px;\">%s</table>"
                .formatted(BLUE_LIGHT, cells);
    }

    public static String button(String label, String url) {
        return "<p style=\"margin:20px 0;\"><a href=\"%s\" style=\"display:inline-block;padding:12px 24px;background:%s;color:%s;text-decoration:none;font-weight:bold;border-radius:8px;\">%s</a></p>"
                .formatted(escape(url), BLUE, TEXT, escape(label));
    }
}
