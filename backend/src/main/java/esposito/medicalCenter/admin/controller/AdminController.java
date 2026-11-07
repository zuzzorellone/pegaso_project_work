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

@Tag(name = "Area Admin", description = "Censimento medici, reinvio referti e statistiche — richiede ruolo ADMIN")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Prenotazioni in attesa di conferma", description = "Tutte le prenotazioni di ogni reparto ancora da confermare o rifiutare, dalla più vicina.")
    @GetMapping("/appointments/pending")
    public ResponseEntity<List<AppointmentIntegrationDTO>> getPendingAppointments() {
        return ResponseEntity.ok(adminService.getPendingAppointments());
    }

    @Operation(summary = "Conferma una prenotazione", description = "La visita diventa visibile ai medici del reparto e il paziente riceve una mail di conferma.")
    @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
    @ApiResponse(responseCode = "409", description = "La prenotazione non è in attesa di conferma")
    @PostMapping("/appointments/{appointmentId}/confirm")
    public ResponseEntity<Void> confirmAppointment(@PathVariable Long appointmentId) {
        adminService.confirmAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rifiuta una prenotazione", description = "La prenotazione viene annullata, lo slot torna disponibile e il paziente riceve una mail.")
    @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
    @ApiResponse(responseCode = "409", description = "La prenotazione non è in attesa di conferma")
    @PostMapping("/appointments/{appointmentId}/reject")
    public ResponseEntity<Void> rejectAppointment(@PathVariable Long appointmentId) {
        adminService.rejectAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elenca tutte le visite con referto già inviato", description = "A differenza dell'analoga vista del medico, non è limitata a un reparto: mostra tutte le visite di ogni reparto.")
    @GetMapping("/reports")
    public ResponseEntity<List<StaffAppointmentResponse>> getAllSentReports() {
        return ResponseEntity.ok(adminService.getAllSentReports());
    }

    @Operation(summary = "Reinvia la mail di download del referto", description = "Come l'analogo endpoint del medico, ma senza il vincolo di reparto: l'admin può reinviare il referto di qualsiasi visita.")
    @ApiResponse(responseCode = "404", description = "Nessun referto caricato per questo appuntamento")
    @PostMapping("/reports/{appointmentId}/resend")
    public ResponseEntity<Void> resendReport(@PathVariable Long appointmentId) {
        adminService.resendReport(appointmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Statistiche del centro medico", description = "Visite (escluse le annullate) e visite in attesa di referto per reparto, medici attivi per reparto (sempre totali), andamento delle prenotazioni nel periodo scelto: 'week' (ultimi 7 giorni), 'month' (ultimi 30 giorni) o 'year' (ultimi 12 mesi, default).")
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats(
            @Parameter(description = "Periodo del grafico andamento prenotazioni: week, month o year") @RequestParam(defaultValue = "year") String period) {
        return ResponseEntity.ok(adminService.getStats(period));
    }

    @Operation(summary = "Elenca tutti i medici censiti", description = "Include lo stato (attivo/disabilitato) e il reparto di ciascun medico.")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @Operation(summary = "Disabilita un medico", description = "Il medico non potrà più accedere (nemmeno con un token già in suo possesso); lo storico visite/referti resta invariato.")
    @ApiResponse(responseCode = "404", description = "Medico non trovato")
    @PostMapping("/doctors/{doctorId}/disable")
    public ResponseEntity<Void> disableDoctor(@PathVariable Long doctorId) {
        adminService.setDoctorActive(doctorId, false);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Riattiva un medico disabilitato")
    @ApiResponse(responseCode = "404", description = "Medico non trovato")
    @PostMapping("/doctors/{doctorId}/enable")
    public ResponseEntity<Void> enableDoctor(@PathVariable Long doctorId) {
        adminService.setDoctorActive(doctorId, true);
        return ResponseEntity.ok().build();
    }
}
