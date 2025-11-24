package com.acme.healthcare.service.impl;

import com.acme.healthcare.audit.entity.AuditableEntity;
import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.entity.VisitPatientAllergy;
import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import com.acme.healthcare.domain.entity.VisitPatientMedication;
import com.acme.healthcare.domain.repository.DocumentRepository;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.domain.repository.UserPatientAssignmentRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.domain.repository.VisitPatientAllergyRepository;
import com.acme.healthcare.domain.repository.VisitPatientDiagnosisRepository;
import com.acme.healthcare.domain.repository.VisitPatientMedicationRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.service.dto.AuditResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuditServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private UserPatientAssignmentRepository assignmentRepository;

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private VisitPatientDiagnosisRepository diagnosisRepository;

    @Mock
    private VisitPatientAllergyRepository allergyRepository;

    @Mock
    private VisitPatientMedicationRepository medicationRepository;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private AuditServiceImpl service;

    private Pageable pageable;
    private User user;
    private Patient patient;
    private Role role;
    private RolePermission rolePermission;
    private UserPatientAssignment assignment;
    private PatientVisit visit;
    private VisitPatientDiagnosis diagnosis;
    private VisitPatientAllergy allergy;
    private VisitPatientMedication medication;
    private Document document;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);
        user = new User();
        user.setId(1L);
        applyAuditMetadata(user);

        patient = new Patient();
        patient.setId(2L);
        applyAuditMetadata(patient);

        role = new Role();
        role.setId(3L);
        applyAuditMetadata(role);

        rolePermission = new RolePermission();
        rolePermission.setId(4L);
        applyAuditMetadata(rolePermission);

        assignment = new UserPatientAssignment();
        assignment.setId(5L);
        applyAuditMetadata(assignment);

        visit = new PatientVisit();
        visit.setId(6L);
        applyAuditMetadata(visit);

        diagnosis = new VisitPatientDiagnosis();
        diagnosis.setId(7L);
        applyAuditMetadata(diagnosis);

        allergy = new VisitPatientAllergy();
        allergy.setId(8L);
        applyAuditMetadata(allergy);

        medication = new VisitPatientMedication();
        medication.setId(9L);
        applyAuditMetadata(medication);

        document = new Document();
        document.setId(10L);
        applyAuditMetadata(document);
    }

    @Test
    void getAuditInfo_ForUser_ShouldReturnAuditResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        AuditResponse result = service.getAuditInfo("user", 1L);

        assertThat(result.getEntityType()).isEqualTo("user");
        assertThat(result.getEntityId()).isEqualTo(1L);
        assertThat(result.getCreatedBy()).isEqualTo("admin");
    }

    @Test
    void getAuditInfo_ForRole_ShouldReturnAuditResponse() {
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));

        AuditResponse result = service.getAuditInfo("role", 3L);

        assertThat(result.getEntityType()).isEqualTo("role");
        assertThat(result.getEntityId()).isEqualTo(3L);
    }

    @Test
    void getAuditInfo_WithUnknownType_ShouldThrow() {
        assertThrows(ResourceNotFoundException.class, () -> service.getAuditInfo("unsupported", 99L));
    }

    @Test
    void getAuditInfoByEntityType_ForUsers_ShouldReturnPagedResponses() {
        Page<User> users = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(users);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("user", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("User");
    }

    @Test
    void getAuditInfoByEntityType_ForPatients_ShouldReturnPagedResponses() {
        Page<Patient> patients = new PageImpl<>(List.of(patient), pageable, 1);
        when(patientRepository.findAll(pageable)).thenReturn(patients);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("patient", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("Patient");
    }

    @Test
    void getAuditInfoByEntityType_ForRoles_ShouldReturnPagedResponses() {
        Page<Role> roles = new PageImpl<>(List.of(role), pageable, 1);
        when(roleRepository.findAll(pageable)).thenReturn(roles);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("role", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("Role");
    }

    @Test
    void getAuditInfoByEntityType_ForRolePermissions_ShouldReturnPagedResponses() {
        Page<RolePermission> permissions = new PageImpl<>(List.of(rolePermission), pageable, 1);
        when(rolePermissionRepository.findAll(pageable)).thenReturn(permissions);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("role_permission", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("RolePermission");
    }

    @Test
    void getAuditInfoByEntityType_ForAssignments_ShouldReturnPagedResponses() {
        Page<UserPatientAssignment> assignments = new PageImpl<>(List.of(assignment), pageable, 1);
        when(assignmentRepository.findAll(pageable)).thenReturn(assignments);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("assignment", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("UserPatientAssignment");
    }

    @Test
    void getAuditInfoByEntityType_ForVisits_ShouldReturnPagedResponses() {
        Page<PatientVisit> visits = new PageImpl<>(List.of(visit), pageable, 1);
        when(visitRepository.findAll(pageable)).thenReturn(visits);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("visit", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("PatientVisit");
    }

    @Test
    void getAuditInfoByEntityType_ForDiagnoses_ShouldReturnPagedResponses() {
        Page<VisitPatientDiagnosis> diagnoses = new PageImpl<>(List.of(diagnosis), pageable, 1);
        when(diagnosisRepository.findAll(pageable)).thenReturn(diagnoses);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("diagnosis", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("VisitPatientDiagnosis");
    }

    @Test
    void getAuditInfoByEntityType_ForAllergies_ShouldReturnPagedResponses() {
        Page<VisitPatientAllergy> allergies = new PageImpl<>(List.of(allergy), pageable, 1);
        when(allergyRepository.findAll(pageable)).thenReturn(allergies);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("visit_patient_allergy", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("VisitPatientAllergy");
    }

    @Test
    void getAuditInfoByEntityType_ForMedications_ShouldReturnPagedResponses() {
        Page<VisitPatientMedication> medications = new PageImpl<>(List.of(medication), pageable, 1);
        when(medicationRepository.findAll(pageable)).thenReturn(medications);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("visit_patient_medication", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("VisitPatientMedication");
    }

    @Test
    void getAuditInfoByEntityType_ForDocuments_ShouldReturnPagedResponses() {
        Page<Document> documents = new PageImpl<>(List.of(document), pageable, 1);
        when(documentRepository.findAll(pageable)).thenReturn(documents);

        Page<AuditResponse> result = service.getAuditInfoByEntityType("document", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEntityType()).isEqualTo("Document");
    }

    @Test
    void getAuditInfoByEntityType_WithUnsupportedType_ShouldThrow() {
        assertThrows(ResourceNotFoundException.class,
            () -> service.getAuditInfoByEntityType("unsupported", pageable));
    }

    @Test
    void getAuditInfo_ShouldResolveAllEntityTypes() {
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));
        when(rolePermissionRepository.findById(4L)).thenReturn(Optional.of(rolePermission));
        when(assignmentRepository.findById(5L)).thenReturn(Optional.of(assignment));
        when(visitRepository.findById(6L)).thenReturn(Optional.of(visit));
        when(diagnosisRepository.findById(7L)).thenReturn(Optional.of(diagnosis));
        when(allergyRepository.findById(8L)).thenReturn(Optional.of(allergy));
        when(medicationRepository.findById(9L)).thenReturn(Optional.of(medication));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        assertThat(service.getAuditInfo("role", 3L).getEntityId()).isEqualTo(3L);
        assertThat(service.getAuditInfo("role_permission", 4L).getEntityId()).isEqualTo(4L);
        assertThat(service.getAuditInfo("user_patient_assignment", 5L).getEntityId()).isEqualTo(5L);
        assertThat(service.getAuditInfo("patient_visit", 6L).getEntityId()).isEqualTo(6L);
        assertThat(service.getAuditInfo("visit_patient_diagnosis", 7L).getEntityId()).isEqualTo(7L);
        assertThat(service.getAuditInfo("visit_patient_allergy", 8L).getEntityId()).isEqualTo(8L);
        assertThat(service.getAuditInfo("visit_patient_medication", 9L).getEntityId()).isEqualTo(9L);
        assertThat(service.getAuditInfo("document", 10L).getEntityId()).isEqualTo(10L);
    }

    @Test
    void getAuditInfoByUser_ShouldAggregateEntitiesCreatedByUser() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        Page<AuditResponse> result = service.getAuditInfoByUser("admin", pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).allMatch(response -> response.getCreatedBy().equals("admin"));
    }

    private void applyAuditMetadata(final AuditableEntity entity) {
        entity.setCreatedBy("admin");
        entity.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        entity.setUpdatedBy("auditor");
        entity.setUpdatedAt(Instant.parse("2024-01-02T00:00:00Z"));
        entity.setVersion(1L);
    }
}

