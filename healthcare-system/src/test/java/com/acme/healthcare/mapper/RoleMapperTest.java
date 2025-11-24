package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.service.dto.RoleResponse;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RoleMapper}.
 */
class RoleMapperTest {

    private final RolePermissionMapper rolePermissionMapper = new RolePermissionMapper();
    private final RoleMapper mapper = new RoleMapper(rolePermissionMapper);

    @Test
    void toResponse_ShouldIncludePermissions() {
        RolePermission permission = new RolePermission();
        permission.setId(2L);
        permission.setCode("USER_READ");

        Role role = new Role();
        role.setId(1L);
        role.setName(RoleType.ADMIN);
        role.setDescription("Administrator");
        role.setPermissions(Set.of(permission));

        RoleResponse response = mapper.toResponse(role);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("ADMIN");
        assertThat(response.getPermissions()).hasSize(1);
        assertThat(response.getPermissions().iterator().next().getCode()).isEqualTo("USER_READ");
    }
}


