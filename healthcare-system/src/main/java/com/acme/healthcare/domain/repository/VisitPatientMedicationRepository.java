package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.VisitPatientMedication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * VisitPatientMedicationRepository manages medication data.
 */
@Repository
public interface VisitPatientMedicationRepository extends JpaRepository<VisitPatientMedication, Long> {
    
    /**
     * Finds medications by visit id with pagination.
     *
     * @param visitId the visit identifier
     * @param pageable pagination parameters
     * @return page of medications
     */
    @Query("SELECT m FROM VisitPatientMedication m WHERE m.visit.id = :visitId")
    Page<VisitPatientMedication> findByVisitId(@Param("visitId") Long visitId, Pageable pageable);
}

