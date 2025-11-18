package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.repository.RolePermissionRepository;
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

    /**
     * Creates a new instance.
     *
     * @param repository the repository
     * @param mapper the mapper
     */
    public RolePermissionServiceImpl(final RolePermissionRepository repository,
                                     final RolePermissionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RolePermissionResponse create(final RolePermissionRequest request) {
        RolePermission entity = mapper.toEntity(request);
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







