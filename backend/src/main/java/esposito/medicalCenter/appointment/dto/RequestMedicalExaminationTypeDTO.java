package esposito.medicalCenter.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;


public record RequestMedicalExaminationTypeDTO(

        @NotNull(message = "Mandatory to insert the name")
        String name,

        String description,

        Boolean active,

        @NotNull(message = "Mandatory to insert the examination duration")
        Integer duration,

        @NotNull(message = "Mandatory to insert the open time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime openTime,

        @NotNull(message = "Mandatory to insert the close time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime closeTime,

        List<String> daysOfWeek
) {
}
