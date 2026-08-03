package io.github.chachen.platform.core.auth;

import java.util.Set;

/**
 * Authentication-facing projection. It deliberately contains no database entity.
 */
public record Account(Long id, String username, String passwordHash, boolean enabled,
                      boolean locked, Set<String> permissions) {
    public boolean canLogin() {
        return enabled && !locked;
    }
}
