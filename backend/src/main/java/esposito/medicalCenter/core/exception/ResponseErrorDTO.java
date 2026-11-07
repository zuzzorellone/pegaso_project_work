package esposito.medicalCenter.core.exception;

import java.time.LocalDateTime;

public record ResponseErrorDTO(

        LocalDateTime time,
        int status,
        String error,
        String message,
        String path
) {
}
