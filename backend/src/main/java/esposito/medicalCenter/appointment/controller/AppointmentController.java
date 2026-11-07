package esposito.medicalCenter.appointment.controller;

import esposito.medicalCenter.appointment.service.AppointmentService;
import esposito.medicalCenter.appointment.dto.RequestAppointmentDTO;
import esposito.medicalCenter.appointment.dto.ResponseAppointmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Appuntamenti", description = "Prenotazione e gestione delle visite")
@SecurityRequirements
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Crea una nuova prenotazione", description = "Se il paziente (identificato dal codice fiscale) non esiste ancora, viene creato automaticamente.")
    @PostMapping
    public ResponseEntity<ResponseAppointmentDTO> createAppointment(@Valid @RequestBody RequestAppointmentDTO requestAppointmentDTO) {
        ResponseAppointmentDTO response = appointmentService.createAppointmentAndHandlePatient(requestAppointmentDTO);
        return ResponseEntity
                .created(buildResourceUri(response.getId()))
                .body(response);
    }

    @Operation(summary = "Date già al completo per un reparto", description = "Restituisce le date per cui non ci sono più slot disponibili nel reparto indicato.")
    @GetMapping("/bookedDates")
    public ResponseEntity<List<String>> getBookedDatesForMedicalExaminationType(
            @Parameter(description = "Nome del reparto/tipo di esame") @RequestParam String medicalExaminationType) {
        return ResponseEntity
                .ok(appointmentService.getBookedDates(medicalExaminationType));
    }

    @Operation(summary = "Orari disponibili per una data e un reparto")
    @GetMapping("/availableTimes")
    public ResponseEntity<List<String>> getAvailableTimes(
            @Parameter(description = "Data selezionata, formato YYYY-MM-DD") @RequestParam String selectedDate,
            @Parameter(description = "Nome del reparto/tipo di esame") @RequestParam String medicalExaminationType) {
        return ResponseEntity
                .ok(appointmentService.getOccupiedTimesForDate(selectedDate, medicalExaminationType));
    }

    private URI buildResourceUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
