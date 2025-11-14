package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.VisitPatientMedication;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for converting between VisitPatientMedication entities and DTOs.
 */
@Component
public class VisitPatientMedicationMapper {
    
    /**
     * Converts request DTO to entity.
     *
     * @param request the request DTO
     * @return the entity
     */
    public VisitPatientMedication toEntity(VisitPatientMedicationRequest request) {
        if (request == null) {
            return null;
        }
        VisitPatientMedication medication = new VisitPatientMedication();
        medication.setMedicationName(request.getMedicationName());
        medication.setDosage(request.getDosage());
        medication.setFrequency(request.getFrequency());
        medication.setRoute(request.getRoute());
        medication.setInstructions(request.getInstructions());
        return medication;
    }
    
    /**
     * Converts entity to response DTO.
     *
     * @param medication the entity
     * @return the response DTO
     */
    public VisitPatientMedicationResponse toResponse(VisitPatientMedication medication) {
        if (medication == null) {
            return null;
        }
        VisitPatientMedicationResponse response = new VisitPatientMedicationResponse();
        response.setId(medication.getId());
        if (medication.getVisit() != null) {
            response.setVisitId(medication.getVisit().getId());
        }
        response.setMedicationName(medication.getMedicationName());
        response.setDosage(medication.getDosage());
        response.setFrequency(medication.getFrequency());
        response.setRoute(medication.getRoute());
        response.setInstructions(medication.getInstructions());
        response.setCreatedTime(convertToLocalDateTime(medication.getCreatedAt()));
        response.setModifiedTime(convertToLocalDateTime(medication.getUpdatedAt()));
        response.setCreatedBy(medication.getCreatedBy());
        response.setLastModifiedBy(medication.getUpdatedBy());
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

