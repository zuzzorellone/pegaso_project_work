package esposito.medicalCenter.appointment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "medicalExaminationTypes")
public class MedicalExaminationTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private Integer examinationDuration;

    private LocalTime openTime;

    private LocalTime closeTime;

    @OneToMany(mappedBy = "examinationType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExaminationDayEntity> openingDays = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    public String getDisplayName() {
        return description != null && !description.isBlank() ? description : name;
    }

    public void addOpeningDay(DayOfWeek day) {
        ExaminationDayEntity examinationDay = new ExaminationDayEntity();
        examinationDay.setDayOfWeek(day);
        examinationDay.setExaminationType(this);
        this.openingDays.add(examinationDay);
    }
}
