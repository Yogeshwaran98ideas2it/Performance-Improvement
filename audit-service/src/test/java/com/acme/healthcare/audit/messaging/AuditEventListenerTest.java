package com.acme.healthcare.audit.messaging;

import com.acme.healthcare.audit.service.AuditWriteService;
import com.acme.healthcare.common.dto.AuditEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AuditEventListener}.
 */
@ExtendWith(MockitoExtension.class)
class AuditEventListenerTest {

    @Mock
    private AuditWriteService writeService;

    @InjectMocks
    private AuditEventListener listener;

    @Test
    void onAuditEvent_ShouldDelegateToWriteService() {
        AuditEventDto event = AuditEventDto.builder()
            .entityType("PATIENT")
            .entityId(1L)
            .build();

        listener.onAuditEvent(event);

        verify(writeService).persist(event);
    }
}










