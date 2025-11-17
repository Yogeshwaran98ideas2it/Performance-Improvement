package com.acme.healthcare.service.impl;

import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.mapper.UserMapper;
import com.acme.healthcare.service.UserService;
import com.acme.healthcare.service.dto.UserRequest;
import com.acme.healthcare.service.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserServiceImpl provides user business logic.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Creates the service implementation.
     *
     * @param userRepository the user repository
     * @param roleRepository the role repository
     * @param passwordEncoder the password encoder
     * @param userMapper the mapper
     */
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final PasswordEncoder passwordEncoder,
                           final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse create(final UserRequest request) {
        User user = new User();
        applyRequest(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse update(final Long id, final UserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        applyRequest(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse get(final Long id) {
        return userRepository.findById(id)
            .map(userMapper::toResponse)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserSummaryDto getSummary(final Long id) {
        return userRepository.findById(id)
            .map(userMapper::toSummary)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> list(final Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    private void applyRequest(final UserRequest request, final User user) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setActive(request.isActive());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        List<Role> roles = roleRepository.findAllById(request.getRoleIds());
        if (roles.size() != request.getRoleIds().size()) {
            throw new EntityNotFoundException("One or more roles not found");
        }
        Set<Role> roleSet = new HashSet<>(roles);
        user.setRoles(roleSet);
    }
}


