package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * PatientMapper converts between patient entities and DTO representations.
 */
@Component
public class PatientMapper {

    /**
     * Maps a patient entity to response DTO.
     *
     * @param patient the patient entity
     * @return the response DTO
     */
    public PatientResponse toResponse(final Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setMedicalRecordNumber(patient.getMedicalRecordNumber());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setPrimaryPhone(patient.getPrimaryPhone());
        response.setEmail(patient.getEmail());
        response.setAddressLine1(patient.getAddressLine1());
        response.setAddressLine2(patient.getAddressLine2());
        response.setCity(patient.getCity());
        response.setState(patient.getState());
        response.setPostalCode(patient.getPostalCode());
        response.setCountry(patient.getCountry());
        Map<String, String> vitalSigns = patient.getVitalSigns();
        if (vitalSigns != null) {
            response.setVitalSigns(Map.copyOf(vitalSigns));
        }
        response.setPastMedicalHistory(patient.getPastMedicalHistory());
        if (patient.getPrimaryPhysician() != null) {
            response.setPrimaryPhysicianId(patient.getPrimaryPhysician().getId());
        }
        Set<User> secondary = patient.getSecondaryPhysicians();
        if (secondary != null) {
            response.setSecondaryPhysicianIds(secondary.stream().map(User::getId).collect(java.util.stream.Collectors.toSet()));
        }
        Set<User> referral = patient.getReferralPhysicians();
        if (referral != null) {
            response.setReferralPhysicianIds(referral.stream().map(User::getId).collect(java.util.stream.Collectors.toSet()));
        }
        return response;
    }

    /**
     * Applies request values to a patient entity.
     *
     * @param request the patient request
     * @param entity the patient entity
     */
    public void updateEntity(final PatientRequest request, final Patient entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setMedicalRecordNumber(request.getMedicalRecordNumber());
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setDateOfBirth(request.getDateOfBirth());
        entity.setGender(request.getGender());
        entity.setPrimaryPhone(request.getPrimaryPhone());
        entity.setEmail(request.getEmail());
        entity.setAddressLine1(request.getAddressLine1());
        entity.setAddressLine2(request.getAddressLine2());
        entity.setCity(request.getCity());
        entity.setState(request.getState());
        entity.setPostalCode(request.getPostalCode());
        entity.setCountry(request.getCountry());
        if (request.getVitalSigns() != null) {
            entity.setVitalSigns(Map.copyOf(request.getVitalSigns()));
        } else {
            entity.setVitalSigns(null);
        }
        entity.setPastMedicalHistory(request.getPastMedicalHistory());
        // Primary, secondary, referral physicians are set within service after validation.
        // Mapper leaves managed associations untouched here.
    }
}

