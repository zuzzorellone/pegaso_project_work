package esposito.medicalCenter.staff.api;

import esposito.medicalCenter.staff.StaffRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStaffDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private StaffRole role;
    private Long medicalExaminationTypeId;
    private String medicalExaminationTypeName;
}
