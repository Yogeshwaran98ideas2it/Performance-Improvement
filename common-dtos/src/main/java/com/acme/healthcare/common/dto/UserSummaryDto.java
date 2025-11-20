package com.acme.healthcare.common.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight user projection exchanged across services.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean active;
}









