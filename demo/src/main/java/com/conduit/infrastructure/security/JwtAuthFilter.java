package com.conduit.infrastructure.security;

import com.conduit.common.util.JwtUtil;
import com.conduit.domain.user.UserEntity;
import com.conduit.infrastructure.persistence.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        UUID userId = jwtUtil.extractUserId(token);


        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserEntity userEntity = userMapper.findByUserId(userId);
            if(userEntity == null) {chain.doFilter(request, response);return;}

            UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
            if (jwtUtil.validateToken(token, userId)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            System.out.println("JWT Authentication Success：");
//        } else {
//            System.out.println("JWT Authentication Fail");
//        }


        chain.doFilter(request, response);
    }
}
