package com.acme.healthcare.audit.client;

import com.acme.healthcare.common.dto.UserSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign client used to resolve user metadata when enriching audit events.
 */
@FeignClient(name = "user-service", path = "/internal/users")
public interface UserServiceClient {

    /**
     * Fetches a user summary by identifier.
     *
     * @param id user identifier
     * @return user summary
     */
    @GetMapping("/{id}")
    UserSummaryDto getUserById(@PathVariable("id") Long id);
}










