package com.acme.healthcare.service.impl;

import com.acme.healthcare.audit.entity.AuditableEntity;
import com.acme.healthcare.domain.entity.*;
import com.acme.healthcare.domain.repository.*;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.service.AuditService;
import com.acme.healthcare.service.dto.AuditResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for retrieving audit information.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {
    
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserPatientAssignmentRepository assignmentRepository;
    private final PatientVisitRepository visitRepository;
    private final VisitPatientDiagnosisRepository diagnosisRepository;
    private final VisitPatientAllergyRepository allergyRepository;
    private final VisitPatientMedicationRepository medicationRepository;
    private final DocumentRepository documentRepository;
    
    @Override
    public AuditResponse getAuditInfo(String entityType, Long entityId) {
        AuditableEntity entity = findEntity(entityType, entityId);
        return toAuditResponse(entityType, entityId, entity);
    }
    
    @Override
    public Page<AuditResponse> getAuditInfoByEntityType(String entityType, Pageable pageable) {
        List<AuditResponse> auditResponses = new ArrayList<>();
        
        switch (entityType.toLowerCase()) {
            case "user":
                Page<User> users = userRepository.findAll(pageable);
                users.forEach(user -> auditResponses.add(toAuditResponse("User", user.getId(), user)));
                return new PageImpl<>(auditResponses, pageable, users.getTotalElements());
            case "patient":
                Page<Patient> patients = patientRepository.findAll(pageable);
                patients.forEach(patient -> auditResponses.add(toAuditResponse("Patient", patient.getId(), patient)));
                return new PageImpl<>(auditResponses, pageable, patients.getTotalElements());
            case "role":
                Page<Role> roles = roleRepository.findAll(pageable);
                roles.forEach(role -> auditResponses.add(toAuditResponse("Role", role.getId(), role)));
                return new PageImpl<>(auditResponses, pageable, roles.getTotalElements());
            case "rolepermission":
            case "role_permission":
                Page<RolePermission> permissions = rolePermissionRepository.findAll(pageable);
                permissions.forEach(perm -> auditResponses.add(toAuditResponse("RolePermission", perm.getId(), perm)));
                return new PageImpl<>(auditResponses, pageable, permissions.getTotalElements());
            case "assignment":
            case "user_patient_assignment":
                Page<UserPatientAssignment> assignments = assignmentRepository.findAll(pageable);
                assignments.forEach(ass -> auditResponses.add(toAuditResponse("UserPatientAssignment", ass.getId(), ass)));
                return new PageImpl<>(auditResponses, pageable, assignments.getTotalElements());
            case "visit":
            case "patient_visit":
                Page<PatientVisit> visits = visitRepository.findAll(pageable);
                visits.forEach(visit -> auditResponses.add(toAuditResponse("PatientVisit", visit.getId(), visit)));
                return new PageImpl<>(auditResponses, pageable, visits.getTotalElements());
            case "diagnosis":
            case "visit_patient_diagnosis":
                Page<VisitPatientDiagnosis> diagnoses = diagnosisRepository.findAll(pageable);
                diagnoses.forEach(diag -> auditResponses.add(toAuditResponse("VisitPatientDiagnosis", diag.getId(), diag)));
                return new PageImpl<>(auditResponses, pageable, diagnoses.getTotalElements());
            case "allergy":
            case "visit_patient_allergy":
                Page<VisitPatientAllergy> allergies = allergyRepository.findAll(pageable);
                allergies.forEach(all -> auditResponses.add(toAuditResponse("VisitPatientAllergy", all.getId(), all)));
                return new PageImpl<>(auditResponses, pageable, allergies.getTotalElements());
            case "medication":
            case "visit_patient_medication":
                Page<VisitPatientMedication> medications = medicationRepository.findAll(pageable);
                medications.forEach(med -> auditResponses.add(toAuditResponse("VisitPatientMedication", med.getId(), med)));
                return new PageImpl<>(auditResponses, pageable, medications.getTotalElements());
            case "document":
                Page<Document> documents = documentRepository.findAll(pageable);
                documents.forEach(doc -> auditResponses.add(toAuditResponse("Document", doc.getId(), doc)));
                return new PageImpl<>(auditResponses, pageable, documents.getTotalElements());
            default:
                throw new ResourceNotFoundException("Unknown entity type: " + entityType);
        }
    }
    
    @Override
    public Page<AuditResponse> getAuditInfoByUser(String username, Pageable pageable) {
        List<AuditResponse> auditResponses = new ArrayList<>();
        
        // Query all entity types and filter by username
        // This is a simplified implementation - in production, you might want to use a more efficient approach
        userRepository.findAll().forEach(user -> {
            if (user.getCreatedBy() != null && user.getCreatedBy().equals(username) ||
                user.getUpdatedBy() != null && user.getUpdatedBy().equals(username)) {
                auditResponses.add(toAuditResponse("User", user.getId(), user));
            }
        });
        
        patientRepository.findAll().forEach(patient -> {
            if (patient.getCreatedBy() != null && patient.getCreatedBy().equals(username) ||
                patient.getUpdatedBy() != null && patient.getUpdatedBy().equals(username)) {
                auditResponses.add(toAuditResponse("Patient", patient.getId(), patient));
            }
        });
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), auditResponses.size());
        List<AuditResponse> paged = auditResponses.subList(start, end);
        
        return new PageImpl<>(paged, pageable, auditResponses.size());
    }
    
    private AuditableEntity findEntity(String entityType, Long entityId) {
        return switch (entityType.toLowerCase()) {
            case "user" -> userRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + entityId));
            case "patient" -> patientRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + entityId));
            case "role" -> roleRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + entityId));
            case "rolepermission", "role_permission" -> rolePermissionRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("RolePermission not found with id: " + entityId));
            case "assignment", "user_patient_assignment" -> assignmentRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + entityId));
            case "visit", "patient_visit" -> visitRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + entityId));
            case "diagnosis", "visit_patient_diagnosis" -> diagnosisRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + entityId));
            case "allergy", "visit_patient_allergy" -> allergyRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + entityId));
            case "medication", "visit_patient_medication" -> medicationRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + entityId));
            case "document" -> documentRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + entityId));
            default -> throw new ResourceNotFoundException("Unknown entity type: " + entityType);
        };
    }
    
    private AuditResponse toAuditResponse(String entityType, Long entityId, AuditableEntity entity) {
        AuditResponse response = new AuditResponse();
        response.setEntityType(entityType);
        response.setEntityId(entityId);
        response.setCreatedBy(entity.getCreatedBy());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedBy(entity.getUpdatedBy());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setVersion(entity.getVersion());
        return response;
    }
}










