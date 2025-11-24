package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserPatientAssignmentMapper}.
 */
class UserPatientAssignmentMapperTest {

    private final UserPatientAssignmentMapper mapper = new UserPatientAssignmentMapper();

    @Test
    void toEntity_ShouldCopyAssignmentFields() {
        UserPatientAssignmentRequest request = new UserPatientAssignmentRequest();
        request.setAssignmentRole(RoleType.NURSE.name());
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 12, 31));

        UserPatientAssignment assignment = mapper.toEntity(request);

        assertThat(assignment.getAssignmentRole()).isEqualTo(RoleType.NURSE);
        assertThat(assignment.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(assignment.getEndDate()).isEqualTo(LocalDate.of(2025, 12, 31));
    }

    @Test
    void toResponse_ShouldComposeNames() {
        User user = new User();
        user.setId(7L);
        user.setFirstName("Sam");
        user.setLastName("Taylor");

        Patient patient = new Patient();
        patient.setId(8L);
        patient.setFirstName("Alex");
        patient.setLastName("Brown");

        UserPatientAssignment assignment = new UserPatientAssignment();
        assignment.setId(3L);
        assignment.setUser(user);
        assignment.setPatient(patient);
        assignment.setAssignmentRole(RoleType.THERAPIST);
        assignment.setStartDate(LocalDate.of(2025, 2, 1));
        assignment.setEndDate(LocalDate.of(2025, 3, 1));
        assignment.setCreatedAt(Instant.parse("2025-02-01T00:00:00Z"));
        assignment.setUpdatedAt(Instant.parse("2025-02-15T00:00:00Z"));
        assignment.setCreatedBy("system");
        assignment.setUpdatedBy("admin");

        UserPatientAssignmentResponse response = mapper.toResponse(assignment);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getUserId()).isEqualTo(7L);
        assertThat(response.getUserName()).isEqualTo("Sam Taylor");
        assertThat(response.getPatientId()).isEqualTo(8L);
        assertThat(response.getPatientName()).isEqualTo("Alex Brown");
        assertThat(response.getCreatedTime()).isEqualTo(LocalDateTime.ofInstant(
            Instant.parse("2025-02-01T00:00:00Z"), ZoneId.systemDefault()));
        assertThat(response.getLastModifiedBy()).isEqualTo("admin");
        assertThat(response.getAssignmentRole()).isEqualTo(RoleType.THERAPIST.name());
    }
}


