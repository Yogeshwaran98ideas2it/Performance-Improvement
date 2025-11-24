package com.acme.healthcare.service.dto;

/**
 * DocumentResponse returns document metadata to clients.
 */
public class DocumentResponse {

    private Long id;
    private Long patientId;
    private Long visitId;
    private String fileName;
    private String fileType;
    private Long fileSize;

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
     * Gets the patient identifier.
     *
     * @return the patient identifier
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient identifier.
     *
     * @param patientId the patient identifier
     */
    public void setPatientId(final Long patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the visit identifier.
     *
     * @return the visit identifier
     */
    public Long getVisitId() {
        return visitId;
    }

    /**
     * Sets the visit identifier.
     *
     * @param visitId the visit identifier
     */
    public void setVisitId(final Long visitId) {
        this.visitId = visitId;
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the file name
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the file type.
     *
     * @return the file type
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Sets the file type.
     *
     * @param fileType the file type
     */
    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    /**
     * Gets the file size.
     *
     * @return the file size
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * Sets the file size.
     *
     * @param fileSize the file size
     */
    public void setFileSize(final Long fileSize) {
        this.fileSize = fileSize;
    }
}










