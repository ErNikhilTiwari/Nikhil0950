package com.cg.siptracker.config;

import com.cg.siptracker.security.JwtAuthenticationEntryPoint;
import com.cg.siptracker.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        // Endpoints for User (user can access SIP, Analytics, Report, and get NAV)
                        .requestMatchers("/api/sips/**").hasRole("USER")
                        .requestMatchers("/api/analytics/**").hasRole("USER")
                        .requestMatchers("/api/report/**").hasRole("USER")
                        .requestMatchers("/api/nav/get/**").hasAnyRole("USER", "ADMIN")

                        // Admin Endpoints (admin only can access admin and fetch NAV)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/nav/fetch/**").hasRole("ADMIN")

                        // Endpoint that both user and admin can access
                        .requestMatchers("/api/nav/get/**").hasAnyRole("USER", "ADMIN")

                        // Any other request should be authenticated
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
