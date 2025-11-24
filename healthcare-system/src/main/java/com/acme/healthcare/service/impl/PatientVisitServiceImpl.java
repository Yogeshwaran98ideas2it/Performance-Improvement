package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.PatientVisitMapper;
import com.acme.healthcare.service.PatientVisitService;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing patient visits.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PatientVisitServiceImpl implements PatientVisitService {
    
    private final PatientVisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientVisitMapper mapper;
    
    @Override
    public PatientVisitResponse create(PatientVisitRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
        
        PatientVisit visit = mapper.toEntity(request);
        visit.setPatient(patient);
        
        if (request.getPhysicianId() != null) {
            User physician = userRepository.findById(request.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Physician not found with id: " + request.getPhysicianId()));
            visit.setPhysician(physician);
        }
        
        PatientVisit saved = visitRepository.save(visit);
        return mapper.toResponse(saved);
    }
    
    @Override
    public PatientVisitResponse update(Long id, PatientVisitRequest request) {
        PatientVisit visit = visitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + id));
        
        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
            visit.setPatient(patient);
        }
        if (request.getPhysicianId() != null) {
            User physician = userRepository.findById(request.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Physician not found with id: " + request.getPhysicianId()));
            visit.setPhysician(physician);
        }
        if (request.getVisitTime() != null) {
            visit.setVisitTime(request.getVisitTime());
        }
        if (request.getVisitType() != null) {
            visit.setVisitType(request.getVisitType());
        }
        if (request.getLocation() != null) {
            visit.setLocation(request.getLocation());
        }
        if (request.getReason() != null) {
            visit.setReason(request.getReason());
        }
        if (request.getNotes() != null) {
            visit.setNotes(request.getNotes());
        }
        
        PatientVisit updated = visitRepository.save(visit);
        return mapper.toResponse(updated);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PatientVisitResponse findById(Long id) {
        PatientVisit visit = visitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + id));
        return mapper.toResponse(visit);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PatientVisitResponse> findAll(Pageable pageable) {
        return visitRepository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PatientVisitResponse> findByPatientId(Long patientId, Pageable pageable) {
        return visitRepository.findByPatientId(patientId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    public void delete(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visit not found with id: " + id);
        }
        visitRepository.deleteById(id);
    }
}










