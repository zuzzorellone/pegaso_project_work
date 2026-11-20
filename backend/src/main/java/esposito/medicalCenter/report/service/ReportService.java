package esposito.medicalCenter.report.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.core.exception.ReportFileNotFoundException;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import esposito.medicalCenter.report.api.ReportDownloadDTO;
import esposito.medicalCenter.report.api.ReportSummaryResponse;
import esposito.medicalCenter.report.entity.ReportEntity;
import esposito.medicalCenter.report.entity.ReportTokenEntity;
import esposito.medicalCenter.report.repository.ReportRepository;
import esposito.medicalCenter.report.repository.ReportTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    public static final int TOKEN_VALIDITY_HOURS = 48;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    private final ReportRepository reportRepository;
    private final ReportTokenRepository reportTokenRepository;
    private final AppointmentFacade appointmentFacade;
    private final PatientFacade patientFacade;
    private final ReportMailService reportMailService;

    public Map<Long, String> getReportFilenamesByAppointmentIds(List<Long> appointmentIds) {
        Map<Long, String> result = new HashMap<>();
        reportRepository.findByAppointmentIdIn(appointmentIds)
                .forEach(r -> result.put(r.getAppointmentId(), r.getOriginalFilename()));
        return result;
    }

    public ReportSummaryResponse getReportForAppointmentId(Long appointmentId) {
        return reportRepository.findByAppointmentId(appointmentId)
                .map(report -> new ReportSummaryResponse(
                        report.getId(),
                        report.getOriginalFilename(),
                        report.getAppointmentId()
                ))
                .orElse(null);
    }

    @Transactional
    public ReportSummaryResponse createReportWithFile(Long appointmentId, MultipartFile file) {
        return createReportWithFile(appointmentId, file, null);
    }

    @Transactional
    public ReportSummaryResponse createReportWithFile(Long appointmentId, MultipartFile file, Long staffId) {

        appointmentFacade.existsById(appointmentId);

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".pdf";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path directoryPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), filePath);

            ReportEntity newReport = new ReportEntity();
            newReport.setAppointmentId(appointmentId);
            newReport.setReportPath(filePath.toString());
            newReport.setOriginalFilename(originalFilename);
            newReport.setStaffId(staffId);
            ReportEntity savedReport = reportRepository.save(newReport);

            return new ReportSummaryResponse(
                    savedReport.getId(),
                    savedReport.getOriginalFilename(),
                    savedReport.getAppointmentId()
            );

        } catch (IOException e) {
            throw new RuntimeException("Saving File Error", e);
        }
    }

    @Transactional
    private Optional<String> deleteExistingReportForAppointment(Long appointmentId) {
        return reportRepository.findByAppointmentId(appointmentId).map(existingReport -> {
            reportTokenRepository.findByReportId(existingReport.getId())
                    .ifPresent(reportTokenRepository::delete);

            String oldReportPath = existingReport.getReportPath();

            reportRepository.delete(existingReport);
            reportRepository.flush();

            return oldReportPath;
        });
    }

    @Transactional
    public String generateAndSaveTokenForReport(Long reportId) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report Not Found with ID: " + reportId));

        reportTokenRepository.findByReportId(reportId)
                .ifPresent(reportTokenRepository::delete);
        reportTokenRepository.flush();

        ReportTokenEntity newToken = new ReportTokenEntity();
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpirationDate(LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS));
        newToken.setFailedAttempts(0);
        newToken.setReport(report);
        reportTokenRepository.save(newToken);

        return newToken.getToken();
    }

    @Transactional
    public ReportSummaryResponse uploadReportAndNotifyPatient(Long appointmentId, MultipartFile file, Long staffId) {
        appointmentFacade.markReportUploaded(appointmentId);
        Optional<String> oldReportPath = deleteExistingReportForAppointment(appointmentId);
        ReportSummaryResponse summary = createReportWithFile(appointmentId, file, staffId);
        String token = generateAndSaveTokenForReport(summary.getId());

        Long patientId = appointmentFacade.getPatientIdByAppointment(appointmentId);
        PatientIntegrationDTO patient = patientFacade.getPatientById(patientId);

        String downloadLink = frontendBaseUrl + "/downloadReport?token=" + token + "&taxId=" + patient.taxIdentificationNumber();
        reportMailService.sendReportAvailableEmail(
                patient.email(),
                patient.name() + " " + patient.surname(),
                downloadLink,
                token
        );

        oldReportPath.ifPresent(path -> {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                log.warn("Could not delete old report file {}: {}", path, e.getMessage());
            }
        });

        return summary;
    }

    @Transactional
    public void resendReportNotification(Long appointmentId) {
        ReportEntity report = reportRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("No report exists for appointment: " + appointmentId));

        String token = generateAndSaveTokenForReport(report.getId());

        Long patientId = appointmentFacade.getPatientIdByAppointment(appointmentId);
        PatientIntegrationDTO patient = patientFacade.getPatientById(patientId);

        String downloadLink = frontendBaseUrl + "/downloadReport?token=" + token + "&taxId=" + patient.taxIdentificationNumber();
        reportMailService.sendReportAvailableEmail(
                patient.email(),
                patient.name() + " " + patient.surname(),
                downloadLink,
                token
        );
    }

    public ReportDownloadDTO getReportFileForAppointment(Long appointmentId) {
        ReportEntity report = reportRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("No report exists for appointment: " + appointmentId));

        Path filePath = Paths.get(report.getReportPath()).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ReportFileNotFoundException("The file is no longer available on the server");
            }

            return new ReportDownloadDTO(resource, report.getOriginalFilename());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading the file path", e);
        }
    }

}
