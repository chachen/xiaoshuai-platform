package io.github.chachen.platform.auth;

import io.github.chachen.platform.captcha.CaptchaService;
import io.github.chachen.platform.core.auth.AccountProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class AuthConfiguration {
    @Bean
    JwtTokenService jwtTokenService(AuthProperties p) {
        return new JwtTokenService(p);
    }

    @Bean
    AuthService authService(AccountProvider a, PasswordEncoder e, JwtTokenService t, ObjectProvider<CaptchaService> c, AuthProperties p, org.springframework.context.ApplicationEventPublisher events) {
        return new AuthService(a, e, t, c.getIfAvailable(), p, events);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService t) {
        return new JwtAuthenticationFilter(t);
    }

    @Bean
    PermissionAspect permissionAspect() {
        return new PermissionAspect();
    }

    @Bean
    UserDetailsService xsUserDetailsService(AccountProvider accounts) {
        return username -> accounts.findByUsername(username)
                .map(account -> org.springframework.security.core.userdetails.User.withUsername(account.username())
                        .password(account.passwordHash())
                        .authorities(account.permissions().toArray(String[]::new))
                        .disabled(!account.enabled())
                        .accountLocked(account.locked())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    @ConditionalOnProperty(prefix = "xs.auth", name = "security-chain-enabled", havingValue = "true", matchIfMissing = false)
    SecurityFilterChain xsSecurityFilterChain(HttpSecurity http, AuthProperties p, JwtAuthenticationFilter filter) throws Exception {
        http.csrf(csrf -> csrf.disable()).formLogin(f -> f.disable()).httpBasic(b -> b.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> {
                    a.requestMatchers(p.getPermitPaths().toArray(String[]::new)).permitAll();
                    a.anyRequest().authenticated();
                })
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
