package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PatientRepository handles patient persistence.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a patient by medical record number.
     *
     * @param medicalRecordNumber the MRN
     * @return the patient if found
     */
    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);
}


