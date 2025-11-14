package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for converting between UserPatientAssignment entities and DTOs.
 */
@Component
public class UserPatientAssignmentMapper {
    
    /**
     * Converts request DTO to entity.
     *
     * @param request the request DTO
     * @return the entity
     */
    public UserPatientAssignment toEntity(UserPatientAssignmentRequest request) {
        if (request == null) {
            return null;
        }
        UserPatientAssignment assignment = new UserPatientAssignment();
        assignment.setAssignmentRole(parseRole(request.getAssignmentRole()));
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        return assignment;
    }
    
    /**
     * Converts entity to response DTO.
     *
     * @param assignment the entity
     * @return the response DTO
     */
    public UserPatientAssignmentResponse toResponse(UserPatientAssignment assignment) {
        if (assignment == null) {
            return null;
        }
        UserPatientAssignmentResponse response = new UserPatientAssignmentResponse();
        response.setId(assignment.getId());
        if (assignment.getUser() != null) {
            response.setUserId(assignment.getUser().getId());
            response.setUserName(assignment.getUser().getFirstName() + " " + assignment.getUser().getLastName());
        }
        if (assignment.getPatient() != null) {
            response.setPatientId(assignment.getPatient().getId());
            response.setPatientName(assignment.getPatient().getFirstName() + " " + assignment.getPatient().getLastName());
        }
        response.setAssignmentRole(assignment.getAssignmentRole() != null ? assignment.getAssignmentRole().name() : null);
        response.setStartDate(assignment.getStartDate());
        response.setEndDate(assignment.getEndDate());
        response.setCreatedTime(convertToLocalDateTime(assignment.getCreatedAt()));
        response.setModifiedTime(convertToLocalDateTime(assignment.getUpdatedAt()));
        response.setCreatedBy(assignment.getCreatedBy());
        response.setLastModifiedBy(assignment.getUpdatedBy());
        return response;
    }
    
    /**
     * Converts Instant to LocalDateTime.
     *
     * @param instant the instant to convert
     * @return the LocalDateTime or null
     */
    private LocalDateTime convertToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }

    private RoleType parseRole(final String role) {
        if (role == null || role.isBlank()) {
            return null;
        }
        return RoleType.valueOf(role.toUpperCase());
    }
}

