package com.project.cs.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cs.exception.ErrorCode;
import com.project.cs.common.exception.ResultData;
import com.project.cs.common.security.JwtAuthenticationFilter;
import com.project.cs.common.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("*"));
                        config.setAllowedMethods(List.of("*"));
                        return config;
                    };
                    c.configurationSource(source);
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/signup", "/login").permitAll()
//                .antMatchers("/member/**").hasRole("MEMBER")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    ResultData result = new ResultData();
                    result.setCode(HttpStatus.FORBIDDEN.value());
                    result.setMsg(ErrorCode.MEMBER_ACCESS_DENY.getMsg());

                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(), result);
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    ResultData result = new ResultData();
                    result.setCode(HttpStatus.UNAUTHORIZED.value());
                    result.setMsg(ErrorCode.MEMBER_INVALID_TOKEN.getMsg());

                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(), result);
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}