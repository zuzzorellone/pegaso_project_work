package esposito.medicalCenter.appointment.dto;

import esposito.medicalCenter.appointment.entity.AppointmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResponseAppointmentDTO {

    private Long id;
    private String patientName;
    private String patientSurname;

    private LocalDateTime appointmentDateTime;

    private String status;

    private String medicalExaminationType;
}
