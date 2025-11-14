package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for converting between PatientVisit entities and DTOs.
 */
@Component
public class PatientVisitMapper {
    
    /**
     * Converts request DTO to entity.
     *
     * @param request the request DTO
     * @return the entity
     */
    public PatientVisit toEntity(PatientVisitRequest request) {
        if (request == null) {
            return null;
        }
        PatientVisit visit = new PatientVisit();
        visit.setVisitTime(request.getVisitTime());
        visit.setVisitType(request.getVisitType());
        visit.setLocation(request.getLocation());
        visit.setReason(request.getReason());
        visit.setNotes(request.getNotes());
        return visit;
    }
    
    /**
     * Converts entity to response DTO.
     *
     * @param visit the entity
     * @return the response DTO
     */
    public PatientVisitResponse toResponse(PatientVisit visit) {
        if (visit == null) {
            return null;
        }
        PatientVisitResponse response = new PatientVisitResponse();
        response.setId(visit.getId());
        if (visit.getPatient() != null) {
            response.setPatientId(visit.getPatient().getId());
            response.setPatientName(visit.getPatient().getFirstName() + " " + visit.getPatient().getLastName());
        }
        if (visit.getPhysician() != null) {
            response.setPhysicianId(visit.getPhysician().getId());
            response.setPhysicianName(visit.getPhysician().getFirstName() + " " + visit.getPhysician().getLastName());
        }
        response.setVisitTime(visit.getVisitTime());
        response.setVisitType(visit.getVisitType());
        response.setLocation(visit.getLocation());
        response.setReason(visit.getReason());
        response.setNotes(visit.getNotes());
        response.setCreatedTime(convertToLocalDateTime(visit.getCreatedAt()));
        response.setModifiedTime(convertToLocalDateTime(visit.getUpdatedAt()));
        response.setCreatedBy(visit.getCreatedBy());
        response.setLastModifiedBy(visit.getUpdatedBy());
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
}

