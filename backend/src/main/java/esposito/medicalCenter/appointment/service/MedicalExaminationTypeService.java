package esposito.medicalCenter.appointment.service;

import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import esposito.medicalCenter.appointment.repository.MedicalExaminationTypeRepository;
import esposito.medicalCenter.appointment.dto.RequestMedicalExaminationTypeDTO;
import esposito.medicalCenter.appointment.dto.ResponseMedicalExaminationTypeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalExaminationTypeService {

    private final MedicalExaminationTypeRepository medicalExaminationTypeRepository;

    @Transactional(readOnly = true)
    public List<ResponseMedicalExaminationTypeDTO> getAllMedicalExaminationTypes() {
        return medicalExaminationTypeRepository.findAll().stream().map(
                ResponseMedicalExaminationTypeDTO::new
        ).toList();
    }

    @Transactional(readOnly = true)
    public ResponseMedicalExaminationTypeDTO getMedicalExaminationTypeByID(Long id) {
        MedicalExaminationTypeEntity entity = medicalExaminationTypeRepository.findById(id)
                .orElseThrow(
                () -> new EntityNotFoundException("Medical Examination Type Not Found With ID: " + id)
        );

        return new ResponseMedicalExaminationTypeDTO(entity);
    }

    @Transactional
    public ResponseMedicalExaminationTypeDTO createMedicalExaminationType(RequestMedicalExaminationTypeDTO requestMedicalExaminationTypeDTO) {
        MedicalExaminationTypeEntity medicalExaminationTypeEntity = new MedicalExaminationTypeEntity();
        medicalExaminationTypeEntity.setName(requestMedicalExaminationTypeDTO.name());
        medicalExaminationTypeEntity.setDescription(requestMedicalExaminationTypeDTO.description());
        medicalExaminationTypeEntity.setExaminationDuration(requestMedicalExaminationTypeDTO.duration());
        medicalExaminationTypeEntity.setOpenTime(requestMedicalExaminationTypeDTO.openTime());
        medicalExaminationTypeEntity.setCloseTime(requestMedicalExaminationTypeDTO.closeTime());

        if (requestMedicalExaminationTypeDTO.daysOfWeek() != null) {
            for (String day : requestMedicalExaminationTypeDTO.daysOfWeek()) {
                medicalExaminationTypeEntity.addOpeningDay(DayOfWeek.valueOf(day.trim().toUpperCase()));
            }
        }else {
            for (DayOfWeek day : DayOfWeek.values()) {
                medicalExaminationTypeEntity.addOpeningDay(DayOfWeek.valueOf(day.name().toUpperCase()));
            }
        }

        medicalExaminationTypeEntity =  medicalExaminationTypeRepository.save(medicalExaminationTypeEntity);

        return new ResponseMedicalExaminationTypeDTO(medicalExaminationTypeEntity);
    }

    @Transactional
    public ResponseMedicalExaminationTypeDTO updateMedicalExaminationType(Long id, RequestMedicalExaminationTypeDTO requestMedicalExaminationTypeDTO) {
        MedicalExaminationTypeEntity entity = medicalExaminationTypeRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Medical Examination Type Not Found With ID: " + id)
                );

        entity.setDescription(requestMedicalExaminationTypeDTO.description());
        entity.setName(requestMedicalExaminationTypeDTO.name());
        entity.setExaminationDuration(requestMedicalExaminationTypeDTO.duration());
        entity.setOpenTime(requestMedicalExaminationTypeDTO.openTime());
        entity.setCloseTime(requestMedicalExaminationTypeDTO.closeTime());
        entity.setActive(requestMedicalExaminationTypeDTO.active() != null ? requestMedicalExaminationTypeDTO.active() : entity.getActive());

        entity.getOpeningDays().clear();
        if (requestMedicalExaminationTypeDTO.daysOfWeek() != null) {
            for (String day : requestMedicalExaminationTypeDTO.daysOfWeek()) {
                entity.addOpeningDay(DayOfWeek.valueOf(day.trim().toUpperCase()));
            }
        } else {
            for (DayOfWeek day : DayOfWeek.values()) {
                entity.addOpeningDay(day);
            }
        }

        entity = medicalExaminationTypeRepository.save(entity);

        return new ResponseMedicalExaminationTypeDTO(entity);
    }
}
