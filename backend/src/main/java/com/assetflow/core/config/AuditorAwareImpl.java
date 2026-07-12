package com.assetflow.core.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
        //     return Optional.empty();
        // }
        // UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        // return Optional.of(userPrincipal.getId());
        
        // Placeholder returning empty until Authentication is fully implemented
        return Optional.empty();
    }
}
