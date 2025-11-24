package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.mapper.RolePermissionMapper;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
 * Unit tests for {@link RolePermissionServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class RolePermissionServiceImplTest {

    @Mock
    private RolePermissionRepository repository;

    @Mock
    private RolePermissionMapper mapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RolePermissionServiceImpl service;

    private RolePermissionRequest request;
    private RolePermission entity;
    private RolePermissionResponse response;
    private Role defaultRole;

    @BeforeEach
    void setUp() {
        request = new RolePermissionRequest();
        request.setCode("PATIENT_READ");
        request.setDescription("Read patient records");

        entity = new RolePermission();
        entity.setId(3L);
        entity.setCode("PATIENT_READ");
        entity.setResource("PATIENT");
        entity.setAction("READ");

        response = new RolePermissionResponse();
        response.setId(3L);
        response.setCode("PATIENT_READ");

        defaultRole = new Role();
        defaultRole.setId(1L);
        defaultRole.setName(RoleType.ADMIN);
    }

    @Test
    void create_ShouldPersist() {
        when(mapper.toEntity(request)).thenReturn(entity);
        when(roleRepository.findByName(RoleType.ADMIN)).thenReturn(Optional.of(defaultRole));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        RolePermissionResponse result = service.create(request);

        assertNotNull(result);
        assertEquals("PATIENT_READ", result.getCode());
        verify(repository, times(1)).save(entity);
        verify(roleRepository, times(1)).findByName(RoleType.ADMIN);
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        RolePermissionResponse result = service.update(3L, request);

        assertNotNull(result);
        verify(mapper, times(1)).updateEntity(request, entity);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void update_WhenMissing_ShouldThrow() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(3L, request));
        verify(repository, never()).save(any());
    }

    @Test
    void get_ShouldReturnResponse() {
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        RolePermissionResponse result = service.get(3L);

        assertEquals(3L, result.getId());
    }

    @Test
    void get_WhenMissing_ShouldThrow() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.get(3L));
    }

    @Test
    void list_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RolePermission> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(entity)).thenReturn(response);

        Page<RolePermissionResponse> result = service.list(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(repository.existsById(3L)).thenReturn(true);

        service.delete(3L);

        verify(repository, times(1)).deleteById(3L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(repository.existsById(3L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(3L));
        verify(repository, never()).deleteById(anyLong());
    }
}

