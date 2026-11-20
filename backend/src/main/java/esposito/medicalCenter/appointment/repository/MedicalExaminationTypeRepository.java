package esposito.medicalCenter.appointment.repository;

import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalExaminationTypeRepository extends JpaRepository<MedicalExaminationTypeEntity, Long> {
    Optional<MedicalExaminationTypeEntity> findMedicalExaminationTypeEntityByName(String name);
}