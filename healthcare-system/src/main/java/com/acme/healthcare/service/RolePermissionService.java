package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * RolePermissionService exposes operations for managing permissions.
 */
public interface RolePermissionService {

    /**
     * Creates a new permission.
     *
     * @param request the request payload
     * @return the created permission response
     */
    RolePermissionResponse create(RolePermissionRequest request);

    /**
     * Updates an existing permission.
     *
     * @param id the permission identifier
     * @param request the update payload
     * @return the updated permission response
     */
    RolePermissionResponse update(Long id, RolePermissionRequest request);

    /**
     * Retrieves a permission by id.
     *
     * @param id the identifier
     * @return the permission response
     */
    RolePermissionResponse get(Long id);

    /**
     * Deletes a permission by id.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Lists permissions with pagination.
     *
     * @param pageable the pageable request
     * @return the page of responses
     */
    Page<RolePermissionResponse> list(Pageable pageable);
}











