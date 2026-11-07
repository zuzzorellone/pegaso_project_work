package esposito.medicalCenter.report.service;

import esposito.medicalCenter.report.api.ReportDownloadDTO;
import esposito.medicalCenter.report.api.ReportFacade;
import esposito.medicalCenter.report.api.ReportSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportFacadeImpl implements ReportFacade {

    private final ReportService reportService;

    @Override
    public Map<Long, String> getReportFilenamesByAppointmentIds(List<Long> appointmentIds) {
        return reportService.getReportFilenamesByAppointmentIds(appointmentIds);
    }

    @Override
    public ReportSummaryResponse uploadReportAndNotifyPatient(Long appointmentId, MultipartFile file, Long staffId) {
        return reportService.uploadReportAndNotifyPatient(appointmentId, file, staffId);
    }

    @Override
    public void resendReportNotification(Long appointmentId) {
        reportService.resendReportNotification(appointmentId);
    }

    @Override
    public ReportDownloadDTO getReportFileForAppointment(Long appointmentId) {
        return reportService.getReportFileForAppointment(appointmentId);
    }
}
