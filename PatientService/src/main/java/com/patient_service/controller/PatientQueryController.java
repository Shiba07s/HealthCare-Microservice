package com.patient_service.controller;

 import com.patient_service.query.dtos.PatientQueryDto;
 import com.patient_service.query.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient-profile")
@RequiredArgsConstructor
public class PatientQueryController {

    private static final Logger logger = LoggerFactory.getLogger(PatientQueryController.class);

    private final PatientQueryService patientQueryService;

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('Patient')")
    public ResponseEntity<PatientQueryDto> getPatient(@PathVariable int patientId) {
        logger.info("Received request to fetch patient with ID: {}", patientId);

        try {
            PatientQueryDto patientDto = patientQueryService.getPatient(patientId);
            logger.info("Fetched patient: {}", patientDto);
            return new ResponseEntity<>(patientDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching patient with ID {}: {}", patientId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Doctor','Admin')")
    public ResponseEntity<List<PatientQueryDto>> getAllPatients() {
        logger.info("Received request to fetch all patients");

        try {
            List<PatientQueryDto> patients = patientQueryService.getAllPatients();
            logger.info("Fetched {} patient(s)", patients.size());
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching all patients: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}