package esposito.medicalCenter.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportPath;

    private String originalFilename;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "appointment_id", unique = true)
    private Long appointmentId;
}
