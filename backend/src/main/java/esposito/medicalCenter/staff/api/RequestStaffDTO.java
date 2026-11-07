package esposito.medicalCenter.staff.api;

import esposito.medicalCenter.staff.StaffRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestStaffDTO(

        @NotNull(message = "The staff member name can't be null")
        String name,

        @NotNull(message = "The staff member surname can't be null")
        String surname,

        @NotNull(message = "The staff member email can't be null")
        String email,

        @NotNull(message = "The staff member password can't be null")
        @Size(min = 8, message = "The password must be at least 8 characters long")
        String password,

        @NotNull(message = "The staff member role can't be null")
        StaffRole role,

        Long medicalExaminationTypeId
) {
}
