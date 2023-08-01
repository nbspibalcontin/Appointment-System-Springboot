package com.appointment.AppointmentService.Security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    public static final String ADMIN = "client_admin";
    public static final String USER = "client_user";

    public static final String DOCTOR = "client_doctor";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"api/appointment/add").hasAnyRole(ADMIN,USER)
                        .requestMatchers(HttpMethod.GET,"api/appointment/getAllAppointment"
                        ,"api/appointment/getById/*").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET,"api/appointment/transactionId/*").hasAnyRole(DOCTOR,USER)
                        .requestMatchers(HttpMethod.DELETE,"api/appointment/delete/*").hasAnyRole(ADMIN,DOCTOR,USER)
                        .requestMatchers(HttpMethod.PUT,"api/appointment/update/*").hasAnyRole(ADMIN,DOCTOR)
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                  .sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
