package com.project.cs.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cs.common.security.JwtAuthenticationFilter;
import com.project.cs.common.security.JwtProvider;
import com.project.cs.common.security.exception.JwtAccessDeniedHandler;
import com.project.cs.common.security.exception.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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
                .exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}