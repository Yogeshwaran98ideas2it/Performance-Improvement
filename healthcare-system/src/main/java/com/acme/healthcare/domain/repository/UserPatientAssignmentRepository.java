package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.UserPatientAssignment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * UserPatientAssignmentRepository manages provider assignments.
 */
@Repository
public interface UserPatientAssignmentRepository extends JpaRepository<UserPatientAssignment, Long> {

    /**
     * Finds assignments by user id.
     *
     * @param userId the user identifier
     * @return the assignments
     */
    List<UserPatientAssignment> findByUserId(Long userId);

    /**
     * Finds assignments by user id with pagination.
     *
     * @param userId the user identifier
     * @param pageable pagination parameters
     * @return page of assignments
     */
    @Query("SELECT a FROM UserPatientAssignment a WHERE a.user.id = :userId")
    Page<UserPatientAssignment> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Finds assignments by patient id.
     *
     * @param patientId the patient identifier
     * @return the assignments
     */
    List<UserPatientAssignment> findByPatientId(Long patientId);

    /**
     * Finds assignments by patient id with pagination.
     *
     * @param patientId the patient identifier
     * @param pageable pagination parameters
     * @return page of assignments
     */
    @Query("SELECT a FROM UserPatientAssignment a WHERE a.patient.id = :patientId")
    Page<UserPatientAssignment> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
}

