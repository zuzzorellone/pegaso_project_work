package esposito.medicalCenter.appointment.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalExaminationTypeEntityTest {

    @Test
    void displayNameUsesDescription() {
        MedicalExaminationTypeEntity type = new MedicalExaminationTypeEntity();
        type.setName("DERMATOLOGY");
        type.setDescription("Dermatologia");

        assertThat(type.getDisplayName()).isEqualTo("Dermatologia");
    }

    @Test
    void displayNameFallsBackToNameWhenDescriptionIsBlank() {
        MedicalExaminationTypeEntity type = new MedicalExaminationTypeEntity();
        type.setName("DERMATOLOGY");
        type.setDescription("  ");

        assertThat(type.getDisplayName()).isEqualTo("DERMATOLOGY");
    }
}
