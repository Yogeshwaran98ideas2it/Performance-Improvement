package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.VisitPatientAllergy;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for converting between VisitPatientAllergy entities and DTOs.
 */
@Component
public class VisitPatientAllergyMapper {
    
    /**
     * Converts request DTO to entity.
     *
     * @param request the request DTO
     * @return the entity
     */
    public VisitPatientAllergy toEntity(VisitPatientAllergyRequest request) {
        if (request == null) {
            return null;
        }
        VisitPatientAllergy allergy = new VisitPatientAllergy();
        allergy.setAllergen(request.getAllergen());
        allergy.setReaction(request.getReaction());
        allergy.setSeverity(request.getSeverity());
        return allergy;
    }
    
    /**
     * Converts entity to response DTO.
     *
     * @param allergy the entity
     * @return the response DTO
     */
    public VisitPatientAllergyResponse toResponse(VisitPatientAllergy allergy) {
        if (allergy == null) {
            return null;
        }
        VisitPatientAllergyResponse response = new VisitPatientAllergyResponse();
        response.setId(allergy.getId());
        if (allergy.getVisit() != null) {
            response.setVisitId(allergy.getVisit().getId());
        }
        response.setAllergen(allergy.getAllergen());
        response.setReaction(allergy.getReaction());
        response.setSeverity(allergy.getSeverity());
        response.setCreatedTime(convertToLocalDateTime(allergy.getCreatedAt()));
        response.setModifiedTime(convertToLocalDateTime(allergy.getUpdatedAt()));
        response.setCreatedBy(allergy.getCreatedBy());
        response.setLastModifiedBy(allergy.getUpdatedBy());
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

