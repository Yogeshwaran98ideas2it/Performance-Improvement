package com.acme.healthcare.domain.entity;

import com.acme.healthcare.audit.entity.AuditableEntity;
import com.acme.healthcare.domain.enums.UserGender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Patient contains demographics and clinical metadata.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "patients")
public class Patient extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mrn", nullable = false, unique = true, length = 80)
    private String medicalRecordNumber;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private UserGender gender;

    @Column(name = "phone", length = 20)
    private String primaryPhone;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String addressLine1;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "state", length = 80)
    private String state;

    @Column(name = "zip_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 80)
    private String country;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "value")
    @JoinTable(name = "patient_vital_signs", joinColumns = @JoinColumn(name = "patient_id"))
    private Map<String, String> vitalSigns = new HashMap<>();

    @Column(name = "past_medical_history", columnDefinition = "TEXT")
    private String pastMedicalHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_physician_id")
    private User primaryPhysician;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_secondary_physicians",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> secondaryPhysicians = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_referral_physicians",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> referralPhysicians = new HashSet<>();
}

