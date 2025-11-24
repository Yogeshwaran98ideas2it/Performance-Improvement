package com.acme.healthcare.integration;

import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.enums.RoleType;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests covering the primary REST endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FullApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    @Autowired
    private VisitPatientDiagnosisRepository diagnosisRepository;

    @Autowired
    private VisitPatientAllergyRepository allergyRepository;

    @Autowired
    private VisitPatientMedicationRepository medicationRepository;

    @Autowired
    private UserPatientAssignmentRepository assignmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor authWith(String... authorities) {
        SimpleGrantedAuthority[] grantedAuthorities = Arrays.stream(authorities)
            .map(SimpleGrantedAuthority::new)
            .toArray(SimpleGrantedAuthority[]::new);
        return user("integration-tester").authorities(grantedAuthorities);
    }

    private RolePermission ensurePermission(String code) {
        return rolePermissionRepository.findByCode(code).orElseGet(() -> {
            // Create a default role if it doesn't exist for standalone permissions
            Role defaultRole = roleRepository.findByName(RoleType.ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleType.ADMIN);
                    role.setDescription("Default admin role for test permissions");
                    return roleRepository.save(role);
                });
            
            RolePermission permission = new RolePermission();
            permission.setCode(code);
            permission.setResource(code.split("_")[0]); // Extract resource from code like "PATIENT_READ"
            permission.setAction(code.contains("_READ") ? "READ" : code.contains("_MANAGE") ? "MANAGE" : "WRITE");
            permission.setRole(defaultRole);
            return rolePermissionRepository.save(permission);
        });
    }

    private Role ensureRole(String name, Set<RolePermission> permissions) {
        RoleType roleType = RoleType.valueOf(name.toUpperCase());
        return roleRepository.findByName(roleType).map(existing -> {
            if (permissions != null && !permissions.isEmpty()) {
                existing.getPermissions().addAll(permissions);
            }
            return existing;
        }).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleType);
            role.setDescription(name + " role");
            role.setPermissions(permissions != null ? new HashSet<>(permissions) : new HashSet<>());
            return roleRepository.save(role);
        });
    }

    private User ensureUser(String username, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("encoded");
        user.setEmail(username + "@example.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setRoles(role != null ? new HashSet<>(Set.of(role)) : new HashSet<>());
        user.setCreatedBy("tester");
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    private Patient ensurePatient(String mrn) {
        Patient patient = new Patient();
        patient.setMedicalRecordNumber(mrn);
        patient.setFirstName("Patient");
        patient.setLastName("Example");
        patient.setCreatedBy("tester");
        patient.setCreatedAt(Instant.now());
        return patientRepository.save(patient);
    }

    @Test
    void userEndpoints_ShouldSupportCrudFlow() throws Exception {
        RolePermission permission = ensurePermission("USER_READ");
        Role role = ensureRole("CLINICIAN", Set.of(permission));

        String payload = """
            {
              "username": "api-user",
              "password": "Secret123!",
              "email": "api-user@example.com",
              "firstName": "API",
              "lastName": "User",
              "roleIds": [%d]
            }
            """.formatted(role.getId());

        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .with(authWith("USER_MANAGE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("api-user"))
            .andReturn();

        JsonNode created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long userId = created.get("id").asLong();

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                .with(authWith("USER_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("api-user@example.com"));

        mockMvc.perform(get("/api/v1/users?page=0&size=5")
                .with(authWith("USER_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].username", Matchers.hasItem("api-user")));

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                .with(authWith("USER_MANAGE")))
            .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(userId)).isFalse();
    }

    @Test
    void patientEndpoints_ShouldSupportCrudFlow() throws Exception {
        String payload = """
            {
              "medicalRecordNumber": "MRN-100",
              "firstName": "Alice",
              "lastName": "Anderson"
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/patients")
                .with(authWith("PATIENT_MANAGE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.medicalRecordNumber").value("MRN-100"))
            .andReturn();

        long patientId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/patients/{id}", patientId)
                .with(authWith("PATIENT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Alice"));

        mockMvc.perform(get("/api/v1/patients?page=0&size=5")
                .with(authWith("PATIENT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].medicalRecordNumber", Matchers.hasItem("MRN-100")));

        mockMvc.perform(delete("/api/v1/patients/{id}", patientId)
                .with(authWith("PATIENT_MANAGE")))
            .andExpect(status().isNoContent());

        assertThat(patientRepository.existsById(patientId)).isFalse();
    }

    @Test
    void roleEndpoints_ShouldSupportCrudFlow() throws Exception {
        RolePermission permission = ensurePermission("ROLE_MANAGE");

        String payload = """
            {
              "name": "SUPERVISOR",
              "description": "Supervisory role",
              "permissionIds": [%d]
            }
            """.formatted(permission.getId());

        MvcResult createResult = mockMvc.perform(post("/api/v1/roles")
                .with(authWith("ROLE_MANAGE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("SUPERVISOR"))
            .andReturn();

        long roleId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/roles/{id}", roleId)
                .with(authWith("ROLE_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Supervisory role"));

        mockMvc.perform(get("/api/v1/roles")
                .with(authWith("ROLE_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].id", Matchers.hasItem((int) roleId)));

        mockMvc.perform(delete("/api/v1/roles/{id}", roleId)
                .with(authWith("ROLE_MANAGE")))
            .andExpect(status().isNoContent());

        assertThat(roleRepository.existsById(roleId)).isFalse();
    }

    @Test
    void permissionEndpoints_ShouldSupportCrudFlow() throws Exception {
        String payload = """
            {
              "code": "PATIENT_READ",
              "description": "Read patient data"
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/permissions")
                .with(authWith("PERMISSION_MANAGE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("PATIENT_READ"))
            .andReturn();

        long permissionId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/permissions/{id}", permissionId)
                .with(authWith("PERMISSION_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Read patient data"));

        mockMvc.perform(get("/api/v1/permissions")
                .with(authWith("PERMISSION_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].code", Matchers.hasItem("PATIENT_READ")));

        mockMvc.perform(delete("/api/v1/permissions/{id}", permissionId)
                .with(authWith("PERMISSION_MANAGE")))
            .andExpect(status().isNoContent());

        assertThat(rolePermissionRepository.existsById(permissionId)).isFalse();
    }

    @Test
    void assignmentEndpoints_ShouldSupportCrudFlow() throws Exception {
        RolePermission permission = ensurePermission("USER_READ");
        Role role = ensureRole("PHYSICIAN", Set.of(permission));
        User user = ensureUser("assignment-user", role);
        Patient patient = ensurePatient("MRN-ASSIGN");

        String payload = """
            {
              "userId": %d,
              "patientId": %d,
              "assignmentRole": "PHYSICIAN",
              "startDate": "2025-01-01"
            }
            """.formatted(user.getId(), patient.getId());

        MvcResult createResult = mockMvc.perform(post("/api/v1/assignments")
                .with(authWith("ASSIGNMENT_CREATE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.assignmentRole").value("PHYSICIAN"))
            .andReturn();

        long assignmentId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/assignments/{id}", assignmentId)
                .with(authWith("ASSIGNMENT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(user.getId()));

        mockMvc.perform(get("/api/v1/assignments/user/{userId}", user.getId())
                .with(authWith("ASSIGNMENT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(assignmentId));

        mockMvc.perform(delete("/api/v1/assignments/{id}", assignmentId)
                .with(authWith("ASSIGNMENT_DELETE")))
            .andExpect(status().isNoContent());

        assertThat(assignmentRepository.existsById(assignmentId)).isFalse();
    }

    @Test
    void visitEndpoints_ShouldSupportClinicalFlows() throws Exception {
        RolePermission permission = ensurePermission("VISIT_READ");
        Role role = ensureRole("ATTENDING", Set.of(permission));
        User physician = ensureUser("attending", role);
        Patient patient = ensurePatient("MRN-VISIT");

        String visitPayload = """
            {
              "patientId": %d,
              "physicianId": %d,
              "visitTime": "2025-01-05T09:00:00",
              "visitType": "Consultation"
            }
            """.formatted(patient.getId(), physician.getId());

        MvcResult visitResult = mockMvc.perform(post("/api/v1/visits")
                .with(authWith("VISIT_CREATE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(visitPayload))
            .andExpect(status().isCreated())
            .andReturn();

        long visitId = objectMapper.readTree(visitResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/visits/{id}", visitId)
                .with(authWith("VISIT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visitType").value("Consultation"));

        String diagnosisPayload = """
            {
              "visitId": %d,
              "code": "E11.9",
              "description": "Type 2 diabetes"
            }
            """.formatted(visitId);

        MvcResult diagnosisResult = mockMvc.perform(post("/api/v1/diagnoses")
                .with(authWith("DIAGNOSIS_CREATE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(diagnosisPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("E11.9"))
            .andReturn();

        long diagnosisId = objectMapper.readTree(diagnosisResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/diagnoses/{id}", diagnosisId)
                .with(authWith("DIAGNOSIS_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visitId").value(visitId));

        mockMvc.perform(get("/api/v1/diagnoses")
                .with(authWith("DIAGNOSIS_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].id", Matchers.hasItem((int) diagnosisId)));

        String allergyPayload = """
            {
              "visitId": %d,
              "allergen": "Peanuts",
              "reaction": "Hives"
            }
            """.formatted(visitId);

        MvcResult allergyResult = mockMvc.perform(post("/api/v1/allergies")
                .with(authWith("ALLERGY_CREATE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(allergyPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.allergen").value("Peanuts"))
            .andReturn();

        long allergyId = objectMapper.readTree(allergyResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/allergies")
                .with(authWith("ALLERGY_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].id", Matchers.hasItem((int) allergyId)));

        String medicationPayload = """
            {
              "visitId": %d,
              "medicationName": "Aspirin",
              "dosage": "100mg"
            }
            """.formatted(visitId);

        MvcResult medicationResult = mockMvc.perform(post("/api/v1/medications")
                .with(authWith("MEDICATION_CREATE"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicationPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.medicationName").value("Aspirin"))
            .andReturn();

        long medicationId = objectMapper.readTree(medicationResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/medications")
                .with(authWith("MEDICATION_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].id", Matchers.hasItem((int) medicationId)));

        mockMvc.perform(delete("/api/v1/medications/{id}", medicationId)
                .with(authWith("MEDICATION_DELETE")))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/allergies/{id}", allergyId)
                .with(authWith("ALLERGY_DELETE")))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/diagnoses/{id}", diagnosisId)
                .with(authWith("DIAGNOSIS_DELETE")))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/visits/{id}", visitId)
                .with(authWith("VISIT_DELETE")))
            .andExpect(status().isNoContent());

        assertThat(patientVisitRepository.existsById(visitId)).isFalse();
        assertThat(diagnosisRepository.existsById(diagnosisId)).isFalse();
        assertThat(allergyRepository.existsById(allergyId)).isFalse();
        assertThat(medicationRepository.existsById(medicationId)).isFalse();
    }

    @Test
    void documentEndpoints_ShouldHandleUploadAndRetrieval() throws Exception {
        Patient patient = ensurePatient("MRN-DOC");

        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            "lab.txt",
            "text/plain",
            "lab data".getBytes()
        );

        MvcResult uploadResult = mockMvc.perform(multipart("/api/v1/documents")
                .file(multipartFile)
                .param("patientId", String.valueOf(patient.getId()))
                .with(authWith("DOCUMENT_MANAGE")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fileName").value("lab.txt"))
            .andReturn();

        long documentId = objectMapper.readTree(uploadResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/v1/documents/{id}", documentId)
                .with(authWith("DOCUMENT_READ")))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"document_" + documentId + "\""));

        mockMvc.perform(get("/api/v1/documents/patient/{patientId}", patient.getId())
                .with(authWith("DOCUMENT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(documentId));

        mockMvc.perform(delete("/api/v1/documents/{id}", documentId)
                .with(authWith("DOCUMENT_MANAGE")))
            .andExpect(status().isNoContent());

        assertThat(documentRepository.existsById(documentId)).isFalse();
    }

    @Test
    void auditEndpoints_ShouldReturnAuditData() throws Exception {
        RolePermission permission = ensurePermission("AUDIT_READ");
        Role role = ensureRole("AUDITOR", Set.of(permission));
        User user = ensureUser("audited-user", role);
        user.setCreatedBy("auditor");
        user.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        userRepository.save(user);

        // Verify audit by user listing
        mockMvc.perform(get("/api/v1/audit/user/{username}", "auditor")
                .with(authWith("AUDIT_READ")))
            .andExpect(status().isOk());

        // Verify audit by entity type listing
        mockMvc.perform(get("/api/v1/audit/entity/user")
                .with(authWith("AUDIT_READ")))
            .andExpect(status().isOk());

        // Use a patient entity for single-entity audit to avoid route collision with /user/{username}
        Patient patient = ensurePatient("MRN-AUDIT-1");
        patient.setCreatedBy("auditor");
        patient.setCreatedAt(Instant.parse("2025-01-02T00:00:00Z"));
        patientRepository.save(patient);

        mockMvc.perform(get("/api/v1/audit/{entityType}/{entityId}", "patient", patient.getId())
                .with(authWith("AUDIT_READ")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.entityId").value(patient.getId()));
    }
}