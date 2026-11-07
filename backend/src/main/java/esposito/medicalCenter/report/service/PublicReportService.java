package esposito.medicalCenter.report.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.core.exception.ExpiredTokenException;
import esposito.medicalCenter.core.exception.FailedAttemptsException;
import esposito.medicalCenter.core.exception.GenericReportDownloadException;
import esposito.medicalCenter.core.exception.ReportFileNotFoundException;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import esposito.medicalCenter.report.api.ReportDownloadDTO;
import esposito.medicalCenter.report.entity.ReportEntity;
import esposito.medicalCenter.report.entity.ReportTokenEntity;
import esposito.medicalCenter.report.repository.ReportTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PublicReportService {

    private final ReportTokenRepository tokenRepository;
    private final PatientFacade patientFacade;
    private final AppointmentFacade appointmentFacade;

    @Transactional(readOnly = false)
    public ReportDownloadDTO validateTokenAndGetReport(String tokenString,
                                                  String taxIdentificationNumber) {

        ReportTokenEntity tokenEntity = tokenRepository.findReportTokenEntityByToken(tokenString)
                .orElseThrow(() -> new EntityNotFoundException(("Report-Token relation not found with token: " + tokenString)));

        if (tokenEntity.isExpired()) {
            throw new ExpiredTokenException("The token for the reports download is expired");
        }

        if (tokenEntity.getFailedAttempts() >= 3) {
            throw new FailedAttemptsException("Too many failed attempts");
        }

        boolean error = Boolean.FALSE;
        try {
            PatientIntegrationDTO patientIntegrationDTO = patientFacade.getPatientByTaxIdentificationNumber(taxIdentificationNumber);
            Long patientId = appointmentFacade.getPatientIdByAppointment(tokenEntity.getReport().getAppointmentId());

            if(!patientId.equals(patientIntegrationDTO.id())) {
                error = true;
            }
        }catch (EntityNotFoundException e){
            error = true;
        }

        if(error) {
            tokenEntity.setFailedAttempts(tokenEntity.getFailedAttempts()+1);
            tokenRepository.save(tokenEntity);
            throw new GenericReportDownloadException("The Reports Download is not possible");
        }

        ReportEntity report = tokenEntity.getReport();
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
