package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.mapper.RoleMapper;
import com.acme.healthcare.service.RoleService;
import com.acme.healthcare.service.dto.RoleRequest;
import com.acme.healthcare.service.dto.RoleResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RoleServiceImpl implements role business logic.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository permissionRepository;
    private final RoleMapper mapper;

    /**
     * Creates the service instance.
     *
     * @param roleRepository the role repository
     * @param permissionRepository the permission repository
     * @param mapper the mapper
     */
    public RoleServiceImpl(final RoleRepository roleRepository,
                           final RolePermissionRepository permissionRepository,
                           final RoleMapper mapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleResponse create(final RoleRequest request) {
        Role role = new Role();
        applyRequest(request, role);
        return mapper.toResponse(roleRepository.save(role));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleResponse update(final Long id, final RoleRequest request) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        applyRequest(request, role);
        return mapper.toResponse(roleRepository.save(role));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RoleResponse get(final Long id) {
        return roleRepository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> list(final Pageable pageable) {
        return roleRepository.findAll(pageable).map(mapper::toResponse);
    }

    private void applyRequest(final RoleRequest request, final Role role) {
        role.setName(request.getName() != null ? RoleType.valueOf(request.getName().toUpperCase()) : null);
        role.setDescription(request.getDescription());
        List<RolePermission> permissions = permissionRepository.findAllById(request.getPermissionIds());
        if (permissions.size() != request.getPermissionIds().size()) {
            throw new EntityNotFoundException("One or more permissions not found");
        }
        Set<RolePermission> permissionSet = new HashSet<>(permissions);
        role.setPermissions(permissionSet);
    }
}


