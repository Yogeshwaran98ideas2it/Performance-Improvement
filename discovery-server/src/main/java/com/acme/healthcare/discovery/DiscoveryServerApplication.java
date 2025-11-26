package com.acme.healthcare.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Entry point for the service discovery server.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

    /**
     * Boots the discovery server.
     *
     * @param args runtime arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}











