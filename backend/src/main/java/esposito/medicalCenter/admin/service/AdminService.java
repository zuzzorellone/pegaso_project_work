package esposito.medicalCenter.admin.service;

import esposito.medicalCenter.admin.dto.AdminStatsResponse;
import esposito.medicalCenter.admin.dto.DepartmentCountDTO;
import esposito.medicalCenter.admin.dto.TrendPointDTO;
import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.appointment.api.AppointmentIntegrationDTO;
import esposito.medicalCenter.report.api.ReportFacade;
import esposito.medicalCenter.staff.api.DoctorSummaryDTO;
import esposito.medicalCenter.staff.api.StaffFacade;
import esposito.medicalCenter.staff.api.StaffAppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final int STATS_MONTHS_BACK = 12;
    private static final int STATS_WEEK_DAYS = 7;
    private static final int STATS_MONTH_DAYS = 30;

    private final AppointmentFacade appointmentFacade;
    private final ReportFacade reportFacade;
    private final StaffFacade staffFacade;

    @Transactional(readOnly = true)
    public List<StaffAppointmentResponse> getAllSentReports() {
        List<AppointmentIntegrationDTO> pastAppointments = appointmentFacade.getAllPastAppointments();
        List<Long> appointmentIds = pastAppointments.stream().map(AppointmentIntegrationDTO::id).toList();
        Map<Long, String> reportFilenamesByAppointmentId =
                reportFacade.getReportFilenamesByAppointmentIds(appointmentIds);

        return pastAppointments.stream()
                .filter(appointment -> reportFilenamesByAppointmentId.containsKey(appointment.id()))
                .map(appointment -> new StaffAppointmentResponse(
                        appointment,
                        true,
                        reportFilenamesByAppointmentId.get(appointment.id())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentIntegrationDTO> getPendingAppointments() {
        return appointmentFacade.getAppointmentsWaitingForConfirmation();
    }

    public void confirmAppointment(Long appointmentId) {
        appointmentFacade.confirmAppointment(appointmentId);
    }

    public void rejectAppointment(Long appointmentId) {
        appointmentFacade.rejectAppointment(appointmentId);
    }

    public void resendReport(Long appointmentId) {
        reportFacade.resendReportNotification(appointmentId);
    }

    @Transactional(readOnly = true)
    public List<DoctorSummaryDTO> getAllDoctors() {
        return staffFacade.getAllDoctors();
    }

    public void setDoctorActive(Long doctorId, boolean active) {
        staffFacade.setDoctorActive(doctorId, active);
    }

    @Transactional(readOnly = true)
    public AdminStatsResponse getStats(String period) {
        return new AdminStatsResponse(
                toDepartmentCountList(appointmentFacade.getAppointmentCountsByDepartment()),
                toDepartmentCountList(appointmentFacade.getWaitingForReportCountsByDepartment()),
                toDepartmentCountList(staffFacade.getActiveDoctorCountsByDepartment()),
                computeAppointmentsTrend(period)
        );
    }

    private List<TrendPointDTO> computeAppointmentsTrend(String period) {
        return switch (period) {
            case "week" -> computeDailyTrend(STATS_WEEK_DAYS);
            case "month" -> computeDailyTrend(STATS_MONTH_DAYS);
            default -> computeMonthlyTrend();
        };
    }

    private List<TrendPointDTO> computeDailyTrend(int daysBack) {
        LocalDate today = LocalDate.now();
        LocalDateTime since = today.minusDays(daysBack - 1L).atStartOfDay();
        Map<String, Long> counts = appointmentFacade.getDailyAppointmentCounts(since);

        List<TrendPointDTO> result = new ArrayList<>();
        for (int i = daysBack - 1; i >= 0; i--) {
            String day = today.minusDays(i).toString();
            result.add(new TrendPointDTO(day, counts.getOrDefault(day, 0L)));
        }
        return result;
    }

    private List<TrendPointDTO> computeMonthlyTrend() {
        LocalDateTime since = LocalDateTime.now().minusMonths(STATS_MONTHS_BACK - 1L)
                .withDayOfMonth(1).toLocalDate().atStartOfDay();
        Map<String, Long> counts = appointmentFacade.getMonthlyAppointmentCounts(since);

        YearMonth current = YearMonth.now();
        List<TrendPointDTO> result = new ArrayList<>();
        for (int i = STATS_MONTHS_BACK - 1; i >= 0; i--) {
            String month = current.minusMonths(i).toString();
            result.add(new TrendPointDTO(month, counts.getOrDefault(month, 0L)));
        }
        return result;
    }

    private List<DepartmentCountDTO> toDepartmentCountList(Map<String, Long> counts) {
        return counts.entrySet().stream()
                .map(entry -> new DepartmentCountDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DepartmentCountDTO::department))
                .toList();
    }
}
