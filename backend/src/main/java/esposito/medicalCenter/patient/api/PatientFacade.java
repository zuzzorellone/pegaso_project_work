package esposito.medicalCenter.patient.api;


import java.util.Map;
import java.util.Set;

public interface PatientFacade {

    public PatientIntegrationDTO getOrCreatePatient(RequestPatientDTO requestPatientDTO);

    public Map<Long, PatientIntegrationDTO> getPatientListByIds(Set<Long> patientIdSet);
    public PatientIntegrationDTO getPatientById(Long id);
    public PatientIntegrationDTO getPatientByTaxIdentificationNumber(String taxIdentificationNumber);
}
