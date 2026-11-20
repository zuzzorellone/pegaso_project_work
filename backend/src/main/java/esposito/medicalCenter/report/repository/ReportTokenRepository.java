package esposito.medicalCenter.report.repository;

import esposito.medicalCenter.report.entity.ReportTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportTokenRepository extends JpaRepository<ReportTokenEntity, Long> {
    Optional<ReportTokenEntity> findReportTokenEntityByToken(String token);

    Optional<ReportTokenEntity> findByReportId(Long reportId);
}
