package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * VisitPatientDiagnosisRepository handles diagnosis persistence.
 */
@Repository
public interface VisitPatientDiagnosisRepository extends JpaRepository<VisitPatientDiagnosis, Long> {
    
    /**
     * Finds diagnoses by visit id with pagination.
     *
     * @param visitId the visit identifier
     * @param pageable pagination parameters
     * @return page of diagnoses
     */
    @Query("SELECT d FROM VisitPatientDiagnosis d WHERE d.visit.id = :visitId")
    Page<VisitPatientDiagnosis> findByVisitId(@Param("visitId") Long visitId, Pageable pageable);
}

