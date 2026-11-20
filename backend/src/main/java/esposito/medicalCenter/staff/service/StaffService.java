package esposito.medicalCenter.staff.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.appointment.api.AppointmentIntegrationDTO;
import esposito.medicalCenter.core.exception.AppointmentAccessDeniedException;
import esposito.medicalCenter.report.api.ReportFacade;
import esposito.medicalCenter.staff.api.StaffAppointmentResponse;
import esposito.medicalCenter.staff.entity.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final AppointmentFacade appointmentFacade;
    private final ReportFacade reportFacade;

    @Transactional(readOnly = true)
    public List<StaffAppointmentResponse> getFutureAppointmentsForLoggedStaff(StaffEntity staff) {
        if (staff.getMedicalExaminationTypeId() == null) {
            return List.of();
        }
        return toStaffAppointmentResponses(
                appointmentFacade.getFutureAppointmentsForExaminationType(staff.getMedicalExaminationTypeId())
        );
    }

    @Transactional(readOnly = true)
    public List<StaffAppointmentResponse> getPastAppointmentsForLoggedStaff(StaffEntity staff) {
        if (staff.getMedicalExaminationTypeId() == null) {
            return List.of();
        }
        return toStaffAppointmentResponses(
                appointmentFacade.getPastAppointmentsForExaminationType(staff.getMedicalExaminationTypeId())
        );
    }

    public void markPatientAttended(Long appointmentId) {
        appointmentFacade.markPatientAttended(appointmentId);
    }

    public void verifyAppointmentInLoggedStaffDepartment(StaffEntity staff, Long appointmentId) {
        Long appointmentDepartmentId = appointmentFacade.getMedicalExaminationTypeIdByAppointmentId(appointmentId);

        if (!Objects.equals(appointmentDepartmentId, staff.getMedicalExaminationTypeId())) {
            throw new AppointmentAccessDeniedException("This appointment does not belong to your department");
        }
    }

    private List<StaffAppointmentResponse> toStaffAppointmentResponses(List<AppointmentIntegrationDTO> appointments) {
        List<Long> appointmentIds = appointments.stream().map(AppointmentIntegrationDTO::id).toList();
        Map<Long, String> reportFilenamesByAppointmentId = reportFacade.getReportFilenamesByAppointmentIds(appointmentIds);

        return appointments.stream()
                .map(appointment -> new StaffAppointmentResponse(
                        appointment,
                        reportFilenamesByAppointmentId.containsKey(appointment.id()),
                        reportFilenamesByAppointmentId.get(appointment.id())
                ))
                .toList();
    }
}
