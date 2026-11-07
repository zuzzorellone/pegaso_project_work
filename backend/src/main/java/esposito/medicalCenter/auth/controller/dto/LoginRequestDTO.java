package esposito.medicalCenter.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "The email can't be blank")
        String email,

        @NotBlank(message = "The password can't be blank")
        String password
) {
}
