package com.acme.healthcare.service.dto;

import com.acme.healthcare.domain.enums.UserGender;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * PatientResponse encapsulates patient data returned via APIs.
 */
public class PatientResponse {

    private Long id;
    private String medicalRecordNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private UserGender gender;
    private String primaryPhone;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Map<String, String> vitalSigns;
    private String pastMedicalHistory;
    private Long primaryPhysicianId;
    private Set<Long> secondaryPhysicianIds;
    private Set<Long> referralPhysicianIds;

    /**
     * Gets the identifier.
     *
     * @return the identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the identifier.
     *
     * @param id the identifier
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the MRN.
     *
     * @return the MRN
     */
    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    /**
     * Sets the MRN.
     *
     * @param medicalRecordNumber the MRN
     */
    public void setMedicalRecordNumber(final String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     *
     * @param dateOfBirth the date of birth
     */
    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public UserGender getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     *
     * @param gender the gender
     */
    public void setGender(final UserGender gender) {
        this.gender = gender;
    }

    /**
     * Gets the primary phone.
     *
     * @return the primary phone
     */
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    /**
     * Sets the primary phone.
     *
     * @param primaryPhone the primary phone
     */
    public void setPrimaryPhone(final String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets address line 1.
     *
     * @return the address line 1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets address line 1.
     *
     * @param addressLine1 the address line 1
     */
    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * Gets address line 2.
     *
     * @return the address line 2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets address line 2.
     *
     * @param addressLine2 the address line 2
     */
    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * Gets the postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country the country
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * Gets the vital signs.
     *
     * @return the vital signs
     */
    public Map<String, String> getVitalSigns() {
        return vitalSigns;
    }

    /**
     * Sets the vital signs.
     *
     * @param vitalSigns the vital signs
     */
    public void setVitalSigns(final Map<String, String> vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    /**
     * Gets the past medical history.
     *
     * @return the past medical history
     */
    public String getPastMedicalHistory() {
        return pastMedicalHistory;
    }

    /**
     * Sets the past medical history.
     *
     * @param pastMedicalHistory the past medical history
     */
    public void setPastMedicalHistory(final String pastMedicalHistory) {
        this.pastMedicalHistory = pastMedicalHistory;
    }

    /**
     * Gets the primary physician identifier.
     *
     * @return the identifier
     */
    public Long getPrimaryPhysicianId() {
        return primaryPhysicianId;
    }

    /**
     * Sets the primary physician identifier.
     *
     * @param primaryPhysicianId the identifier
     */
    public void setPrimaryPhysicianId(final Long primaryPhysicianId) {
        this.primaryPhysicianId = primaryPhysicianId;
    }

    /**
     * Gets the secondary physician identifiers.
     *
     * @return the identifiers
     */
    public Set<Long> getSecondaryPhysicianIds() {
        return secondaryPhysicianIds;
    }

    /**
     * Sets the secondary physician identifiers.
     *
     * @param secondaryPhysicianIds the identifiers
     */
    public void setSecondaryPhysicianIds(final Set<Long> secondaryPhysicianIds) {
        this.secondaryPhysicianIds = secondaryPhysicianIds;
    }

    /**
     * Gets the referral physician identifiers.
     *
     * @return the identifiers
     */
    public Set<Long> getReferralPhysicianIds() {
        return referralPhysicianIds;
    }

    /**
     * Sets the referral physician identifiers.
     *
     * @param referralPhysicianIds the identifiers
     */
    public void setReferralPhysicianIds(final Set<Long> referralPhysicianIds) {
        this.referralPhysicianIds = referralPhysicianIds;
    }
}










