package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.PatientVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * PatientVisitRepository provides persistence for patient visits.
 */
@Repository
public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {
    
    /**
     * Finds visits by patient id with pagination.
     *
     * @param patientId the patient identifier
     * @param pageable pagination parameters
     * @return page of visits
     */
    @Query("SELECT v FROM PatientVisit v WHERE v.patient.id = :patientId")
    Page<PatientVisit> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
}

