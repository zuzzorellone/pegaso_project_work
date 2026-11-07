package esposito.medicalCenter.staff.api;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface StaffFacade extends UserDetailsService {

    ResponseStaffDTO registerStaff(RequestStaffDTO requestStaffDTO);

    Map<String, Long> getActiveDoctorCountsByDepartment();

    List<DoctorSummaryDTO> getAllDoctors();

    void setDoctorActive(Long staffId, boolean active);
}
