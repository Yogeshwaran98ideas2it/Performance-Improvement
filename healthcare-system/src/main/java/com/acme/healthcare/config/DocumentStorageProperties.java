package com.acme.healthcare.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * DocumentStorageProperties captures configuration for document storage.
 */
@Validated
@ConfigurationProperties(prefix = "storage.documents")
public class DocumentStorageProperties {

    @NotBlank
    private String root;

    /**
     * Gets the root directory path.
     *
     * @return the root directory path
     */
    public String getRoot() {
        return root;
    }

    /**
     * Sets the root directory path.
     *
     * @param root the root directory path
     */
    public void setRoot(final String root) {
        this.root = root;
    }
}







