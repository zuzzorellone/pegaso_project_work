package esposito.medicalCenter.appointment.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AppointmentFacade {

    public void existsById(Long id);
    public Long getPatientIdByAppointment(Long id);
    public String getMedicalExaminationTypeNameById(Long medicalExaminationTypeId);
    public Long getMedicalExaminationTypeIdByAppointmentId(Long appointmentId);
    public List<AppointmentIntegrationDTO> getFutureAppointmentsForExaminationType(Long medicalExaminationTypeId);
    public List<AppointmentIntegrationDTO> getPastAppointmentsForExaminationType(Long medicalExaminationTypeId);
    public List<AppointmentIntegrationDTO> getAllPastAppointments();
    public Map<String, Long> getAppointmentCountsByDepartment();
    public Map<String, Long> getMonthlyAppointmentCounts(LocalDateTime since);
    public Map<String, Long> getDailyAppointmentCounts(LocalDateTime since);
    public List<AppointmentIntegrationDTO> getAppointmentsWaitingForConfirmation();
   public void confirmAppointment(Long appointmentId);
    public void rejectAppointment(Long appointmentId);
    public void markPatientAttended(Long appointmentId);
    public void markReportUploaded(Long appointmentId);
    public Map<String, Long> getWaitingForReportCountsByDepartment();
}
