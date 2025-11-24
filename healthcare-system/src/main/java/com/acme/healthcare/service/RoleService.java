package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.RoleRequest;
import com.acme.healthcare.service.dto.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * RoleService describes operations for role management.
 */
public interface RoleService {

    /**
     * Creates a role.
     *
     * @param request the request payload
     * @return the created role
     */
    RoleResponse create(RoleRequest request);

    /**
     * Updates a role.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the updated role
     */
    RoleResponse update(Long id, RoleRequest request);

    /**
     * Retrieves a role.
     *
     * @param id the identifier
     * @return the role
     */
    RoleResponse get(Long id);

    /**
     * Deletes a role.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Lists roles.
     *
     * @param pageable the pageable
     * @return the page of roles
     */
    Page<RoleResponse> list(Pageable pageable);
}










