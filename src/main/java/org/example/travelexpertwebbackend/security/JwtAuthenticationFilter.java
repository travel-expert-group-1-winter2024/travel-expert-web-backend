package org.example.travelexpertwebbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * To create security filter for JWT authentication that will be included in out chain of
 * security filters in the SecurityConfig class.
 */
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // extract the JWT token from the request header
        // get the token from the request header
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        // ensure the token in header is not null
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Token is invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(jwt);
        System.out.println("Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // extract user details from username
            UserDetails userDetails = userService.loadUserByUsername(username);
            // check if token is valid => token has not expired
            if (jwtService.isTokenNotExpired(jwt)) {
                // if token is not expired then perform basic authentication using username and password
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
                System.out.println("User roles: " + userDetails.getAuthorities());
                // add this the auth token to the request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );
                // put the auth token in the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Token is expired");
            }
        }
        filterChain.doFilter(request, response);
    }
}