package esposito.medicalCenter.appointment.dto;

import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ResponseMedicalExaminationTypeDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean isActive = Boolean.TRUE;
    private Integer examinationDuration;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<Integer> daysOfWeek = new LinkedList<>();

    public ResponseMedicalExaminationTypeDTO(MedicalExaminationTypeEntity medicalExaminationTypeEntity) {
        setId(medicalExaminationTypeEntity.getId());
        setName(medicalExaminationTypeEntity.getName());
        setDescription(medicalExaminationTypeEntity.getDescription());
        setIsActive(medicalExaminationTypeEntity.getActive());
        setExaminationDuration(medicalExaminationTypeEntity.getExaminationDuration());
        setOpenTime(medicalExaminationTypeEntity.getOpenTime());
        setCloseTime(medicalExaminationTypeEntity.getCloseTime());

        if(medicalExaminationTypeEntity.getOpeningDays() != null) {
            medicalExaminationTypeEntity.getOpeningDays()
                    .forEach(day -> {
                                daysOfWeek.add(day.getDayOfWeek().getValue());
                            }
                    );
        }

    }

}
