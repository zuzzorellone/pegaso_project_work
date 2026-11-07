package esposito.medicalCenter.appointment.service;

import esposito.medicalCenter.appointment.AppointmentStatus;
import esposito.medicalCenter.appointment.api.AppointmentFacade;
import esposito.medicalCenter.appointment.api.AppointmentIntegrationDTO;
import esposito.medicalCenter.appointment.entity.AppointmentEntity;
import esposito.medicalCenter.appointment.entity.MedicalExaminationTypeEntity;
import esposito.medicalCenter.appointment.repository.AppointmentRepository;
import esposito.medicalCenter.appointment.repository.MedicalExaminationTypeRepository;
import esposito.medicalCenter.core.exception.InvalidAppointmentStatusException;
import esposito.medicalCenter.patient.api.PatientFacade;
import esposito.medicalCenter.patient.api.PatientIntegrationDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentFacadeImpl implements AppointmentFacade {

    private static final Set<AppointmentStatus> DOCTOR_VISIBLE_STATUSES =
            EnumSet.of(AppointmentStatus.CONFIRMED, AppointmentStatus.WAITING_FOR_REPORT, AppointmentStatus.COMPLETED);

    private final AppointmentRepository appointmentRepository;
    private final MedicalExaminationTypeRepository medicalExaminationTypeRepository;
    private final PatientFacade patientFacade;
    private final AppointmentMailService appointmentMailService;

    @Override
    public void existsById(Long id) {
        appointmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Appointment Found with id: " + id)
        );
    }

    @Override
    public Long getPatientIdByAppointment(Long id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Appoaintment Found with id: "+ id)
        ).getPatientId();
    }

    @Override
    public String getMedicalExaminationTypeNameById(Long medicalExaminationTypeId) {
        return medicalExaminationTypeRepository.findById(medicalExaminationTypeId)
                .map(MedicalExaminationTypeEntity::getName)
                .orElseThrow(() -> new EntityNotFoundException("Medical Examination Type Not Found With ID: " + medicalExaminationTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMedicalExaminationTypeIdByAppointmentId(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId))
                .getMedicalExaminationType().getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentIntegrationDTO> getFutureAppointmentsForExaminationType(Long medicalExaminationTypeId) {
        return mapEntitiesToIntegrationDTOs(
                appointmentRepository.findByMedicalExaminationType_IdAndAppointmentStatusInAndAppointmentDateAfterOrderByAppointmentDateAsc(
                        medicalExaminationTypeId, DOCTOR_VISIBLE_STATUSES, LocalDateTime.now())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentIntegrationDTO> getPastAppointmentsForExaminationType(Long medicalExaminationTypeId) {
        return mapEntitiesToIntegrationDTOs(
                appointmentRepository.findByMedicalExaminationType_IdAndAppointmentStatusInAndAppointmentDateBeforeOrderByAppointmentDateDesc(
                        medicalExaminationTypeId, DOCTOR_VISIBLE_STATUSES, LocalDateTime.now())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentIntegrationDTO> getAllPastAppointments() {
        return mapEntitiesToIntegrationDTOs(
                appointmentRepository.findByAppointmentDateBeforeOrderByAppointmentDateDesc(LocalDateTime.now())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getAppointmentCountsByDepartment() {
        return rowsToMap(appointmentRepository.countAppointmentsByDepartment());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getMonthlyAppointmentCounts(LocalDateTime since) {
        return rowsToMap(appointmentRepository.countAppointmentsByMonth(since));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getDailyAppointmentCounts(LocalDateTime since) {
        return rowsToMap(appointmentRepository.countAppointmentsByDay(since));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentIntegrationDTO> getAppointmentsWaitingForConfirmation() {
        return mapEntitiesToIntegrationDTOs(
                appointmentRepository.findByAppointmentStatusOrderByAppointmentDateAsc(AppointmentStatus.WAITING_FOR_CONFIRMATION)
        );
    }

    @Override
    @Transactional
    public void confirmAppointment(Long appointmentId) {
        AppointmentEntity appointment = findAppointment(appointmentId);
        transition(appointment, AppointmentStatus.WAITING_FOR_CONFIRMATION, AppointmentStatus.CONFIRMED);

        PatientIntegrationDTO patient = patientFacade.getPatientById(appointment.getPatientId());
        appointmentMailService.sendAppointmentConfirmedEmail(
                patient.email(),
                patient.name() + " " + patient.surname(),
                appointment.getMedicalExaminationType().getDisplayName(),
                appointment.getAppointmentDate()
        );
    }

    @Override
    @Transactional
    public void rejectAppointment(Long appointmentId) {
        AppointmentEntity appointment = findAppointment(appointmentId);
        transition(appointment, AppointmentStatus.WAITING_FOR_CONFIRMATION, AppointmentStatus.CANCELLED);

        PatientIntegrationDTO patient = patientFacade.getPatientById(appointment.getPatientId());
        appointmentMailService.sendAppointmentRejectedEmail(
                patient.email(),
                patient.name() + " " + patient.surname(),
                appointment.getMedicalExaminationType().getDisplayName(),
                appointment.getAppointmentDate()
        );
    }

    @Override
    @Transactional
    public void markPatientAttended(Long appointmentId) {
        AppointmentEntity appointment = findAppointment(appointmentId);
        if (appointment.getAppointmentDate().toLocalDate().isAfter(LocalDate.now())) {
            throw new InvalidAppointmentStatusException(
                    "Attendance for appointment " + appointmentId + " can only be confirmed on or after the appointment day");
        }
        transition(appointment, AppointmentStatus.CONFIRMED, AppointmentStatus.WAITING_FOR_REPORT);
    }

    @Override
    @Transactional
    public void markReportUploaded(Long appointmentId) {
        AppointmentEntity appointment = findAppointment(appointmentId);
        AppointmentStatus current = appointment.getAppointmentStatus();
        if (current != AppointmentStatus.WAITING_FOR_REPORT && current != AppointmentStatus.COMPLETED) {
            throw new InvalidAppointmentStatusException(
                    "A report can't be uploaded for appointment " + appointmentId + " while it is " + current);
        }
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getWaitingForReportCountsByDepartment() {
        return rowsToMap(appointmentRepository.countWaitingForReportByDepartment());
    }

    private AppointmentEntity findAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
    }

    private void transition(AppointmentEntity appointment, AppointmentStatus from, AppointmentStatus to) {
        if (appointmentRepository.updateStatusIfCurrent(appointment.getId(), from, to) == 0) {
            throw new InvalidAppointmentStatusException(
                    "Appointment " + appointment.getId() + " is no longer " + from);
        }
    }

    private Map<String, Long> rowsToMap(List<Object[]> rows) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }

    private List<AppointmentIntegrationDTO> mapEntitiesToIntegrationDTOs(List<AppointmentEntity> appointments) {
        Set<Long> patientIds = appointments.stream()
                .map(AppointmentEntity::getPatientId)
                .collect(Collectors.toSet());

        Map<Long, PatientIntegrationDTO> patientMap = patientFacade.getPatientListByIds(patientIds);

        return appointments.stream()
                .map(appointment -> mapEntityToIntegrationDTO(appointment, patientMap.get(appointment.getPatientId())))
                .toList();
    }

    private AppointmentIntegrationDTO mapEntityToIntegrationDTO(AppointmentEntity appointmentEntity, PatientIntegrationDTO patient) {
        return new AppointmentIntegrationDTO(
                appointmentEntity.getId(),
                appointmentEntity.getAppointmentDate(),
                appointmentEntity.getAppointmentStatus().name(),
                appointmentEntity.getMedicalExaminationType().getName(),
                appointmentEntity.getMedicalExaminationType().getId(),
                appointmentEntity.getPatientId(),
                patient != null ? patient.name() : null,
                patient != null ? patient.surname() : null,
                patient != null ? patient.telephoneNumber() : null,
                patient != null ? patient.taxIdentificationNumber() : null
        );
    }
}
