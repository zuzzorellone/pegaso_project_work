package esposito.medicalCenter.report.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ReportFacade {

    Map<Long, String> getReportFilenamesByAppointmentIds(List<Long> appointmentIds);
    ReportSummaryResponse uploadReportAndNotifyPatient(Long appointmentId, MultipartFile file, Long staffId);
    void resendReportNotification(Long appointmentId);
    ReportDownloadDTO getReportFileForAppointment(Long appointmentId);
}
