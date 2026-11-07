package esposito.medicalCenter.report.controller.publicapi;

import esposito.medicalCenter.report.api.ReportDownloadDTO;
import esposito.medicalCenter.report.entity.ReportEntity;
import esposito.medicalCenter.report.service.PublicReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name = "Download Pubblico Referti", description = "Download del referto da parte del paziente, senza login: serve token + codice fiscale")
@SecurityRequirements
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/reports")
public class PublicReportController {

    private final PublicReportService publicReportService;

    @Operation(summary = "Scarica il referto tramite token", description = "Il token viene inviato via mail al paziente quando il medico carica il referto (valido 48 ore).")
    @ApiResponse(responseCode = "200", description = "Referto restituito come PDF")
    @ApiResponse(responseCode = "400", description = "Token scaduto, non valido, o codice fiscale non corrispondente")
    @GetMapping("/download")
    public ResponseEntity<?> downloadReport(
            @Parameter(description = "Token generato al momento del caricamento del referto") @RequestParam("token") String token,
            @Parameter(description = "Codice fiscale del paziente, verificato insieme al token") @RequestParam("taxId") String taxIdentificationNumber) {
        ReportDownloadDTO report = publicReportService.validateTokenAndGetReport(token, taxIdentificationNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.originalFilename() + "\"")
                .body(report.file());
    }
}
