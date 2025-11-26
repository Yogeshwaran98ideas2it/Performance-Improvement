package com.acme.healthcare.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the Audit microservice.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuditServiceApplication {

    /**
     * Boots the audit service.
     *
     * @param args runtime args
     */
    public static void main(final String[] args) {
        SpringApplication.run(AuditServiceApplication.class, args);
    }
}











