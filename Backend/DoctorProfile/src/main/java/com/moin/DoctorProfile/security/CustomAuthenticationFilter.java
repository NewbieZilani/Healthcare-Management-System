package com.moin.DoctorProfile.security;

import com.moin.DoctorProfile.SpringApplicationContext;
import com.moin.DoctorProfile.constants.AppConstants;
import com.moin.DoctorProfile.dto.DoctorDto;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.exceptions.EmailNotFoundException;
import com.moin.DoctorProfile.model.LoginRequestModel;
import com.moin.DoctorProfile.repository.DoctorRepository;
import com.moin.DoctorProfile.service.DoctorService;
import com.moin.DoctorProfile.utilities.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/doctor/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword())
            );
        } catch (IOException e) {
            log.info("Exception occurred at attemptAuthentication method: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String user = ((User) authResult.getPrincipal()).getUsername();
        DoctorService doctorServiceRole = (DoctorService) SpringApplicationContext.getBean("doctorServiceImplementation");
        DoctorEntity doctorEntity = doctorServiceRole.readByEmail(user);
        List<String> roles = new ArrayList<>();
        roles.add(doctorEntity.getRole());
        String accessToken = JWTUtils.generateToken(user, roles);
        DoctorService doctorService = (DoctorService) SpringApplicationContext.getBean("doctorServiceImplementation");
        try {
            DoctorDto doctorDto = doctorService.getDoctorByEmail(user);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("Message", "Successfully logged in");
            responseBody.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
            responseBody.put("role", doctorDto.getRole());
            responseBody.put("email", doctorDto.getEmail());
            responseBody.put("userId", doctorDto.getDoctor_id());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseBody);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonResponse);
        } catch (EmailNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String errorMessage = "Authentication failed: ";
            if (failed instanceof BadCredentialsException) {
                errorMessage += "Invalid password.";
            } else if (failed instanceof UsernameNotFoundException) {
                errorMessage += "Invalid email.";
            } else {
                errorMessage += failed.getMessage();
            }
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
