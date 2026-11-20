package esposito.medicalCenter.appointment.controller;

import esposito.medicalCenter.appointment.dto.RequestMedicalExaminationTypeDTO;
import esposito.medicalCenter.appointment.dto.ResponseMedicalExaminationTypeDTO;
import esposito.medicalCenter.appointment.service.MedicalExaminationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Departments")
@SecurityRequirements
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-examination-types")
public class MedicalExaminationTypeController {

    private final MedicalExaminationTypeService medicalExaminationTypeService;

    @Operation(summary = "List all departments")
    @GetMapping
    public ResponseEntity<List<ResponseMedicalExaminationTypeDTO>> getAllMedicalExaminationType() {
        return ResponseEntity.ok(medicalExaminationTypeService.getAllMedicalExaminationTypes());
    }

    @Operation(summary = "Get a department by ID")
    @ApiResponse(responseCode = "404", description = "Department not found")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMedicalExaminationTypeDTO> getMedicalExaminationTypeByID(@PathVariable Long id) {
        return ResponseEntity.ok(medicalExaminationTypeService.getMedicalExaminationTypeByID(id));
    }

    @Operation(summary = "Create a department", description = "Sets opening hours, visit length and open days.")
    @PostMapping
    public ResponseEntity<ResponseMedicalExaminationTypeDTO> createNewMedicalExaminationType(@Valid @RequestBody RequestMedicalExaminationTypeDTO requestDTO) {
        ResponseMedicalExaminationTypeDTO responseDTO = medicalExaminationTypeService.createMedicalExaminationType(requestDTO);

        return ResponseEntity.created(buildResourceUri(responseDTO.getId()))
                .body(responseDTO);
    }

    @Operation(summary = "Update a department")
    @ApiResponse(responseCode = "404", description = "Department not found")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMedicalExaminationTypeDTO> updateMedicalExaminationType(@PathVariable Long id,
                                                                                          @Valid @RequestBody RequestMedicalExaminationTypeDTO requestDTO) {
        return ResponseEntity
                .ok(medicalExaminationTypeService.updateMedicalExaminationType(id, requestDTO));
    }

    private URI buildResourceUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
