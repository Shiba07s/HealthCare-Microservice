package com.patient_service.controller;

import com.patient_service.command.dtos.PatientCommandDto;
import com.patient_service.command.service.PatientCommandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient-profile")
@RequiredArgsConstructor
public class PatientCommandController {

    private static final Logger logger = LoggerFactory.getLogger(PatientCommandController.class);

    private final PatientCommandService patientCommandService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('Patient')")
    public ResponseEntity<PatientCommandDto> createPatient(@RequestBody PatientCommandDto patientDto) {
        logger.info("Received request to create patient: {}", patientDto);

        try {
            PatientCommandDto createdPatient = patientCommandService.createPatient(patientDto);
            logger.info("Patient created successfully: {}", createdPatient);
            return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating patient: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{patientId}")
    @PreAuthorize("hasAnyRole('Patient')")
    public ResponseEntity<PatientCommandDto> updatePatient(
            @PathVariable int patientId,
            @RequestBody PatientCommandDto patientDto) {
        logger.info("Received request to update patient with ID {}: {}", patientId, patientDto);

        try {
            PatientCommandDto updatedPatient = patientCommandService.updatePatient(patientId, patientDto);
            logger.info("Patient updated successfully: {}", updatedPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating patient with ID {}: {}", patientId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}