package esposito.medicalCenter.appointment.service;

import esposito.medicalCenter.appointment.dto.RequestMedicalExaminationTypeDTO;
import esposito.medicalCenter.appointment.dto.ResponseMedicalExaminationTypeDTO;
import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import esposito.medicalCenter.appointment.repository.MedicalExaminationTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalExaminationTypeServiceTest {

    private final MedicalExaminationTypeRepository repository = mock(MedicalExaminationTypeRepository.class);

    private final MedicalExaminationTypeService service = new MedicalExaminationTypeService(repository);

    @Test
    void getAllReturnsEveryType() {
        MedicalExaminationTypeEntity cardiology = new MedicalExaminationTypeEntity();
        cardiology.setName("CARDIOLOGY");
        MedicalExaminationTypeEntity dermatology = new MedicalExaminationTypeEntity();
        dermatology.setName("DERMATOLOGY");
        when(repository.findAll()).thenReturn(List.of(cardiology, dermatology));

        List<ResponseMedicalExaminationTypeDTO> result = service.getAllMedicalExaminationTypes();

        assertEquals(2, result.size());
        assertEquals("CARDIOLOGY", result.get(0).getName());
        assertEquals("DERMATOLOGY", result.get(1).getName());
    }

    @Test
    void getByIdReturnsTheType() {
        MedicalExaminationTypeEntity cardiology = new MedicalExaminationTypeEntity();
        cardiology.setId(1L);
        cardiology.setName("CARDIOLOGY");
        when(repository.findById(1L)).thenReturn(Optional.of(cardiology));

        assertEquals("CARDIOLOGY", service.getMedicalExaminationTypeByID(1L).getName());
    }

    @Test
    void getByIdOfMissingTypeFails() {
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getMedicalExaminationTypeByID(9L));
    }

    @Test
    void createRejectsAnInvalidDayName() {
        RequestMedicalExaminationTypeDTO request = new RequestMedicalExaminationTypeDTO(
                "CARDIOLOGY", "Cardiologia", true, 30, LocalTime.of(9, 0), LocalTime.of(13, 0), List.of("lunedi"));

        assertThrows(IllegalArgumentException.class, () -> service.createMedicalExaminationType(request));
    }
}
