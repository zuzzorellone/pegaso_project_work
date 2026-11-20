package esposito.medicalCenter.patient.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephoneNumber;

    @Column(unique = true, nullable = false)
    private String taxIdentificationNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;
}
