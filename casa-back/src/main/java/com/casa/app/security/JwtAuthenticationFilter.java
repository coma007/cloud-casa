package com.casa.app.security;

import com.casa.app.util.email.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

//    TODO
//    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request, String authToken) {
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getEmailFromToken(authToken));
//        // create a UsernamePasswordAuthenticationToken with null values.
//        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
//        authentication.setToken(authToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // Set the claims so that in controller we will be using it to create
//        // new JWT
//        request.setAttribute("claims", ex.getClaims());
//
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String username = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        try{
            username = jwtUtil.getUsernameFromToken(jwt);
        }catch(Exception e){
            exceptionResolver.resolveException(request, response, null, e);
        }

        try {
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            exceptionResolver.resolveException(request, response, null, e);
//            String isRefreshToken = request.getHeader("isRefreshToken");
//            String requestURL = request.getRequestURL().toString();
//            // allow for Refresh Token creation if following conditions are true.
//            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
//                allowForRefreshToken(e, request, jwt);
//            }
        }


    }

}