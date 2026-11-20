package esposito.medicalCenter.staff.api;

import esposito.medicalCenter.appointment.api.AppointmentIntegrationDTO;

public record StaffAppointmentResponse(
        AppointmentIntegrationDTO appointment,
        boolean hasReport,
        String reportFilename
) {
}
