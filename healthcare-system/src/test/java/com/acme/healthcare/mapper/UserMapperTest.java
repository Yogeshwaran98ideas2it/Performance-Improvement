package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.service.dto.UserResponse;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
class UserMapperTest {

    private final RolePermissionMapper rolePermissionMapper = new RolePermissionMapper();
    private final RoleMapper roleMapper = new RoleMapper(rolePermissionMapper);
    private final UserMapper mapper = new UserMapper(roleMapper);

    @Test
    void toResponse_ShouldMapNestedRoles() {
        RolePermission permission = new RolePermission();
        permission.setId(5L);
        permission.setCode("PATIENT_READ");

        Role role = new Role();
        role.setId(2L);
        role.setName(RoleType.CLINICIAN);
        role.setDescription("Clinical staff");
        role.setPermissions(Set.of(permission));

        User user = new User();
        user.setId(1L);
        user.setUsername("jdoe");
        user.setEmail("jdoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        user.setDateOfBirth(LocalDate.of(1990, 5, 20));
        user.getRoles().add(role);

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("jdoe");
        assertThat(response.getRoles()).hasSize(1);
        assertThat(response.getRoles().iterator().next().getName()).isEqualTo("CLINICIAN");
        assertThat(response.getRoles().iterator().next().getPermissions())
            .singleElement()
            .satisfies(permissionDto -> assertThat(permissionDto.getCode()).isEqualTo("PATIENT_READ"));
    }

    @Test
    void toSummary_ShouldReturnLightweightProjection() {
        Role role = new Role();
        role.setId(2L);
        role.setName(RoleType.ADMIN);
        role.setDescription("Administrator");

        User user = new User();
        user.setId(7L);
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setActive(true);
        user.getRoles().add(role);

        UserSummaryDto summary = mapper.toSummary(user);

        assertThat(summary.getId()).isEqualTo(7L);
        assertThat(summary.getRoles()).containsExactly("ADMIN");
    }
}


