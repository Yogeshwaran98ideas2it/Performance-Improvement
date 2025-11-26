package com.acme.healthcare.audit.service;

import com.acme.healthcare.audit.client.UserServiceClient;
import com.acme.healthcare.audit.domain.AuditRecord;
import com.acme.healthcare.audit.domain.AuditRecordRepository;
import com.acme.healthcare.audit.mapper.AuditMapper;
import com.acme.healthcare.common.dto.AuditEventDto;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import com.acme.healthcare.common.dto.UserSummaryDto;
import java.time.Instant;
import java.util.HashMap;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuditWriteService}.
 */
@ExtendWith(MockitoExtension.class)
class AuditWriteServiceTest {

    @Mock
    private AuditRecordRepository repository;

    @Mock
    private AuditMapper mapper;

    @Mock
    private ObjectProvider<UserServiceClient> userServiceClientProvider;

    @InjectMocks
    private AuditWriteService service;

    private AuditEventDto eventDto;
    private AuditRecord persistedRecord;
    private AuditRecordResponse response;

    @BeforeEach
    void setUp() {
        eventDto = AuditEventDto.builder()
            .entityType("USER")
            .entityId(99L)
            .username("auditor")
            .action("CREATED")
            .tenantId("tenant-1")
            .timestamp(Instant.now())
            .details(new HashMap<>())
            .build();

        persistedRecord = AuditRecord.fromDto(eventDto);
        response = AuditRecordResponse.builder()
            .entityType("USER")
            .entityId(99L)
            .username("auditor")
            .build();

        when(repository.save(any(AuditRecord.class))).thenReturn(persistedRecord);
        when(mapper.toResponse(persistedRecord)).thenReturn(response);
    }

    @Test
    void persist_ShouldSaveRecordAndEnrichWithUserEmail() {
        UserServiceClient client = mock(UserServiceClient.class);
        when(client.getUserById(99L)).thenReturn(UserSummaryDto.builder()
            .id(99L)
            .email("user@example.com")
            .build());
        stubProvider(client);

        AuditRecordResponse result = service.persist(eventDto);

        assertThat(result).isEqualTo(response);
        assertThat(persistedRecord.getAttributes()).containsEntry("userEmail", "user@example.com");
        verify(repository, times(1)).save(any(AuditRecord.class));
    }

    @Test
    void persist_ShouldIgnoreDownstreamFailures() {
        UserServiceClient client = mock(UserServiceClient.class);
        when(client.getUserById(99L)).thenThrow(new IllegalStateException("downstream error"));
        stubProvider(client);

        AuditRecordResponse result = service.persist(eventDto);

        assertThat(result).isEqualTo(response);
        assertThat(persistedRecord.getAttributes()).doesNotContainKey("userEmail");
    }

    private void stubProvider(final UserServiceClient client) {
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<UserServiceClient> consumer = invocation.getArgument(0);
            consumer.accept(client);
            return null;
        }).when(userServiceClientProvider).ifAvailable(any());
    }
}










