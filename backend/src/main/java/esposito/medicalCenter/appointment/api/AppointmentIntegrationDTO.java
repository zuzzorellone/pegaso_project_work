package esposito.medicalCenter.appointment.api;

import java.time.LocalDateTime;

public record AppointmentIntegrationDTO(

        Long id,
        LocalDateTime appointmentDateTime,
        String status,
        String medicalExaminationType,
        Long medicalExaminationTypeId,
        Long patientId,
        String patientName,
        String patientSurname,
        String patientTelephoneNumber,
        String patientTaxIdentificationNumber
) {
}
