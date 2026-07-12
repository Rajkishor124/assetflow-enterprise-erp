package com.assetflow.core.service;

import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.UserRepository;
import com.assetflow.security.userdetails.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides access to the currently authenticated user.
 * Also serves as the AuditorAware implementation for JPA auditing.
 */
@Service
public class CurrentUserService implements AuditorAware<Long> {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(getCurrentUserId());
    }

    /**
     * Returns the ID of the currently authenticated user, or null if unauthenticated.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }

    /**
     * Returns the currently authenticated User entity.
     * @throws IllegalStateException if no user is authenticated.
     */
    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("No authenticated user in SecurityContext");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }

    /**
     * Checks whether the current user has a specific role.
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
