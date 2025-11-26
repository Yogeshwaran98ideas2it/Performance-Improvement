package com.acme.healthcare.controller;

import com.acme.healthcare.common.dto.UserSummaryDto;
import com.acme.healthcare.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link InternalUserController}.
 */
@ExtendWith(MockitoExtension.class)
class InternalUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private InternalUserController controller;

    @Test
    void findById_ShouldReturnSummaryFromService() {
        UserSummaryDto summary = UserSummaryDto.builder()
            .id(99L)
            .username("jdoe")
            .email("jdoe@example.com")
            .active(true)
            .build();

        when(userService.getSummary(99L)).thenReturn(summary);

        ResponseEntity<UserSummaryDto> response = controller.findById(99L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("jdoe");
    }
}










