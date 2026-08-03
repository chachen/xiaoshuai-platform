package io.github.chachen.platform.auth;

import io.github.chachen.platform.core.context.CurrentUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService tokens;

    public JwtAuthenticationFilter(JwtTokenService t) {
        tokens = t;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) try {
            Claims c = tokens.parse(header.substring(7));
            if (!tokens.isRefresh(c)) {
                String username = c.getSubject();
                Long id = c.get("uid", Long.class);
                @SuppressWarnings("unchecked") Collection<String> ps = (Collection<String>) c.get("permissions");
                var authorities = ps == null ? List.<SimpleGrantedAuthority>of() : ps.stream().map(SimpleGrantedAuthority::new).toList();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
                CurrentUser.set(new CurrentUser(id, username, username));
            }
        } catch (Exception ignored) {
        }
        try {
            chain.doFilter(req, res);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
