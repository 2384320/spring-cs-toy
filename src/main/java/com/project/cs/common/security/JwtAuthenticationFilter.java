package com.project.cs.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cs.common.response.CommonResponse;
import com.project.cs.common.response.code.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && jwtProvider.validateToken(token)) {
                token = token.split(" ")[1].trim();
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            if (e instanceof MalformedJwtException)
                sendErrorResponse(response, CommonResponse.failure(ErrorCode.MEMBER_INVALID_TOKEN));
            else if (e instanceof SignatureException)
                sendErrorResponse(response, CommonResponse.failure(ErrorCode.MEMBER_INVALID_TOKEN));
            else if (e instanceof ExpiredJwtException)
                sendErrorResponse(response, CommonResponse.failure(ErrorCode.MEMBER_EXPIRED_TOKEN));
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, CommonResponse commonResponse) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), commonResponse);
    }
}