package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.service.dto.DocumentResponse;
import org.springframework.stereotype.Component;

/**
 * DocumentMapper converts document entities to DTOs.
 */
@Component
public class DocumentMapper {

    /**
     * Maps a document entity to response DTO.
     *
     * @param document the document entity
     * @return the response DTO
     */
    public DocumentResponse toResponse(final Document document) {
        if (document == null) {
            return null;
        }
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        if (document.getPatient() != null) {
            response.setPatientId(document.getPatient().getId());
        }
        if (document.getVisit() != null) {
            response.setVisitId(document.getVisit().getId());
        }
        response.setFileName(document.getFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        return response;
    }
}

