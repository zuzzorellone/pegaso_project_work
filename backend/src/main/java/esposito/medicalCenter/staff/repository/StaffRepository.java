package esposito.medicalCenter.staff.repository;

import esposito.medicalCenter.staff.StaffRole;
import esposito.medicalCenter.staff.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {

    Optional<StaffEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<StaffEntity> findByRoleOrderBySurnameAscNameAsc(StaffRole role);

    @Query("SELECT s.medicalExaminationTypeId, COUNT(s) FROM StaffEntity s WHERE s.role = :role AND s.active = true GROUP BY s.medicalExaminationTypeId")
    List<Object[]> countByRoleGroupedByDepartment(@Param("role") StaffRole role);
}
