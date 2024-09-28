package com.directory.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomUsernameSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Retrieve the Authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the username from the authenticated principal
            String username = authentication.getName();

            // Store the username in the session
            request.getSession().setAttribute("username", username);

//            if(!isCookiePresent(request)) {
//                // Create a session-based cookie if it's not already present
//                Cookie sessionCookie = new Cookie("SESSION_COOKIE", "unique-session-id"); // You can generate a unique session ID here
//                sessionCookie.setHttpOnly(true);
//                sessionCookie.setPath("/");
//                sessionCookie.setMaxAge(-1); // Session-based, will be deleted when the browser is closed
//                response.addCookie(sessionCookie);
//            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

//    private boolean isCookiePresent(HttpServletRequest request) {
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("SESSION_COOKIE".equals(cookie.getName())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}