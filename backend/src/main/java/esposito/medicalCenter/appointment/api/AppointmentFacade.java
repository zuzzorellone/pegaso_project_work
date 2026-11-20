package esposito.medicalCenter.appointment.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AppointmentFacade {

    public void existsById(Long id);

    public Long getPatientIdByAppointment(Long id);

    String getMedicalExaminationTypeNameById(Long medicalExaminationTypeId);

    Long getMedicalExaminationTypeIdByAppointmentId(Long appointmentId);

    List<AppointmentIntegrationDTO> getFutureAppointmentsForExaminationType(Long medicalExaminationTypeId);

    List<AppointmentIntegrationDTO> getPastAppointmentsForExaminationType(Long medicalExaminationTypeId);

    List<AppointmentIntegrationDTO> getAllPastAppointments();

    Map<String, Long> getAppointmentCountsByDepartment();

    Map<String, Long> getMonthlyAppointmentCounts(LocalDateTime since);

    Map<String, Long> getDailyAppointmentCounts(LocalDateTime since);
    List<AppointmentIntegrationDTO> getAppointmentsWaitingForConfirmation();
    void confirmAppointment(Long appointmentId);
    void rejectAppointment(Long appointmentId);
    void markPatientAttended(Long appointmentId);
    void markReportUploaded(Long appointmentId);
    Map<String, Long> getWaitingForReportCountsByDepartment();
}
