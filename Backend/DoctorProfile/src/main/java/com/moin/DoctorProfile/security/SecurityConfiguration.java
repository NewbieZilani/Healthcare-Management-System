package com.moin.DoctorProfile.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/doctor/register", "/doctor/login", "/doctor/allDepartments").permitAll()
                        .requestMatchers(HttpMethod.POST,"/doctor/verify/{doctorId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/doctor/changeAvailabilityStatus/{doctorId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/doctor/update").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/getById/{doctorId}").hasAnyAuthority("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.GET,"/doctor/getByEmail/{email}").hasAnyAuthority("ADMIN", "PATIENT", "DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllAvailableDoctor").permitAll()
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllDoctorByDepartment/{department}").hasAnyAuthority("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.POST,"/doctor/createSlots").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllSlots/{doctorId}/{date}").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllAvailableSlotDoctor/{doctorId}/{date}").permitAll()
                        .requestMatchers(HttpMethod.POST,"/doctor/bookAppointment/{slotId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/appointment/book/{slotId}/{appointmentType}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/appointment/cancel/{appointmentId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/appointment/complete/{appointmentId}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/appointment/getCurrentAppointments/{date}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/appointment/getUpcomingAppointments/{patientId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/appointment/history/{patientId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/doctor/totalDoctor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllDoctor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/doctor/disableDoctor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/doctor/getAllBookedSlot/{doctorId}/{date}").hasAuthority("DOCTOR")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.addAllowedOrigin("*");
        config.addAllowedOrigin("http://localhost:5173");
        //config.addAllowedHeader("Authorization");
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"
        ));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}