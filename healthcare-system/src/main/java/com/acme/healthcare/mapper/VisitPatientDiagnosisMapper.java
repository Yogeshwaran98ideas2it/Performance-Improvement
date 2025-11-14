package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Mapper for converting between VisitPatientDiagnosis entities and DTOs.
 */
@Component
public class VisitPatientDiagnosisMapper {
    
    /**
     * Converts request DTO to entity.
     *
     * @param request the request DTO
     * @return the entity
     */
    public VisitPatientDiagnosis toEntity(VisitPatientDiagnosisRequest request) {
        if (request == null) {
            return null;
        }
        VisitPatientDiagnosis diagnosis = new VisitPatientDiagnosis();
        diagnosis.setCode(request.getCode());
        diagnosis.setDescription(request.getDescription());
        diagnosis.setStatus(request.getStatus());
        return diagnosis;
    }
    
    /**
     * Converts entity to response DTO.
     *
     * @param diagnosis the entity
     * @return the response DTO
     */
    public VisitPatientDiagnosisResponse toResponse(VisitPatientDiagnosis diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        VisitPatientDiagnosisResponse response = new VisitPatientDiagnosisResponse();
        response.setId(diagnosis.getId());
        if (diagnosis.getVisit() != null) {
            response.setVisitId(diagnosis.getVisit().getId());
        }
        response.setCode(diagnosis.getCode());
        response.setDescription(diagnosis.getDescription());
        response.setStatus(diagnosis.getStatus());
        response.setCreatedTime(convertToLocalDateTime(diagnosis.getCreatedAt()));
        response.setModifiedTime(convertToLocalDateTime(diagnosis.getUpdatedAt()));
        response.setCreatedBy(diagnosis.getCreatedBy());
        response.setLastModifiedBy(diagnosis.getUpdatedBy());
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

