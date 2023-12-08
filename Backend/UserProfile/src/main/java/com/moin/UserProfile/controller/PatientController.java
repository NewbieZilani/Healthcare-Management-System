package com.moin.UserProfile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moin.UserProfile.service.serviceImplementation.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @GetMapping("/callGpt")
    public void callGptWithPatientData() {
        try {
            patientService.callGptWithPatientData();
        } catch (JsonProcessingException ignored) {

        }
    }
}
