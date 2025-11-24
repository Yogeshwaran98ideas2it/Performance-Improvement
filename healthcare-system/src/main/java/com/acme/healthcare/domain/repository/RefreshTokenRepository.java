package com.acme.healthcare.domain.repository;

import com.acme.healthcare.domain.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RefreshTokenRepository manages persisted refresh tokens.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds an active token by token string.
     *
     * @param token the token string
     * @return the matching token
     */
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    /**
     * Deletes all tokens for the given user.
     *
     * @param userId the user identifier
     */
    void deleteByUserId(Long userId);
}










