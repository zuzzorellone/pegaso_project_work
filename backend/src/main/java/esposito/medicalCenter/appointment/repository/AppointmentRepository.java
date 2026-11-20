package esposito.medicalCenter.appointment.repository;

import esposito.medicalCenter.appointment.AppointmentStatus;
import esposito.medicalCenter.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT a FROM AppointmentEntity a WHERE a.appointmentDate > :now AND a.medicalExaminationType.name = :examinationType " +
            "AND a.appointmentStatus <> esposito.medicalCenter.appointment.AppointmentStatus.CANCELLED")
    List<AppointmentEntity> getAllAppointmentDateTimeList(@Param("examinationType") String examinationType,
                                                      @Param("now") LocalDateTime now);

    @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE a.appointmentDate >= :start " +
            "AND a.appointmentDate <= :end " +
            "AND a.medicalExaminationType.name = :examinationTypeName " +
            "AND a.appointmentStatus <> esposito.medicalCenter.appointment.AppointmentStatus.CANCELLED")
    List<AppointmentEntity> findByAppointmentDateBetweenAndExaminationTypeName(
            LocalDateTime start,
            LocalDateTime end,
            String examinationTypeName
    );

    List<AppointmentEntity> findByMedicalExaminationType_IdAndAppointmentStatusInAndAppointmentDateAfterOrderByAppointmentDateAsc(
            Long medicalExaminationTypeId, Collection<AppointmentStatus> statuses, LocalDateTime now);

    List<AppointmentEntity> findByMedicalExaminationType_IdAndAppointmentStatusInAndAppointmentDateBeforeOrderByAppointmentDateDesc(
            Long medicalExaminationTypeId, Collection<AppointmentStatus> statuses, LocalDateTime now);

    List<AppointmentEntity> findByAppointmentDateBeforeOrderByAppointmentDateDesc(LocalDateTime now);

    List<AppointmentEntity> findByAppointmentStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    @Modifying
    @Query("UPDATE AppointmentEntity a SET a.appointmentStatus = :to WHERE a.id = :id AND a.appointmentStatus = :from")
    int updateStatusIfCurrent(@Param("id") Long id, @Param("from") AppointmentStatus from, @Param("to") AppointmentStatus to);

    @Query("SELECT a.medicalExaminationType.name, COUNT(a) FROM AppointmentEntity a " +
            "WHERE a.appointmentStatus <> esposito.medicalCenter.appointment.AppointmentStatus.CANCELLED " +
            "GROUP BY a.medicalExaminationType.name")
    List<Object[]> countAppointmentsByDepartment();

    @Query("SELECT a.medicalExaminationType.name, COUNT(a) FROM AppointmentEntity a " +
            "WHERE a.appointmentStatus = esposito.medicalCenter.appointment.AppointmentStatus.WAITING_FOR_REPORT " +
            "GROUP BY a.medicalExaminationType.name")
    List<Object[]> countWaitingForReportByDepartment();

    @Query("SELECT FUNCTION('to_char', a.appointmentDate, 'YYYY-MM'), COUNT(a) " +
            "FROM AppointmentEntity a WHERE a.appointmentDate >= :since " +
            "AND a.appointmentStatus <> esposito.medicalCenter.appointment.AppointmentStatus.CANCELLED " +
            "GROUP BY FUNCTION('to_char', a.appointmentDate, 'YYYY-MM')")
    List<Object[]> countAppointmentsByMonth(@Param("since") LocalDateTime since);

    @Query("SELECT FUNCTION('to_char', a.appointmentDate, 'YYYY-MM-DD'), COUNT(a) " +
            "FROM AppointmentEntity a WHERE a.appointmentDate >= :since " +
            "AND a.appointmentStatus <> esposito.medicalCenter.appointment.AppointmentStatus.CANCELLED " +
            "GROUP BY FUNCTION('to_char', a.appointmentDate, 'YYYY-MM-DD')")
    List<Object[]> countAppointmentsByDay(@Param("since") LocalDateTime since);
}
