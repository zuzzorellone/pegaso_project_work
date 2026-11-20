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

@Tag(name = "Appointments", description = "Patient bookings.")
@SecurityRequirements
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Create a booking", description = "Creates the patient if the tax code is new.")
    @PostMapping
    public ResponseEntity<ResponseAppointmentDTO> createAppointment(@Valid @RequestBody RequestAppointmentDTO requestAppointmentDTO) {
        ResponseAppointmentDTO response = appointmentService.createAppointmentAndHandlePatient(requestAppointmentDTO);
        return ResponseEntity
                .created(buildResourceUri(response.getId()))
                .body(response);
    }

    @Operation(summary = "Fully booked dates for a department")
    @GetMapping("/bookedDates")
    public ResponseEntity<List<String>> getBookedDatesForMedicalExaminationType(
            @Parameter(description = "Department name") @RequestParam String medicalExaminationType) {
        return ResponseEntity
                .ok(appointmentService.getBookedDates(medicalExaminationType));
    }

    @Operation(summary = "Free times for a date and department")
    @GetMapping("/availableTimes")
    public ResponseEntity<List<String>> getAvailableTimes(
            @Parameter(description = "Date in YYYY-MM-DD format") @RequestParam String selectedDate,
            @Parameter(description = "Department name") @RequestParam String medicalExaminationType) {
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
