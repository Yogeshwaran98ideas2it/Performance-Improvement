package com.acme.healthcare;

import com.acme.healthcare.config.DocumentStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * HealthcareSystemApplication bootstraps the healthcare management system.
 */
@SpringBootApplication
@EnableConfigurationProperties(DocumentStorageProperties.class)
public class HealthcareSystemApplication {

    /**
     * Application entry point.
     *
     * @param args the command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(HealthcareSystemApplication.class, args);
    }
}

