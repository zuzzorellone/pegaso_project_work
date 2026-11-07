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

@Tag(name = "Area Medico", description = "Endpoint per il medico autenticato: le proprie visite (per reparto) e i referti")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;
    private final ReportFacade reportFacade;

    @Operation(summary = "Visite future del proprio reparto", description = "Restituisce le prossime visite del reparto a cui appartiene il medico autenticato, non solo le proprie.")
    @GetMapping("/me/appointments/future")
    public ResponseEntity<List<StaffAppointmentResponse>> getMyFutureAppointments(@AuthenticationPrincipal StaffEntity staff) {
        return ResponseEntity.ok(staffService.getFutureAppointmentsForLoggedStaff(staff));
    }

    @Operation(summary = "Visite passate del proprio reparto", description = "Come le future, ma già svolte; ogni visita indica se il referto è già stato caricato.")
    @GetMapping("/me/appointments/past")
    public ResponseEntity<List<StaffAppointmentResponse>> getMyPastAppointments(@AuthenticationPrincipal StaffEntity staff) {
        return ResponseEntity.ok(staffService.getPastAppointmentsForLoggedStaff(staff));
    }

    @Operation(summary = "Conferma la presenza del paziente", description = "Da usare il giorno della visita o dopo: la visita passa in attesa di referto.")
    @ApiResponse(responseCode = "403", description = "L'appuntamento non appartiene al reparto del medico")
    @ApiResponse(responseCode = "404", description = "Appuntamento non trovato")
    @ApiResponse(responseCode = "409", description = "La visita non è confermata oppure è in una data futura")
    @PostMapping("/me/appointments/{appointmentId}/attendance")
    public ResponseEntity<Void> confirmAttendance(@AuthenticationPrincipal StaffEntity staff,
                                                  @PathVariable Long appointmentId) {
        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        staffService.markPatientAttended(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Carica il referto di una visita", description = "Se un referto esiste già per questo appuntamento viene sostituito. Il paziente riceve una mail con il link per scaricarlo.")
    @ApiResponse(responseCode = "201", description = "Referto caricato con successo")
    @ApiResponse(responseCode = "403", description = "L'appuntamento non appartiene al reparto del medico")
    @ApiResponse(responseCode = "409", description = "La presenza del paziente non è ancora stata confermata")
    @PostMapping(value = "/me/appointments/{appointmentId}/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportSummaryResponse> uploadReport(@AuthenticationPrincipal StaffEntity staff,
                                                               @PathVariable Long appointmentId,
                                                               @Parameter(description = "File PDF del referto") @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        ReportSummaryResponse response = reportFacade.uploadReportAndNotifyPatient(appointmentId, file, staff.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Reinvia la mail di download del referto", description = "Genera un nuovo link (valido 48 ore) e lo rimanda al paziente, senza toccare il file.")
    @ApiResponse(responseCode = "403", description = "L'appuntamento non appartiene al reparto del medico")
    @ApiResponse(responseCode = "404", description = "Nessun referto caricato per questo appuntamento")
    @PostMapping("/me/appointments/{appointmentId}/report/resend")
    public ResponseEntity<Void> resendReportNotification(@AuthenticationPrincipal StaffEntity staff,
                                                           @PathVariable Long appointmentId) {
        staffService.verifyAppointmentInLoggedStaffDepartment(staff, appointmentId);

        reportFacade.resendReportNotification(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Scarica il referto di una propria visita", description = "Download diretto e autenticato, senza il token pubblico usato dal paziente.")
    @ApiResponse(responseCode = "403", description = "L'appuntamento non appartiene al reparto del medico")
    @ApiResponse(responseCode = "404", description = "Nessun referto caricato per questo appuntamento")
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
