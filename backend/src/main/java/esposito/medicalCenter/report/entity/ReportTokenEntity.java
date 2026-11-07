package esposito.medicalCenter.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reports_tokens")
public class ReportTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private int failedAttempts = 0;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationDate);
    }
}
