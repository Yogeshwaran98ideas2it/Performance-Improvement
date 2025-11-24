package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.UserService;
import com.acme.healthcare.service.dto.UserRequest;
import com.acme.healthcare.service.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link UserController}.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldDelegateToService() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setUsername("jdoe");
        when(userService.create(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "jdoe",
                      "password": "secret",
                      "email": "jdoe@example.com",
                      "roleIds": [1,2]
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("jdoe"));

        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).create(captor.capture());
        assertThat(captor.getValue().getRoleIds()).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void update_ShouldReturnUpdatedUser() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(7L);
        response.setUsername("updated");
        when(userService.update(eq(7L), any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/users/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "updated",
                      "password": "secret",
                      "email": "updated@example.com",
                      "roleIds": [3]
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    void get_ShouldReturnUser() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(9L);
        response.setUsername("user9");
        when(userService.get(9L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/9"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(9));
    }

    @Test
    void list_ShouldReturnPagedUsers() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(3L);
        response.setUsername("user3");
        Page<UserResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(userService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/users?page=0&size=20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].username").value("user3"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/users/5"))
            .andExpect(status().isNoContent());

        verify(userService).delete(5L);
    }
}










