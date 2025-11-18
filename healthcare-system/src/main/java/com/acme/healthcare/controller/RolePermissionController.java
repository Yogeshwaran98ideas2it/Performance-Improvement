package com.acme.healthcare.controller;

import com.acme.healthcare.service.RolePermissionService;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
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
 * RolePermissionController exposes CRUD endpoints for permissions.
 */
@RestController
@RequestMapping("/api/v1/permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    /**
     * Creates the controller.
     *
     * @param service the service
     */
    public RolePermissionController(final RolePermissionService service) {
        this.service = service;
    }

    /**
     * Creates a new permission.
     *
     * @param request the request payload
     * @return the created permission
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public ResponseEntity<RolePermissionResponse> create(@Valid @RequestBody final RolePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    /**
     * Updates an existing permission.
     *
     * @param id the identifier
     * @param request the update payload
     * @return the updated permission
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public ResponseEntity<RolePermissionResponse> update(@PathVariable final Long id,
                                                         @Valid @RequestBody final RolePermissionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /**
     * Gets a permission by id.
     *
     * @param id the identifier
     * @return the permission
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public ResponseEntity<RolePermissionResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    /**
     * Lists permissions with pagination.
     *
     * @param pageable the pageable
     * @return the paged permissions
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public ResponseEntity<Page<RolePermissionResponse>> list(final Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    /**
     * Deletes a permission by id.
     *
     * @param id the identifier
     * @return the response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


