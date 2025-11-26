package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientMedication;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientMedicationRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientMedicationMapper;
import com.acme.healthcare.service.VisitPatientMedicationService;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing visit patient medications.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VisitPatientMedicationServiceImpl implements VisitPatientMedicationService {
    
    private final VisitPatientMedicationRepository medicationRepository;
    private final PatientVisitRepository visitRepository;
    private final VisitPatientMedicationMapper mapper;
    
    @Override
    public VisitPatientMedicationResponse create(VisitPatientMedicationRequest request) {
        PatientVisit visit = visitRepository.findById(request.getVisitId())
            .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
        
        VisitPatientMedication medication = mapper.toEntity(request);
        medication.setVisit(visit);
        
        VisitPatientMedication saved = medicationRepository.save(medication);
        return mapper.toResponse(saved);
    }
    
    @Override
    public VisitPatientMedicationResponse update(Long id, VisitPatientMedicationRequest request) {
        VisitPatientMedication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));
        
        if (request.getVisitId() != null) {
            PatientVisit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
            medication.setVisit(visit);
        }
        if (request.getMedicationName() != null) {
            medication.setMedicationName(request.getMedicationName());
        }
        if (request.getDosage() != null) {
            medication.setDosage(request.getDosage());
        }
        if (request.getFrequency() != null) {
            medication.setFrequency(request.getFrequency());
        }
        if (request.getRoute() != null) {
            medication.setRoute(request.getRoute());
        }
        if (request.getInstructions() != null) {
            medication.setInstructions(request.getInstructions());
        }
        
        VisitPatientMedication updated = medicationRepository.save(medication);
        return mapper.toResponse(updated);
    }
    
    @Override
    @Transactional(readOnly = true)
    public VisitPatientMedicationResponse findById(Long id) {
        VisitPatientMedication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));
        return mapper.toResponse(medication);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientMedicationResponse> findAll(Pageable pageable) {
        return medicationRepository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientMedicationResponse> findByVisitId(Long visitId, Pageable pageable) {
        return medicationRepository.findByVisitId(visitId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    public void delete(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medication not found with id: " + id);
        }
        medicationRepository.deleteById(id);
    }
}











