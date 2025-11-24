package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
import com.acme.healthcare.domain.repository.RoleRepository;
import com.acme.healthcare.mapper.RolePermissionMapper;
import com.acme.healthcare.service.RolePermissionService;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RolePermissionServiceImpl provides the business logic for permissions.
 */
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository repository;
    private final RolePermissionMapper mapper;
    private final RoleRepository roleRepository;

    /**
     * Creates a new instance.
     *
     * @param repository the repository
     * @param mapper the mapper
     * @param roleRepository the role repository
     */
    public RolePermissionServiceImpl(final RolePermissionRepository repository,
                                     final RolePermissionMapper mapper,
                                     final RoleRepository roleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RolePermissionResponse create(final RolePermissionRequest request) {
        RolePermission entity = mapper.toEntity(request);
        
        // Parse code to extract resource and action (e.g., "PATIENT_READ" -> resource="PATIENT", action="READ")
        String code = request.getCode();
        String[] parts = code.split("_", 2);
        String resource = parts.length > 0 ? parts[0] : code;
        String action = parts.length > 1 ? parts[1] : "READ";
        
        entity.setResource(resource);
        entity.setAction(action);
        
        // Find or create a default ADMIN role for standalone permissions
        Role defaultRole = roleRepository.findByName(RoleType.ADMIN)
            .orElseGet(() -> {
                Role role = new Role();
                role.setName(RoleType.ADMIN);
                role.setDescription("Default admin role for system permissions");
                return roleRepository.save(role);
            });
        entity.setRole(defaultRole);
        
        return mapper.toResponse(repository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RolePermissionResponse update(final Long id, final RolePermissionRequest request) {
        RolePermission entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RolePermissionResponse get(final Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Permission not found");
        }
        repository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RolePermissionResponse> list(final Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}








