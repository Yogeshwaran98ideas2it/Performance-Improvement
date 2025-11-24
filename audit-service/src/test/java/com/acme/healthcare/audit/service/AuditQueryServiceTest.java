package com.acme.healthcare.audit.service;

import com.acme.healthcare.audit.domain.AuditRecord;
import com.acme.healthcare.audit.domain.AuditRecordRepository;
import com.acme.healthcare.audit.mapper.AuditMapper;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import com.acme.healthcare.common.dto.PagedResponseDto;
import java.util.HashMap;
import java.util.List;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuditQueryService}.
 */
@ExtendWith(MockitoExtension.class)
class AuditQueryServiceTest {

    @Mock
    private AuditRecordRepository repository;

    @Mock
    private AuditMapper mapper;

    @InjectMocks
    private AuditQueryService service;

    private Pageable pageable;
    private AuditRecord entity;
    private AuditRecordResponse response;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
        entity = new AuditRecord();
        entity.setAttributes(new HashMap<>());

        response = AuditRecordResponse.builder()
            .entityType("Patient")
            .entityId(42L)
            .username("auditor")
            .build();
    }

    @Test
    void findByUsername_ShouldMapResults() {
        Page<AuditRecord> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(repository.findByUsernameIgnoreCase("auditor", pageable)).thenReturn(page);
        when(mapper.toResponse(entity)).thenReturn(response);

        PagedResponseDto<AuditRecordResponse> result = service.findByUsername("auditor", pageable);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
    }

    @Test
    void findByEntityType_ShouldReturnPagedResponse() {
        Page<AuditRecord> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(repository.findByEntityTypeIgnoreCase("Patient", pageable)).thenReturn(page);
        when(mapper.toResponse(entity)).thenReturn(response);

        PagedResponseDto<AuditRecordResponse> result = service.findByEntityType("Patient", pageable);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }
}

