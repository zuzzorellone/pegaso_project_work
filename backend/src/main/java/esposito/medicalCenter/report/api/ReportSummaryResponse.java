package esposito.medicalCenter.report.api;

import esposito.medicalCenter.report.entity.ReportEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummaryResponse {

    private Long id;
    private String filename;
    private Long appointmentId;
}
