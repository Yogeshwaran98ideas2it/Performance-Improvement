package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientAllergy;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientAllergyRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientAllergyMapper;
import com.acme.healthcare.service.VisitPatientAllergyService;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing visit patient allergies.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VisitPatientAllergyServiceImpl implements VisitPatientAllergyService {
    
    private final VisitPatientAllergyRepository allergyRepository;
    private final PatientVisitRepository visitRepository;
    private final VisitPatientAllergyMapper mapper;
    
    @Override
    public VisitPatientAllergyResponse create(VisitPatientAllergyRequest request) {
        PatientVisit visit = visitRepository.findById(request.getVisitId())
            .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
        
        VisitPatientAllergy allergy = mapper.toEntity(request);
        allergy.setVisit(visit);
        
        VisitPatientAllergy saved = allergyRepository.save(allergy);
        return mapper.toResponse(saved);
    }
    
    @Override
    public VisitPatientAllergyResponse update(Long id, VisitPatientAllergyRequest request) {
        VisitPatientAllergy allergy = allergyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + id));
        
        if (request.getVisitId() != null) {
            PatientVisit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
            allergy.setVisit(visit);
        }
        if (request.getAllergen() != null) {
            allergy.setAllergen(request.getAllergen());
        }
        if (request.getReaction() != null) {
            allergy.setReaction(request.getReaction());
        }
        if (request.getSeverity() != null) {
            allergy.setSeverity(request.getSeverity());
        }
        
        VisitPatientAllergy updated = allergyRepository.save(allergy);
        return mapper.toResponse(updated);
    }
    
    @Override
    @Transactional(readOnly = true)
    public VisitPatientAllergyResponse findById(Long id) {
        VisitPatientAllergy allergy = allergyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + id));
        return mapper.toResponse(allergy);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientAllergyResponse> findAll(Pageable pageable) {
        return allergyRepository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientAllergyResponse> findByVisitId(Long visitId, Pageable pageable) {
        return allergyRepository.findByVisitId(visitId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    public void delete(Long id) {
        if (!allergyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Allergy not found with id: " + id);
        }
        allergyRepository.deleteById(id);
    }
}










