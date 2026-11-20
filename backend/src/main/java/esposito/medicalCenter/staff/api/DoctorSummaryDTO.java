package esposito.medicalCenter.staff.api;

public record DoctorSummaryDTO(
        Long id,
        String name,
        String surname,
        String email,
        Long medicalExaminationTypeId,
        String medicalExaminationTypeName,
        boolean active
) {
}
