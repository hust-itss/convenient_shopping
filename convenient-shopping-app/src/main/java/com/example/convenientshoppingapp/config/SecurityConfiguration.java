package com.example.convenientshoppingapp.config;

import com.example.convenientshoppingapp.config.security.JwtAuthenticationFilter;
import com.example.convenientshoppingapp.entity.auth.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors();

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/users/**"
                )
                .permitAll()
                .requestMatchers("/api/v1/dish/**").hasAnyAuthority("ROLE_USER")
                .requestMatchers(GET, "/api/v1/dish/**").hasAnyAuthority(ERole.ROLE_USER.name())

                .requestMatchers("/api/v1/food/**").hasAnyAuthority("ROLE_USER")
                .requestMatchers(POST, "/api/v1/food/**").hasAnyAuthority(ERole.ROLE_USER.name())

                .requestMatchers(GET, "/api/v1/food/**").hasAnyAuthority(ERole.ROLE_USER.name())
                .requestMatchers(PUT, "/api/v1/food/**").hasAnyAuthority(ERole.ROLE_USER.name())

                .requestMatchers(GET, "/api/v1/group/**").hasAnyAuthority(ERole.ROLE_USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        log.info("Security configuration is loaded");
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

}