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

@Tag(name = "Public report download", description = "No login, needs the token and the tax code.")
@SecurityRequirements
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/reports")
public class PublicReportController {

    private final PublicReportService publicReportService;

    @Operation(summary = "Download a report by token", description = "The token is emailed on upload and lasts 48 hours.")
    @ApiResponse(responseCode = "200", description = "Report returned as PDF")
    @ApiResponse(responseCode = "400", description = "Expired or invalid token, or wrong tax code")
    @GetMapping("/download")
    public ResponseEntity<?> downloadReport(
            @Parameter(description = "Token created when the report was uploaded") @RequestParam("token") String token,
            @Parameter(description = "Patient's tax code, checked against the token") @RequestParam("taxId") String taxIdentificationNumber) {
        ReportDownloadDTO report = publicReportService.validateTokenAndGetReport(token, taxIdentificationNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.originalFilename() + "\"")
                .body(report.file());
    }
}
