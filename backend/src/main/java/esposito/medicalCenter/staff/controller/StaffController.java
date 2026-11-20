package esposito.medicalCenter.staff.controller;

import esposito.medicalCenter.report.api.ReportDownloadDTO;
import esposito.medicalCenter.report.api.ReportSummaryResponse;
import esposito.medicalCenter.report.api.ReportFacade;
import esposito.medicalCenter.staff.api.StaffAppointmentResponse;
import esposito.medicalCenter.staff.entity.StaffEntity;
import esposito.medicalCenter.staff.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Doctor", description = "Endpoints for the logged-in doctor.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;
    private final ReportFacade reportFacade;

    @Operation(summary = "Upcoming visits in own department")
    @GetMapping("/me/appointments/future")
    public ResponseEntity<List<StaffAppointmentResponse>> getMyFutureAppointments(@AuthenticationPrincipal StaffEntity staff) {
        return ResponseEntity.ok(staffService.getFutureAppointmentsForLoggedStaff(staff));
    }

    @Operation(summary = "Past visits in own department", description = "Shows whether each report has been uploaded.")
    @GetMapping("/me/appointments/past")
    public ResponseEntity<List<StaffAppointmentResponse>> getMyPastAppointments(@AuthenticationPrincipal StaffEntity staff) {
        return ResponseEntity.ok(staffService.getPastAppointmentsForLoggedStaff(staff));
    }

    @Operation(summary = "Confirm patient attendance", description = "Allowed on or after the visit day.")
    @ApiResponse(responseCode = "403", description = "Appointment is not in the doctor's department")
    @ApiResponse(responseCode = "404", description = "Appointment not found")
    @ApiResponse(responseCode = "409", description = "Visit is not confirmed or is in the future")
    @PostMapping("/me/appointments/{appointmentId}/attendance")
    public ResponseEntity<Void> confirmAttendance(@AuthenticationPrincipal StaffEntity staff,
                                                  @PathVariable Long appointmentId) {
        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        staffService.markPatientAttended(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload a visit report", description = "Replaces any existing report and emails the patient.")
    @ApiResponse(responseCode = "201", description = "Report uploaded")
    @ApiResponse(responseCode = "403", description = "Appointment is not in the doctor's department")
    @ApiResponse(responseCode = "409", description = "Patient attendance not confirmed yet")
    @PostMapping(value = "/me/appointments/{appointmentId}/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportSummaryResponse> uploadReport(@AuthenticationPrincipal StaffEntity staff,
                                                               @PathVariable Long appointmentId,
                                                               @Parameter(description = "Report PDF file") @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        ReportSummaryResponse response = reportFacade.uploadReportAndNotifyPatient(appointmentId, file, staff.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Resend the report download email", description = "Sends a new link, valid for 48 hours.")
    @ApiResponse(responseCode = "403", description = "Appointment is not in the doctor's department")
    @ApiResponse(responseCode = "404", description = "No report uploaded for this appointment")
    @PostMapping("/me/appointments/{appointmentId}/report/resend")
    public ResponseEntity<Void> resendReportNotification(@AuthenticationPrincipal StaffEntity staff,
                                                           @PathVariable Long appointmentId) {
        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        reportFacade.resendReportNotification(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Download a visit report", description = "Authenticated download, no patient token needed.")
    @ApiResponse(responseCode = "403", description = "Appointment is not in the doctor's department")
    @ApiResponse(responseCode = "404", description = "No report uploaded for this appointment")
    @GetMapping("/me/appointments/{appointmentId}/report/download")
    public ResponseEntity<Resource> downloadReport(@AuthenticationPrincipal StaffEntity staff,
                                                     @PathVariable Long appointmentId) {
        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        ReportDownloadDTO report = reportFacade.getReportFileForAppointment(appointmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.originalFilename() + "\"")
                .body(report.file());
    }
}
