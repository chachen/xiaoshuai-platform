package io.github.chachen.platform.core.auth;

import java.util.Optional;

public interface AccountProvider {
    Optional<Account> findByUsername(String username);
}
