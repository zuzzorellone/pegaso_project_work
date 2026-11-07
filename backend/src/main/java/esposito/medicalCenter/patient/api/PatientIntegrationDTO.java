package esposito.medicalCenter.patient.api;

public record PatientIntegrationDTO(

        Long id,
        String name,
        String surname,
        String email,
        String telephoneNumber,
        String taxIdentificationNumber
) {
}
