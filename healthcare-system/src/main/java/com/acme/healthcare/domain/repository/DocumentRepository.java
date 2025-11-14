package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.Document;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DocumentRepository persists document metadata.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Finds documents by patient id.
     *
     * @param patientId the patient identifier
     * @return the documents
     */
    List<Document> findByPatientId(Long patientId);

    /**
     * Finds documents by visit id.
     *
     * @param visitId the visit identifier
     * @return the documents
     */
    List<Document> findByVisitId(Long visitId);
}


