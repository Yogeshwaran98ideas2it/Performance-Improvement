package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.RolePermission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RolePermissionRepository provides access to role permissions.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * Finds a permission by code.
     *
     * @param code the code
     * @return the matching permission
     */
    Optional<RolePermission> findByCode(String code);
}


