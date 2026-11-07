package esposito.medicalCenter.patient.service;

import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import esposito.medicalCenter.patient.api.RequestPatientDTO;
import esposito.medicalCenter.patient.entity.PatientEntity;
import esposito.medicalCenter.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientFacadeImplTest {

    private final PatientRepository repository = mock(PatientRepository.class);
    private final PatientFacadeImpl facade = new PatientFacadeImpl(repository);

    private static PatientEntity existing() {
        PatientEntity p = new PatientEntity();
        p.setId(7L);
        p.setName("Giulia");
        p.setSurname("Bianchi");
        p.setEmail("giulia@example.com");
        p.setTelephoneNumber("321987654321");
        p.setTaxIdentificationNumber("BNCGLI93L62H501Q");
        return p;
    }

    @Test
    void knownTaxCodeReusesThePatientWithoutChangingTheirContacts() {
        when(repository.findPatientEntityByTaxIdentificationNumber("BNCGLI93L62H501Q")).thenReturn(Optional.of(existing()));

        PatientIntegrationDTO result = facade.getOrCreatePatient(new RequestPatientDTO(
                "Giulia", "Bianchi", "someone.else@example.com", "3330000000", " bncgli93l62h501q "));

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.email()).isEqualTo("giulia@example.com");
        verify(repository, never()).save(any());
    }

    @Test
    void newTaxCodeCreatesAPatientEvenWithAnEmailOrPhoneAlreadyInUse() {
        when(repository.findPatientEntityByTaxIdentificationNumber("RSSMRC85C15F205K")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> {
            PatientEntity saved = inv.getArgument(0);
            saved.setId(99L);
            return saved;
        });

        PatientIntegrationDTO result = facade.getOrCreatePatient(new RequestPatientDTO(
                "Marco", "Rossi", "giulia@example.com", "321987654321", "rssmrc85c15f205k"));

        ArgumentCaptor<PatientEntity> captor = ArgumentCaptor.forClass(PatientEntity.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getTaxIdentificationNumber()).isEqualTo("RSSMRC85C15F205K");
        assertThat(captor.getValue().getName()).isEqualTo("Marco");
        assertThat(result.id()).isEqualTo(99L);
    }
}
