package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import org.springframework.stereotype.Component;

/**
 * RolePermissionMapper converts between permission entities and DTOs.
 */
@Component
public class RolePermissionMapper {

    /**
     * Converts a request into an entity.
     *
     * @param request the request
     * @return the entity
     */
    public RolePermission toEntity(final RolePermissionRequest request) {
        if (request == null) {
            return null;
        }
        RolePermission entity = new RolePermission();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        return entity;
    }

    /**
     * Maps an entity to a response DTO.
     *
     * @param entity the entity
     * @return the response DTO
     */
    public RolePermissionResponse toResponse(final RolePermission entity) {
        if (entity == null) {
            return null;
        }
        RolePermissionResponse response = new RolePermissionResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setDescription(entity.getDescription());
        return response;
    }

    /**
     * Updates an entity from a request payload.
     *
     * @param request the request
     * @param entity the entity
     */
    public void updateEntity(final RolePermissionRequest request, final RolePermission entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
    }
}


