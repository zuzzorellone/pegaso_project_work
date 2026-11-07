package esposito.medicalCenter.report.repository;

import esposito.medicalCenter.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    Optional<ReportEntity> findByAppointmentId(Long appointmentId);

    List<ReportEntity> findByAppointmentIdIn(List<Long> appointmentIds);
}
