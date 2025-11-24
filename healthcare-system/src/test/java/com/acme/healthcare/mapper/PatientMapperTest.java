package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PatientMapper}.
 */
class PatientMapperTest {

    private final PatientMapper mapper = new PatientMapper();

    @Test
    void toResponse_ShouldMapPrimaryPhysicianId() {
        User physician = new User();
        physician.setId(99L);
        physician.setFirstName("Sam");
        physician.setLastName("Smith");

        Patient patient = new Patient();
        patient.setId(5L);
        patient.setMedicalRecordNumber("MRN-5");
        patient.setPrimaryPhysician(physician);
        patient.setVitalSigns(Map.of("BP", "120/80"));

        PatientResponse response = mapper.toResponse(patient);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getPrimaryPhysicianId()).isEqualTo(99L);
        assertThat(response.getVitalSigns()).containsEntry("BP", "120/80");
    }

    @Test
    void updateEntity_ShouldCopyEditableFields() {
        Patient patient = new Patient();
        patient.setMedicalRecordNumber("OLD");
        patient.setFirstName("Jane");
        patient.setLastName("Doe");
        patient.setCity("Old City");

        PatientRequest request = new PatientRequest();
        request.setMedicalRecordNumber("NEW");
        request.setFirstName("Janet");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setCity("New City");
        request.setVitalSigns(Map.of("HR", "75"));
        request.setSecondaryPhysicianIds(Set.of(1L, 2L));
        request.setReferralPhysicianIds(Set.of(3L));

        mapper.updateEntity(request, patient);

        assertThat(patient.getMedicalRecordNumber()).isEqualTo("NEW");
        assertThat(patient.getFirstName()).isEqualTo("Janet");
        assertThat(patient.getLastName()).isEqualTo("Smith");
        assertThat(patient.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(patient.getCity()).isEqualTo("New City");
        assertThat(patient.getVitalSigns()).containsEntry("HR", "75");
    }
}


