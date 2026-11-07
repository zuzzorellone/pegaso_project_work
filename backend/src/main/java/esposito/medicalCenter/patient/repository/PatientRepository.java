package esposito.medicalCenter.patient.repository;

import esposito.medicalCenter.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findPatientEntityByTaxIdentificationNumber(String taxIdentificationNumber);
}
