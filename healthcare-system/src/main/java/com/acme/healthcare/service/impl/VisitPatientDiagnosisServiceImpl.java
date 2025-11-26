package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientDiagnosisRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientDiagnosisMapper;
import com.acme.healthcare.service.VisitPatientDiagnosisService;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing visit patient diagnoses.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VisitPatientDiagnosisServiceImpl implements VisitPatientDiagnosisService {
    
    private final VisitPatientDiagnosisRepository diagnosisRepository;
    private final PatientVisitRepository visitRepository;
    private final VisitPatientDiagnosisMapper mapper;
    
    @Override
    public VisitPatientDiagnosisResponse create(VisitPatientDiagnosisRequest request) {
        PatientVisit visit = visitRepository.findById(request.getVisitId())
            .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
        
        VisitPatientDiagnosis diagnosis = mapper.toEntity(request);
        diagnosis.setVisit(visit);
        
        VisitPatientDiagnosis saved = diagnosisRepository.save(diagnosis);
        return mapper.toResponse(saved);
    }
    
    @Override
    public VisitPatientDiagnosisResponse update(Long id, VisitPatientDiagnosisRequest request) {
        VisitPatientDiagnosis diagnosis = diagnosisRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + id));
        
        if (request.getVisitId() != null) {
            PatientVisit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
            diagnosis.setVisit(visit);
        }
        if (request.getCode() != null) {
            diagnosis.setCode(request.getCode());
        }
        if (request.getDescription() != null) {
            diagnosis.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            diagnosis.setStatus(request.getStatus());
        }
        
        VisitPatientDiagnosis updated = diagnosisRepository.save(diagnosis);
        return mapper.toResponse(updated);
    }
    
    @Override
    @Transactional(readOnly = true)
    public VisitPatientDiagnosisResponse findById(Long id) {
        VisitPatientDiagnosis diagnosis = diagnosisRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + id));
        return mapper.toResponse(diagnosis);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientDiagnosisResponse> findAll(Pageable pageable) {
        return diagnosisRepository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<VisitPatientDiagnosisResponse> findByVisitId(Long visitId, Pageable pageable) {
        return diagnosisRepository.findByVisitId(visitId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    public void delete(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw new ResourceNotFoundException("Diagnosis not found with id: " + id);
        }
        diagnosisRepository.deleteById(id);
    }
}











