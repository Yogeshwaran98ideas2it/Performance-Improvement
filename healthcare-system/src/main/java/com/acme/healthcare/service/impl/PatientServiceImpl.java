package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.mapper.PatientMapper;
import com.acme.healthcare.service.PatientService;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PatientServiceImpl implements patient business logic.
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    /**
     * Creates the service implementation.
     *
     * @param patientRepository the patient repository
     * @param userRepository the user repository
     * @param patientMapper the patient mapper
     */
    public PatientServiceImpl(final PatientRepository patientRepository,
                              final UserRepository userRepository,
                              final PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.patientMapper = patientMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientResponse create(final PatientRequest request) {
        Patient patient = new Patient();
        applyRequest(request, patient);
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientResponse update(final Long id, final PatientRequest request) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        applyRequest(request, patient);
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PatientResponse get(final Long id) {
        return patientRepository.findById(id)
            .map(patientMapper::toResponse)
            .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> list(final Pageable pageable) {
        return patientRepository.findAll(pageable).map(patientMapper::toResponse);
    }

    private void applyRequest(final PatientRequest request, final Patient patient) {
        patient.setMedicalRecordNumber(request.getMedicalRecordNumber());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPrimaryPhone(request.getPrimaryPhone());
        patient.setEmail(request.getEmail());
        patient.setAddressLine1(request.getAddressLine1());
        // addressLine2 not in database - handled in mapper
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setPostalCode(request.getPostalCode());
        patient.setCountry(request.getCountry());
        patient.setVitalSigns(request.getVitalSigns());
        patient.setPastMedicalHistory(request.getPastMedicalHistory());
        if (request.getPrimaryPhysicianId() != null) {
            User primary = userRepository.findById(request.getPrimaryPhysicianId())
                .orElseThrow(() -> new EntityNotFoundException("Primary physician not found"));
            patient.setPrimaryPhysician(primary);
        } else {
            patient.setPrimaryPhysician(null);
        }
        if (request.getSecondaryPhysicianIds() != null) {
            List<User> secondary = userRepository.findAllById(request.getSecondaryPhysicianIds());
            validatePhysicians(request.getSecondaryPhysicianIds(), secondary);
            patient.setSecondaryPhysicians(new HashSet<>(secondary));
        }
        if (request.getReferralPhysicianIds() != null) {
            List<User> referral = userRepository.findAllById(request.getReferralPhysicianIds());
            validatePhysicians(request.getReferralPhysicianIds(), referral);
            patient.setReferralPhysicians(new HashSet<>(referral));
        }
    }

    private void validatePhysicians(final Set<Long> expectedIds, final List<User> foundUsers) {
        if (foundUsers.size() != expectedIds.size()) {
            throw new EntityNotFoundException("One or more physicians not found");
        }
    }
}





