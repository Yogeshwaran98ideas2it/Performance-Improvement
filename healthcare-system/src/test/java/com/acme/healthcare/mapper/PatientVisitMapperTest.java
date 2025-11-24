package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PatientVisitMapper}.
 */
class PatientVisitMapperTest {

    private final PatientVisitMapper mapper = new PatientVisitMapper();

    @Test
    void toEntity_ShouldCopyVisitDetails() {
        PatientVisitRequest request = new PatientVisitRequest();
        request.setPatientId(1L);
        request.setPhysicianId(2L);
        request.setVisitTime(LocalDateTime.of(2025, 1, 1, 9, 30));
        request.setVisitType("Consultation");
        request.setLocation("Room 101");
        request.setReason("Checkup");
        request.setNotes("Bring reports");

        PatientVisit visit = mapper.toEntity(request);

        assertThat(visit.getVisitTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 9, 30));
        assertThat(visit.getVisitType()).isEqualTo("Consultation");
        assertThat(visit.getLocation()).isEqualTo("Room 101");
        assertThat(visit.getReason()).isEqualTo("Checkup");
        assertThat(visit.getNotes()).isEqualTo("Bring reports");
    }

    @Test
    void toResponse_ShouldComposeNamesAndTimestamps() {
        Patient patient = new Patient();
        patient.setId(10L);
        patient.setFirstName("Jane");
        patient.setLastName("Doe");

        User physician = new User();
        physician.setId(20L);
        physician.setFirstName("Sam");
        physician.setLastName("Smith");

        PatientVisit visit = new PatientVisit();
        visit.setId(5L);
        visit.setPatient(patient);
        visit.setPhysician(physician);
        visit.setVisitTime(LocalDateTime.of(2025, 1, 2, 14, 0));
        visit.setVisitType("Follow-up");
        visit.setLocation("Telehealth");
        visit.setReason("Review labs");
        visit.setCreatedAt(Instant.parse("2025-01-01T10:00:00Z"));
        visit.setUpdatedAt(Instant.parse("2025-01-03T08:45:00Z"));
        visit.setCreatedBy("system");
        visit.setUpdatedBy("nurse");

        PatientVisitResponse response = mapper.toResponse(visit);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getPatientId()).isEqualTo(10L);
        assertThat(response.getPatientName()).isEqualTo("Jane Doe");
        assertThat(response.getPhysicianId()).isEqualTo(20L);
        assertThat(response.getPhysicianName()).isEqualTo("Sam Smith");
        assertThat(response.getCreatedTime()).isEqualTo(LocalDateTime.ofInstant(
            Instant.parse("2025-01-01T10:00:00Z"), ZoneId.systemDefault()));
        assertThat(response.getLastModifiedBy()).isEqualTo("nurse");
    }
}










