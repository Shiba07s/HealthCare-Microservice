package com.doctor_service.controller;

import com.doctor_service.command.dtos.DoctorsCommandDto;
import com.doctor_service.command.service.DoctorProfileCommandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/doctor-profile")
public class DoctorsCommandController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorsCommandController.class);  // Initialize logger
    private final DoctorProfileCommandService doctorProfileService;

    // Create a new doctor profile
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('Doctor')")
    public ResponseEntity<DoctorsCommandDto> createDoctorsProfile(@RequestBody DoctorsCommandDto doctorsDto) {
        try {
            logger.info("Creating doctor profile for: {}", doctorsDto.getFirstName() + " " + doctorsDto.getLastName()); // Log the doctor creation request
            DoctorsCommandDto doctor = doctorProfileService.createDoctor(doctorsDto);
            logger.info("Doctor profile created successfully with ID: {}", doctor.getId());
            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating doctor profile: {}", e.getMessage(), e);  // Log the error if it occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update doctor profile
    @PutMapping("/update/{doctorId}")
    @PreAuthorize("hasAnyRole('Doctor')")
    public ResponseEntity<DoctorsCommandDto> updateDoctor(@PathVariable Integer doctorId,
                                                   @Validated @RequestBody DoctorsCommandDto doctorDto) {
        try {
            logger.info("Updating doctor profile with ID: {}", doctorId);
            DoctorsCommandDto updatedDoctor = doctorProfileService.updateDoctor(doctorId, doctorDto);
            logger.info("Doctor profile updated successfully for ID: {}", doctorId);
            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Delete a doctor profile by ID
    @DeleteMapping("/delete/{doctorId}")
    @PreAuthorize("hasAnyRole('Admin','Doctor')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
        try {
            logger.info("Deleting doctor profile with ID: {}", doctorId);
            doctorProfileService.deleteDoctorsProfile(doctorId);
            logger.info("Doctor profile with ID: {} deleted successfully.", doctorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{doctorId}/verify")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<DoctorsCommandDto> verifyDoctorsProfile(@PathVariable Integer doctorId) {
        try {
            logger.info("Verifying doctor profile with ID: {}", doctorId);
            DoctorsCommandDto verifyDoctorsProfile = doctorProfileService.verifyDoctorsProfile(doctorId);
            logger.info("Doctor profile with ID: {} verified.", doctorId);
            return new ResponseEntity<>(verifyDoctorsProfile, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error verifying doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}