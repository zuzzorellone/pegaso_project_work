package esposito.medicalCenter.staff.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.core.exception.AppointmentAccessDeniedException;
import esposito.medicalCenter.report.api.ReportFacade;
import esposito.medicalCenter.staff.entity.StaffEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    private final AppointmentFacade appointmentFacade = mock(AppointmentFacade.class);
    private final ReportFacade reportFacade = mock(ReportFacade.class);

    private final StaffService service = new StaffService(appointmentFacade, reportFacade);

    private StaffEntity createDoctor(Long departmentId) {
        StaffEntity doctor = new StaffEntity();
        doctor.setMedicalExaminationTypeId(departmentId);
        return doctor;
    }

    @Test
    void doctorWithoutDepartmentSeesNoAppointments() {
        StaffEntity doctor = createDoctor(null);

        assertTrue(service.getFutureAppointmentsForLoggedStaff(doctor).isEmpty());
        assertTrue(service.getPastAppointmentsForLoggedStaff(doctor).isEmpty());
    }

    @Test
    void appointmentOfTheSameDepartmentIsAllowed() {
        when(appointmentFacade.getMedicalExaminationTypeIdByAppointmentId(10L)).thenReturn(3L);

        assertDoesNotThrow(() -> service.verifyAppointmentInLoggedStaffDepartment(createDoctor(3L), 10L));
    }

    @Test
    void appointmentOfAnotherDepartmentIsDenied() {
        when(appointmentFacade.getMedicalExaminationTypeIdByAppointmentId(10L)).thenReturn(4L);

        assertThrows(AppointmentAccessDeniedException.class,
                () -> service.verifyAppointmentInLoggedStaffDepartment(createDoctor(3L), 10L));
    }
}
