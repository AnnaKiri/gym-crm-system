package com.kirillova.gymcrmsystem.filter;

import com.kirillova.gymcrmsystem.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean swaggerUri = pathMatcher.match("/v3/api-docs/**", path) ||
                pathMatcher.match("/swagger-ui.html", path) ||
                pathMatcher.match("/swagger-ui/**", path);
        boolean registrationUri = (pathMatcher.match("/trainers", path) || pathMatcher.match("/trainees", path)) && method.equalsIgnoreCase("POST");
        boolean allowedUri = swaggerUri || registrationUri;

        if (!allowedUri) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String base64Credentials = authHeader.substring(6);
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                String[] values = credentials.split(":", 2);

                String username = values[0];
                String password = values[1];

                if (authenticationService.getAuthenticatedUser(username, password) == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized. User with this credentials doesn't exist");
                    log.info("Failed authentication with username = {}", username);
                    return;
                }

                log.info("Successful authentication with username = {}", username);
            } else {
                response.setHeader("WWW-Authenticate", "Basic realm=\"Access to the site\"");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
