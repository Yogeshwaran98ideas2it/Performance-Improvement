package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.mapper.RoleMapper;
import com.acme.healthcare.service.dto.RoleRequest;
import com.acme.healthcare.service.dto.RoleResponse;
import com.acme.healthcare.domain.enums.RoleType;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link RoleServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository permissionRepository;

    @Mock
    private RoleMapper mapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleRequest request;
    private Role role;
    private RoleResponse response;
    private RolePermission permission;

    @BeforeEach
    void setUp() {
        permission = new RolePermission();
        permission.setId(1L);
        permission.setCode("USER_READ");

        request = new RoleRequest();
        request.setName("NURSE");
        request.setDescription("Nursing staff");
        request.setPermissionIds(Set.of(1L));

        role = new Role();
        role.setId(2L);
        role.setName(RoleType.NURSE);

        response = new RoleResponse();
        response.setId(2L);
        response.setName("NURSE");
    }

    @Test
    void create_ShouldPersistRole() {
        when(permissionRepository.findAllById(request.getPermissionIds())).thenReturn(List.of(permission));
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(mapper.toResponse(role)).thenReturn(response);

        RoleResponse result = roleService.create(request);

        assertNotNull(result);
        assertEquals("NURSE", result.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void create_WithMissingPermission_ShouldThrow() {
        when(permissionRepository.findAllById(request.getPermissionIds())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> roleService.create(request));
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(request.getPermissionIds())).thenReturn(List.of(permission));
        when(roleRepository.save(role)).thenReturn(role);
        when(mapper.toResponse(role)).thenReturn(response);

        RoleResponse result = roleService.update(2L, request);

        assertNotNull(result);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void update_WhenMissing_ShouldThrow() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.update(2L, request));
    }

    @Test
    void get_ShouldReturnResponse() {
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(mapper.toResponse(role)).thenReturn(response);

        RoleResponse result = roleService.get(2L);

        assertEquals(2L, result.getId());
    }

    @Test
    void get_WhenMissing_ShouldThrow() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.get(2L));
    }

    @Test
    void list_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> page = new PageImpl<>(List.of(role), pageable, 1);

        when(roleRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(role)).thenReturn(response);

        Page<RoleResponse> result = roleService.list(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(roleRepository.existsById(2L)).thenReturn(true);

        roleService.delete(2L);

        verify(roleRepository, times(1)).deleteById(2L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(roleRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> roleService.delete(2L));
        verify(roleRepository, never()).deleteById(anyLong());
    }
}


