package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.enums.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository provides data access for roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by name.
     *
     * @param name the role name
     * @return the matching role
     */
    Optional<Role> findByName(RoleType name);
}


