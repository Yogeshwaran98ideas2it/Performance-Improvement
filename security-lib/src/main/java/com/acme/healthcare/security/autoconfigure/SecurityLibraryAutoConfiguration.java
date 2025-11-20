package com.acme.healthcare.security.autoconfigure;

import com.acme.healthcare.security.jwt.JwtTokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration entry point that registers security properties.
 */
@Configuration
@EnableConfigurationProperties(JwtTokenProperties.class)
public class SecurityLibraryAutoConfiguration {
}









