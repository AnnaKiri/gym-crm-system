package com.kirillova.gymcrmsystem.filter;

import com.kirillova.gymcrmsystem.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean swaggerUri = pathMatcher.match("/v3/api-docs/**", path) ||
                pathMatcher.match("/swagger-ui.html", path) ||
                pathMatcher.match("/swagger-ui/**", path);
        boolean registrationUri = (pathMatcher.match("/trainer", path) || pathMatcher.match("/trainee", path)) && method.equalsIgnoreCase("POST");
        boolean allowedUri = swaggerUri || registrationUri;

        if (!allowedUri) {
            String username = request.getHeader("username");
            String password = request.getHeader("password");

            if (authenticationService.getAuthenticatedUser(username, password) == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized. User with this credentials doesn't exist");
                log.info("Failed authentication with username = {}", username);
                return;
            }

            log.info("Successful authentication with username = {}", username);
        }

        filterChain.doFilter(request, response);
    }
}
