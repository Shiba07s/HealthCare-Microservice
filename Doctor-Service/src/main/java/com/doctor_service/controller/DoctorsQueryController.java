package com.doctor_service.controller;

import com.doctor_service.command.dtos.DoctorStatus;
import com.doctor_service.query.dtos.DoctorsViewDto;
import com.doctor_service.query.service.DoctorProfileViewService;
import com.doctor_service.query.service.DoctorSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/doctor-profile")
public class DoctorsQueryController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorsCommandController.class);  // Initialize logger
    private final DoctorProfileViewService doctorProfileService;

    @GetMapping("/{doctorId}")
    @PreAuthorize("hasAnyRole('Doctor')")
    public ResponseEntity<DoctorsViewDto> getDoctorById(@PathVariable Integer doctorId) {
        try {
            logger.info("Fetching doctor profile with ID: {}", doctorId);
            DoctorsViewDto getById = doctorProfileService.getDoctorById(doctorId);

            if (getById == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(getById);
        } catch (AccessDeniedException e) {
            logger.error("Error fetching doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            logger.error("Error fetching doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get the list of all doctors
    @GetMapping
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<List<DoctorsViewDto>> getDoctorsList() {
        try {
            logger.info("Fetching list of all doctors.");
            List<DoctorsViewDto> allDoctors = doctorProfileService.getAllDoctors();
            logger.info("Fetched {} doctors.", allDoctors.size());
            return new ResponseEntity<>(allDoctors, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching doctor list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/verified-list")
    @PreAuthorize("hasAnyRole('Admin','Doctor','Patient')")
    public ResponseEntity<List<DoctorsViewDto>> getVerifiedDoctorsList() {
        try {
            logger.info("Fetching list of verified doctors.");
            List<DoctorsViewDto> allVerifiedDoctorsList = doctorProfileService.getAllVerifiedDoctorsList();
            logger.info("Fetched {} verified doctors.", allVerifiedDoctorsList.size());
            return new ResponseEntity<>(allVerifiedDoctorsList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching verified doctors list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/un-verified-list")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<List<DoctorsViewDto>> getUnVerifiedDoctorsList() {
        try {
            logger.info("Fetching list of unverified doctors.");
            List<DoctorsViewDto> allUnVerifiedDoctorsList = doctorProfileService.getAllUnVerifiedDoctorsList();
            logger.info("Fetched {} unverified doctors.", allUnVerifiedDoctorsList.size());
            return new ResponseEntity<>(allUnVerifiedDoctorsList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching unverified doctors list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('Admin','Doctor','Patient')")
    public ResponseEntity<List<DoctorsViewDto>> searchDoctors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String clinicName,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) BigDecimal maxFee,
            @RequestParam(required = false) DoctorStatus status) {

        try {
            logger.info("Searching doctors with criteria: keyword={}, specialization={}, location={}, clinicName={}, minExperience={}, maxFee={}, status={}",
                    keyword, specialization, location, clinicName, minExperience, maxFee, status);
            DoctorSearchCriteria criteria = DoctorSearchCriteria.builder()
                    .keyword(keyword)
                    .specialization(specialization)
                    .location(location)
                    .clinicName(clinicName)
                    .minExperience(minExperience)
                    .maxFee(maxFee)
                    .status(status)
                    .build();
            List<DoctorsViewDto> doctors = doctorProfileService.searchDoctors(criteria);
            logger.info("Found {} doctors matching the search criteria.", doctors.size());
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            logger.error("Error searching doctors: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
