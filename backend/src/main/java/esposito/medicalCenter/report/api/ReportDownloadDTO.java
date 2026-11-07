package esposito.medicalCenter.report.api;

import org.springframework.core.io.Resource;

public record ReportDownloadDTO(
        Resource file,
        String originalFilename
) {
}
