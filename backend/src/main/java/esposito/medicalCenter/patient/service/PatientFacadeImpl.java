package esposito.medicalCenter.patient.service;

import esposito.medicalCenter.patient.api.RequestPatientDTO;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import esposito.medicalCenter.patient.entity.PatientEntity;
import esposito.medicalCenter.patient.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientFacadeImpl implements PatientFacade {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientIntegrationDTO getOrCreatePatient(RequestPatientDTO requestPatientDTO) {
        // the tax code identifies the person; an existing patient's contacts are never overwritten from a booking,
        // otherwise anyone knowing a tax code could redirect that patient's report emails
        String taxIdentificationNumber = requestPatientDTO.taxIdentificationNumber().trim().toUpperCase();
        PatientEntity patientEntity = patientRepository.findPatientEntityByTaxIdentificationNumber(taxIdentificationNumber)
                .orElseGet(() -> {
                    PatientEntity newPatient = new PatientEntity();
                    newPatient.setName(requestPatientDTO.name());
                    newPatient.setSurname(requestPatientDTO.surname());
                    newPatient.setEmail(requestPatientDTO.email());
                    newPatient.setTelephoneNumber(requestPatientDTO.telephoneNumber());
                    newPatient.setTaxIdentificationNumber(taxIdentificationNumber);
                    return patientRepository.save(newPatient);
                });

        return mapEntityToDTO(patientEntity);
    }

    @Override
    public PatientIntegrationDTO getPatientById(Long id) {
        PatientEntity patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: "+ id));

        return mapEntityToDTO(patientEntity);
    }

    @Override
    public Map<Long, PatientIntegrationDTO> getPatientListByIds(Set<Long> patientIdSet) {
        Map<Long, PatientIntegrationDTO> mapToReturn = new HashMap<>();

        patientRepository.findAllById(patientIdSet).forEach(patientEntity ->
            {
                mapToReturn.put(patientEntity.getId(),
                        mapEntityToDTO(patientEntity));
            }
        );

        return mapToReturn;
    }

    @Override
    public PatientIntegrationDTO getPatientByTaxIdentificationNumber(String taxIdentificationNumber) {
        PatientEntity patientEntity = patientRepository.findPatientEntityByTaxIdentificationNumber(taxIdentificationNumber)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with tax identification number: "+ taxIdentificationNumber));

        return mapEntityToDTO(patientEntity);
    }

    private PatientIntegrationDTO mapEntityToDTO(PatientEntity patientEntity) {
        return new PatientIntegrationDTO(patientEntity.getId(),
                patientEntity.getName(),
                patientEntity.getSurname(),
                patientEntity.getEmail(),
                patientEntity.getTelephoneNumber(),
                patientEntity.getTaxIdentificationNumber());
    }
}
