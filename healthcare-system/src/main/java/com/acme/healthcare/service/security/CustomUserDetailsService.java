package com.acme.healthcare.service.security;

import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.RolePermission;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomUserDetailsService integrates application users with Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Creates the service.
     *
     * @param userRepository the user repository
     */
    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the {@link UserDetails} by username.
     *
     * @param username the username
     * @return the user details
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            mapAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(final Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (RolePermission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        }
        return authorities;
    }
}





