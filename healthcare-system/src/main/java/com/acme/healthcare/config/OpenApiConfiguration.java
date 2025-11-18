package com.acme.healthcare.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class OpenApiConfiguration {
    
    /**
     * Configures OpenAPI documentation with security and API information.
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Healthcare Management System API")
                .version("1.0.0")
                .description("Enterprise-grade healthcare management platform with multi-tenant support. " +
                    "This API provides comprehensive CRUD operations for managing users, patients, visits, " +
                    "diagnoses, allergies, medications, and documents with role-based access control.")
                .contact(new Contact()
                    .name("Healthcare Platform Support")
                    .email("support@healthcare.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://healthcare.com/license")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }
    
    /**
     * Creates the security scheme for JWT Bearer token authentication.
     *
     * @return the security scheme
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
            .description("JWT token obtained from /api/v1/auth/login endpoint. " +
                "Include the token in the Authorization header as: Bearer {token}");
    }
}







