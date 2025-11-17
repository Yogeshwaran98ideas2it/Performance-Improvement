package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.UserRequest;
import com.acme.healthcare.service.dto.UserResponse;
import com.acme.healthcare.common.dto.UserSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UserService manages user operations.
 */
public interface UserService {

    /**
     * Creates a user.
     *
     * @param request the request payload
     * @return the response
     */
    UserResponse create(UserRequest request);

    /**
     * Updates a user.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the response
     */
    UserResponse update(Long id, UserRequest request);

    /**
     * Retrieves a user.
     *
     * @param id the identifier
     * @return the response
     */
    UserResponse get(Long id);

    /**
     * Retrieves a lightweight user summary.
     *
     * @param id the identifier
     * @return the summary
     */
    UserSummaryDto getSummary(Long id);

    /**
     * Deletes a user.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Lists users.
     *
     * @param pageable the pageable
     * @return the page of responses
     */
    Page<UserResponse> list(Pageable pageable);
}


