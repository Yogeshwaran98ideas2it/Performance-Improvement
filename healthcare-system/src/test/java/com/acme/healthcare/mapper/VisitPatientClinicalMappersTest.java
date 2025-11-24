package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientAllergy;
import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import com.acme.healthcare.domain.entity.VisitPatientMedication;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for manual visit-patient mappers.
 */
class VisitPatientClinicalMappersTest {

    private final VisitPatientDiagnosisMapper diagnosisMapper = new VisitPatientDiagnosisMapper();
    private final VisitPatientAllergyMapper allergyMapper = new VisitPatientAllergyMapper();
    private final VisitPatientMedicationMapper medicationMapper = new VisitPatientMedicationMapper();

    @Test
    void diagnosisMapper_ShouldMapEntityAndResponse() {
        VisitPatientDiagnosisRequest request = new VisitPatientDiagnosisRequest();
        request.setVisitId(11L);
        request.setCode("A01");
        request.setDescription("Diagnosis");
        request.setStatus("ACTIVE");

        VisitPatientDiagnosis diagnosis = diagnosisMapper.toEntity(request);
        assertThat(diagnosis.getCode()).isEqualTo("A01");
        assertThat(diagnosis.getDescription()).isEqualTo("Diagnosis");
        assertThat(diagnosis.getStatus()).isEqualTo("ACTIVE");

        PatientVisit visit = new PatientVisit();
        visit.setId(11L);
        diagnosis.setId(5L);
        diagnosis.setVisit(visit);
        diagnosis.setCreatedAt(Instant.parse("2025-01-01T10:15:30Z"));
        diagnosis.setUpdatedAt(Instant.parse("2025-01-02T08:00:00Z"));
        diagnosis.setCreatedBy("system");
        diagnosis.setUpdatedBy("admin");

        VisitPatientDiagnosisResponse response = diagnosisMapper.toResponse(diagnosis);
        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getVisitId()).isEqualTo(11L);
        assertThat(response.getCode()).isEqualTo("A01");
        assertThat(response.getCreatedTime()).isEqualTo(LocalDateTime.ofInstant(
            Instant.parse("2025-01-01T10:15:30Z"), ZoneId.systemDefault()));
        assertThat(response.getLastModifiedBy()).isEqualTo("admin");
    }

    @Test
    void allergyMapper_ShouldMapEntityAndResponse() {
        VisitPatientAllergyRequest request = new VisitPatientAllergyRequest();
        request.setVisitId(9L);
        request.setAllergen("Peanuts");
        request.setReaction("Hives");
        request.setSeverity("Severe");

        VisitPatientAllergy allergy = allergyMapper.toEntity(request);
        assertThat(allergy.getAllergen()).isEqualTo("Peanuts");
        assertThat(allergy.getReaction()).isEqualTo("Hives");
        assertThat(allergy.getSeverity()).isEqualTo("Severe");

        PatientVisit visit = new PatientVisit();
        visit.setId(9L);
        allergy.setId(6L);
        allergy.setVisit(visit);
        allergy.setCreatedAt(Instant.parse("2025-02-01T00:00:00Z"));

        VisitPatientAllergyResponse response = allergyMapper.toResponse(allergy);
        assertThat(response.getId()).isEqualTo(6L);
        assertThat(response.getVisitId()).isEqualTo(9L);
        assertThat(response.getAllergen()).isEqualTo("Peanuts");
        assertThat(response.getCreatedTime()).isEqualTo(LocalDateTime.ofInstant(
            Instant.parse("2025-02-01T00:00:00Z"), ZoneId.systemDefault()));
    }

    @Test
    void medicationMapper_ShouldMapEntityAndResponse() {
        VisitPatientMedicationRequest request = new VisitPatientMedicationRequest();
        request.setVisitId(7L);
        request.setMedicationName("Aspirin");
        request.setDosage("100mg");
        request.setFrequency("Once daily");
        request.setRoute("Oral");
        request.setInstructions("Take with water");

        VisitPatientMedication medication = medicationMapper.toEntity(request);
        assertThat(medication.getMedicationName()).isEqualTo("Aspirin");
        assertThat(medication.getRoute()).isEqualTo("Oral");

        PatientVisit visit = new PatientVisit();
        visit.setId(7L);
        medication.setId(8L);
        medication.setVisit(visit);
        medication.setCreatedAt(Instant.parse("2025-03-01T05:30:00Z"));
        medication.setUpdatedAt(Instant.parse("2025-03-02T06:45:00Z"));
        medication.setCreatedBy("nurse");
        medication.setUpdatedBy("doctor");

        VisitPatientMedicationResponse response = medicationMapper.toResponse(medication);
        assertThat(response.getId()).isEqualTo(8L);
        assertThat(response.getVisitId()).isEqualTo(7L);
        assertThat(response.getInstructions()).isEqualTo("Take with water");
        assertThat(response.getCreatedBy()).isEqualTo("nurse");
        assertThat(response.getModifiedTime()).isEqualTo(LocalDateTime.ofInstant(
            Instant.parse("2025-03-02T06:45:00Z"), ZoneId.systemDefault()));
    }
}










