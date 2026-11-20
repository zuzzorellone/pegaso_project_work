package esposito.medicalCenter.admin.controller;

import esposito.medicalCenter.admin.dto.AdminStatsResponse;
import esposito.medicalCenter.admin.service.AdminService;
import esposito.medicalCenter.appointment.api.AppointmentIntegrationDTO;
import esposito.medicalCenter.staff.api.DoctorSummaryDTO;
import esposito.medicalCenter.staff.api.StaffAppointmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin", description = "Requires the ADMIN role.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Bookings awaiting confirmation", description = "All departments, nearest first.")
    @GetMapping("/appointments/pending")
    public ResponseEntity<List<AppointmentIntegrationDTO>> getPendingAppointments() {
        return ResponseEntity.ok(adminService.getPendingAppointments());
    }

    @Operation(summary = "Confirm a booking", description = "Emails the patient.")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @ApiResponse(responseCode = "409", description = "Booking is not awaiting confirmation")
    @PostMapping("/appointments/{appointmentId}/confirm")
    public ResponseEntity<Void> confirmAppointment(@PathVariable Long appointmentId) {
        adminService.confirmAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reject a booking", description = "Frees the slot and emails the patient.")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @ApiResponse(responseCode = "409", description = "Booking is not awaiting confirmation")
    @PostMapping("/appointments/{appointmentId}/reject")
    public ResponseEntity<Void> rejectAppointment(@PathVariable Long appointmentId) {
        adminService.rejectAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "List visits with a report sent", description = "Covers every department.")
    @GetMapping("/reports")
    public ResponseEntity<List<StaffAppointmentResponse>> getAllSentReports() {
        return ResponseEntity.ok(adminService.getAllSentReports());
    }

    @Operation(summary = "Resend the report download email", description = "Works for any department.")
    @ApiResponse(responseCode = "404", description = "No report uploaded for this appointment")
    @PostMapping("/reports/{appointmentId}/resend")
    public ResponseEntity<Void> resendReport(@PathVariable Long appointmentId) {
        adminService.resendReport(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Medical center statistics")
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats(
            @Parameter(description = "Booking trend period: week, month or year") @RequestParam(defaultValue = "year") String period) {
        return ResponseEntity.ok(adminService.getStats(period));
    }

    @Operation(summary = "List all doctors", description = "Includes status and department.")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @Operation(summary = "Disable a doctor", description = "Blocks login and existing tokens, keeps history.")
    @ApiResponse(responseCode = "404", description = "Doctor not found")
    @PostMapping("/doctors/{doctorId}/disable")
    public ResponseEntity<Void> disableDoctor(@PathVariable Long doctorId) {
        adminService.setDoctorActive(doctorId, false);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Re-enable a disabled doctor")
    @ApiResponse(responseCode = "404", description = "Doctor not found")
    @PostMapping("/doctors/{doctorId}/enable")
    public ResponseEntity<Void> enableDoctor(@PathVariable Long doctorId) {
        adminService.setDoctorActive(doctorId, true);
        return ResponseEntity.ok().build();
    }
}
