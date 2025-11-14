package com.acme.healthcare.mapper;

import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.service.dto.RoleResponse;
import com.acme.healthcare.service.dto.UserResponse;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * UserMapper maps user entities to responses.
 */
@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(final RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * Converts a user entity to response DTO.
     *
     * @param user the user entity
     * @return the response DTO
     */
    public UserResponse toResponse(final User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setJobTitle(user.getJobTitle());
        response.setActive(user.isActive());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setGender(user.getGender());
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            Set<RoleResponse> mappedRoles = roles.stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toSet());
            response.setRoles(mappedRoles);
        }
        return response;
    }

    /**
     * Converts a user entity to a lightweight summary DTO.
     *
     * @param user the user entity
     * @return the summary DTO
     */
    public UserSummaryDto toSummary(final User user) {
        if (user == null) {
            return null;
        }
        Set<Role> roles = user.getRoles() == null ? Set.of() : user.getRoles();
        return UserSummaryDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .active(user.isActive())
            .roles(roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()))
            .build();
    }
}


