package esposito.medicalCenter.appointment.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalExaminationTypeEntityTest {

    @Test
    void displayNameIsTheDescription() {
        MedicalExaminationTypeEntity type = new MedicalExaminationTypeEntity();
        type.setName("DERMATOLOGY");
        type.setDescription("Dermatologia");

        assertThat(type.getDisplayName()).isEqualTo("Dermatologia");
    }

    @Test
    void displayNameFallsBackToTheNameWithoutDescription() {
        MedicalExaminationTypeEntity type = new MedicalExaminationTypeEntity();
        type.setName("DERMATOLOGY");
        type.setDescription("  ");

        assertThat(type.getDisplayName()).isEqualTo("DERMATOLOGY");
    }
}
