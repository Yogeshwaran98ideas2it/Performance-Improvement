package com.acme.healthcare.service.impl;

import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.mapper.UserMapper;
import com.acme.healthcare.service.dto.UserRequest;
import com.acme.healthcare.service.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
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
 * Unit tests for {@link UserServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest request;
    private Role physicianRole;
    private User persistedUser;
    private UserResponse response;
    private UserSummaryDto summary;

    @BeforeEach
    void setUp() {
        physicianRole = new Role();
        physicianRole.setId(1L);
        physicianRole.setName(RoleType.PHYSICIAN);

        request = new UserRequest();
        request.setUsername("jdoe");
        request.setPassword("password");
        request.setEmail("jdoe@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setActive(true);
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setRoleIds(Set.of(1L));

        persistedUser = new User();
        persistedUser.setId(99L);
        persistedUser.setUsername("jdoe");
        persistedUser.setEmail("jdoe@example.com");

        response = new UserResponse();
        response.setId(99L);
        response.setUsername("jdoe");
        response.setEmail("jdoe@example.com");

        summary = UserSummaryDto.builder()
            .id(99L)
            .username("jdoe")
            .email("jdoe@example.com")
            .active(true)
            .build();
    }

    @Test
    void create_ShouldPersistUserAndReturnResponse() {
        when(roleRepository.findAllById(request.getRoleIds())).thenReturn(List.of(physicianRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(persistedUser);
        when(userMapper.toResponse(persistedUser)).thenReturn(response);

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_WhenRoleMissing_ShouldThrowEntityNotFound() {
        when(roleRepository.findAllById(request.getRoleIds())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> userService.create(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_ShouldMergeChangesAndReturnResponse() {
        when(userRepository.findById(99L)).thenReturn(Optional.of(persistedUser));
        when(roleRepository.findAllById(request.getRoleIds())).thenReturn(List.of(physicianRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(persistedUser)).thenReturn(persistedUser);
        when(userMapper.toResponse(persistedUser)).thenReturn(response);

        UserResponse result = userService.update(99L, request);

        assertNotNull(result);
        verify(userRepository, times(1)).save(persistedUser);
    }

    @Test
    void update_WhenUserMissing_ShouldThrowEntityNotFound() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(42L, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void get_ShouldReturnMappedResponse() {
        when(userRepository.findById(99L)).thenReturn(Optional.of(persistedUser));
        when(userMapper.toResponse(persistedUser)).thenReturn(response);

        UserResponse result = userService.get(99L);

        assertEquals("jdoe", result.getUsername());
    }

    @Test
    void get_WhenUserMissing_ShouldThrowEntityNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get(99L));
    }

    @Test
    void getSummary_ShouldReturnLightweightProjection() {
        when(userRepository.findById(99L)).thenReturn(Optional.of(persistedUser));
        when(userMapper.toSummary(persistedUser)).thenReturn(summary);

        UserSummaryDto result = userService.getSummary(99L);

        assertThat(result.getUsername()).isEqualTo("jdoe");
    }

    @Test
    void list_ShouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> page = new PageImpl<>(List.of(persistedUser), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toResponse(persistedUser)).thenReturn(response);

        Page<UserResponse> result = userService.list(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepositoryDelete() {
        when(userRepository.existsById(99L)).thenReturn(true);

        userService.delete(99L);

        verify(userRepository, times(1)).deleteById(99L);
    }

    @Test
    void delete_WhenMissing_ShouldThrowEntityNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.delete(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}

