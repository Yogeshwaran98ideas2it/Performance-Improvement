package com.acme.healthcare.controller;

import com.acme.healthcare.service.RoleService;
import com.acme.healthcare.service.dto.RoleRequest;
import com.acme.healthcare.service.dto.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RoleController exposes role management endpoints.
 */
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Creates the controller.
     *
     * @param roleService the role service
     */
    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Creates a new role.
     *
     * @param request the request payload
     * @return the created role
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody final RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
    }

    /**
     * Updates an existing role.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the updated role
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<RoleResponse> update(@PathVariable final Long id,
                                               @Valid @RequestBody final RoleRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    /**
     * Retrieves a role.
     *
     * @param id the identifier
     * @return the role
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RoleResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    /**
     * Lists roles.
     *
     * @param pageable the pageable
     * @return the paged roles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<Page<RoleResponse>> list(final Pageable pageable) {
        return ResponseEntity.ok(roleService.list(pageable));
    }

    /**
     * Deletes a role.
     *
     * @param id the identifier
     * @return the response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


