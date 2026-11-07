package esposito.medicalCenter.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import esposito.medicalCenter.patient.api.RequestPatientDTO;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestAppointmentDTO(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Long id,

        @NotNull(message = "The patient information can't be null")
        RequestPatientDTO patient,

        @NotNull(message = "The appointment date can't be null")
        @FutureOrPresent(message = "The appointment date can`t be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime appointmentDate,

        @NotNull(message = "The medical examination type can`t be null")
        String medicalExaminationType
) {
}