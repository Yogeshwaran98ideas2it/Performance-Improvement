package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import com.acme.healthcare.service.dto.RoleResponse;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * RoleMapper maps role entities to responses.
 */
@Component
public class RoleMapper {

    private final RolePermissionMapper rolePermissionMapper;

    public RoleMapper(final RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * Converts a role entity to response.
     *
     * @param role the role entity
     * @return the response
     */
    public RoleResponse toResponse(final Role role) {
        if (role == null) {
            return null;
        }
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName() != null ? role.getName().name() : null);
        response.setDescription(role.getDescription());
        Set<RolePermission> permissions = role.getPermissions();
        if (permissions != null) {
            Set<RolePermissionResponse> mapped = permissions.stream()
                .map(rolePermissionMapper::toResponse)
                .collect(Collectors.toSet());
            response.setPermissions(mapped);
        }
        return response;
    }
}


