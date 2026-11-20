package esposito.medicalCenter.appointment.entity;

import esposito.medicalCenter.appointment.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus appointmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_examination_type_id", nullable = false)
    private MedicalExaminationTypeEntity medicalExaminationType;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;
}
