package com.kirillova.gymcrmsystem.filter;

import com.kirillova.gymcrmsystem.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean allowedUri = (path.equals("/trainer") || path.equals("/trainee")) && method.equalsIgnoreCase("POST");
        if (!allowedUri) {
            String username = request.getHeader("username");
            String password = request.getHeader("password");


            if (authenticationService.getAuthenticatedUser(username, password) == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized. User with this credentials doesn't exist");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}