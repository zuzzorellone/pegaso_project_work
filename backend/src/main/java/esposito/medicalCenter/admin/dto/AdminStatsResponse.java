package esposito.medicalCenter.admin.dto;

import java.util.List;

public record AdminStatsResponse(
        List<DepartmentCountDTO> appointmentsByDepartment,
        List<DepartmentCountDTO> pendingReportsByDepartment,
        List<DepartmentCountDTO> activeDoctorsByDepartment,
        List<TrendPointDTO> appointmentsTrend
) {
}
