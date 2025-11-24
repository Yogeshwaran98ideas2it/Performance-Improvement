package com.acme.healthcare.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * AuditableEntity provides audit metadata for all JPA entities.
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, com.acme.healthcare.audit.listener.AuditEventPublishingEntityListener.class})
public abstract class AuditableEntity {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Gets the creator identifier.
     *
     * @return the creator identifier
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator identifier.
     *
     * @param createdBy the creator identifier
     */
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the modifier identifier.
     *
     * @return the modifier identifier
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the modifier identifier.
     *
     * @param updatedBy the modifier identifier
     */
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Gets the updated timestamp.
     *
     * @return the updated timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updated timestamp.
     *
     * @param updatedAt the updated timestamp
     */
    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the entity version.
     *
     * @return the entity version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the entity version.
     *
     * @param version the entity version
     */
    public void setVersion(final Long version) {
        this.version = version;
    }
}


