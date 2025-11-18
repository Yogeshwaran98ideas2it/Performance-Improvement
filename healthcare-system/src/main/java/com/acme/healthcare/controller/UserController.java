package com.acme.healthcare.controller;

import com.acme.healthcare.service.UserService;
import com.acme.healthcare.service.dto.UserRequest;
import com.acme.healthcare.service.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController provides user CRUD endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates the controller.
     *
     * @param userService the user service
     */
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a user.
     *
     * @param request the request payload
     * @return the created user
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody final UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    /**
     * Updates a user.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the updated user
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserResponse> update(@PathVariable final Long id,
                                               @Valid @RequestBody final UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /**
     * Gets a user.
     *
     * @param id the identifier
     * @return the user
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    /**
     * Lists users.
     *
     * @param pageable the pageable
     * @return the paged users
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserResponse>> list(final Pageable pageable) {
        return ResponseEntity.ok(userService.list(pageable));
    }

    /**
     * Deletes a user.
     *
     * @param id the identifier
     * @return the response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


