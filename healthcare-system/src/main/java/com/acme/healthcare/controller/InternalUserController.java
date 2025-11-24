package com.acme.healthcare.controller;

import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InternalUserController exposes lightweight projections for inter-service calls.
 */
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    /**
     * Retrieves a user summary for service-to-service communication.
     *
     * @param id user identifier
     * @return user summary projection
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserSummaryDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.getSummary(id));
    }
}









