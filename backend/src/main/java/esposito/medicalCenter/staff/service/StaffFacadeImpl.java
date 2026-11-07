package esposito.medicalCenter.staff.service;

import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.core.exception.EmailAlreadyInUseException;
import esposito.medicalCenter.core.exception.InvalidStaffDataException;
import esposito.medicalCenter.staff.StaffRole;
import esposito.medicalCenter.staff.api.DoctorSummaryDTO;
import esposito.medicalCenter.staff.api.StaffFacade;
import esposito.medicalCenter.staff.api.RequestStaffDTO;
import esposito.medicalCenter.staff.api.ResponseStaffDTO;
import esposito.medicalCenter.staff.entity.StaffEntity;
import esposito.medicalCenter.staff.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StaffFacadeImpl implements StaffFacade {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentFacade appointmentFacade;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No staff member found with email: " + email));
    }

    @Override
    @Transactional
    public ResponseStaffDTO registerStaff(RequestStaffDTO requestStaffDTO) {
        if (staffRepository.existsByEmail(requestStaffDTO.email())) {
            throw new EmailAlreadyInUseException("A staff member with email " + requestStaffDTO.email() + " already exists");
        }

        Long medicalExaminationTypeId = requestStaffDTO.medicalExaminationTypeId();
        String medicalExaminationTypeName = null;

        if (requestStaffDTO.role() == StaffRole.DOCTOR) {
            if (medicalExaminationTypeId == null) {
                throw new InvalidStaffDataException("A department (medicalExaminationTypeId) is required for the DOCTOR role");
            }
            medicalExaminationTypeName = appointmentFacade.getMedicalExaminationTypeNameById(medicalExaminationTypeId);
        } else {
            medicalExaminationTypeId = null;
        }

        StaffEntity newStaff = new StaffEntity();
        newStaff.setName(requestStaffDTO.name());
        newStaff.setSurname(requestStaffDTO.surname());
        newStaff.setEmail(requestStaffDTO.email());
        newStaff.setPassword(passwordEncoder.encode(requestStaffDTO.password()));
        newStaff.setRole(requestStaffDTO.role());
        newStaff.setMedicalExaminationTypeId(medicalExaminationTypeId);

        StaffEntity savedStaff = staffRepository.save(newStaff);

        return mapEntityToDTO(savedStaff, medicalExaminationTypeName);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getActiveDoctorCountsByDepartment() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (Object[] row : staffRepository.countByRoleGroupedByDepartment(StaffRole.DOCTOR)) {
            Long medicalExaminationTypeId = (Long) row[0];
            Long count = (Long) row[1];
            if (medicalExaminationTypeId == null) {
                continue;
            }
            counts.put(appointmentFacade.getMedicalExaminationTypeNameById(medicalExaminationTypeId), count);
        }
        return counts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorSummaryDTO> getAllDoctors() {
        return staffRepository.findByRoleOrderBySurnameAscNameAsc(StaffRole.DOCTOR).stream()
                .map(staff -> new DoctorSummaryDTO(
                        staff.getId(),
                        staff.getName(),
                        staff.getSurname(),
                        staff.getEmail(),
                        staff.getMedicalExaminationTypeId(),
                        staff.getMedicalExaminationTypeId() == null ? null
                                : appointmentFacade.getMedicalExaminationTypeNameById(staff.getMedicalExaminationTypeId()),
                        staff.isActive()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void setDoctorActive(Long staffId, boolean active) {
        StaffEntity staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff member not found with ID: " + staffId));

        if (staff.getRole() != StaffRole.DOCTOR) {
            throw new EntityNotFoundException("Staff member not found with ID: " + staffId);
        }

        staff.setActive(active);
        staffRepository.save(staff);
    }

    private ResponseStaffDTO mapEntityToDTO(StaffEntity staffEntity, String medicalExaminationTypeName) {
        return new ResponseStaffDTO(
                staffEntity.getId(),
                staffEntity.getName(),
                staffEntity.getSurname(),
                staffEntity.getEmail(),
                staffEntity.getRole(),
                staffEntity.getMedicalExaminationTypeId(),
                medicalExaminationTypeName
        );
    }
}
