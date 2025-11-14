package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository manages {@link User} persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the matching user if found
     */
    Optional<User> findByUsername(String username);
}


