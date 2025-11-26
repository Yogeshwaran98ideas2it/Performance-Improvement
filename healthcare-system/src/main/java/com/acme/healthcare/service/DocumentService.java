package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.DocumentResponse;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * DocumentService manages document storage and retrieval.
 */
public interface DocumentService {

    /**
     * Uploads a document and persists metadata.
     *
     * @param patientId the patient identifier
     * @param visitId the visit identifier
     * @param file the multipart file
     * @return the document response
     */
    DocumentResponse upload(Long patientId, Long visitId, MultipartFile file);

    /**
     * Downloads document contents as input stream.
     *
     * @param documentId the document identifier
     * @return the input stream
     */
    InputStream download(Long documentId);

    /**
     * Lists documents for a patient.
     *
     * @param patientId the patient identifier
     * @return the document list
     */
    List<DocumentResponse> listByPatient(Long patientId);

    /**
     * Lists documents for a visit.
     *
     * @param visitId the visit identifier
     * @return the document list
     */
    List<DocumentResponse> listByVisit(Long visitId);

    /**
     * Deletes a document.
     *
     * @param documentId the document identifier
     */
    void delete(Long documentId);
}











