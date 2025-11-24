package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RolePermissionMapper}.
 */
class RolePermissionMapperTest {

    private final RolePermissionMapper mapper = new RolePermissionMapper();

    @Test
    void toEntity_ShouldCopyFields() {
        RolePermissionRequest request = new RolePermissionRequest();
        request.setCode("USER_READ");
        request.setDescription("Read user data");

        RolePermission entity = mapper.toEntity(request);

        assertThat(entity.getCode()).isEqualTo("USER_READ");
        // Description is not stored in entity, only in DTO
    }

    @Test
    void toResponse_ShouldExposeFields() {
        RolePermission entity = new RolePermission();
        entity.setId(4L);
        entity.setCode("PATIENT_MANAGE");

        RolePermissionResponse response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(4L);
        assertThat(response.getCode()).isEqualTo("PATIENT_MANAGE");
        // Description is not stored in entity, mapper sets it to null
        assertThat(response.getDescription()).isNull();
    }

    @Test
    void updateEntity_ShouldOverwriteFields() {
        RolePermission entity = new RolePermission();
        entity.setCode("OLD");

        RolePermissionRequest request = new RolePermissionRequest();
        request.setCode("NEW");
        request.setDescription("New description");

        mapper.updateEntity(request, entity);

        assertThat(entity.getCode()).isEqualTo("NEW");
        // Description is not stored in entity
    }
}


