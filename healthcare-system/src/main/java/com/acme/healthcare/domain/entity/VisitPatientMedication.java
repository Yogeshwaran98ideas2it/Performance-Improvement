package com.acme.healthcare.domain.entity;

import com.acme.healthcare.audit.entity.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * VisitPatientMedication records medication orders made during visits.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "visit_patient_medications")
public class VisitPatientMedication extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private PatientVisit visit;

    @Column(name = "medication_name", nullable = false, length = 160)
    private String medicationName;

    @Column(name = "dosage", length = 80)
    private String dosage;

    @Column(name = "frequency", length = 80)
    private String frequency;

    @Column(name = "route", length = 40)
    private String route;

    @Column(name = "instructions", length = 255)
    private String instructions;
}

