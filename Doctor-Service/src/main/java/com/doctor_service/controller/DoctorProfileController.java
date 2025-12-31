//package com.doctor_service.controller;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.doctor_service.command.dtos.DoctorStatus;
//import com.doctor_service.command.dtos.DoctorsDto;
//import com.doctor_service.services.DoctorProfileService;
//import com.doctor_service.services.Impl.DoctorSearchCriteria;
//
//import lombok.RequiredArgsConstructor;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/doctor-profile")
//public class DoctorProfileController {
//
//    private static final Logger logger = LoggerFactory.getLogger(DoctorProfileController.class);  // Initialize logger
//    private final DoctorProfileService doctorProfileService;
//
//    // Create a new doctor profile
//    @PostMapping("/create")
//    public ResponseEntity<DoctorsDto> createDoctorsProfile(@RequestBody DoctorsDto doctorsDto) {
//        try {
//            logger.info("Creating doctor profile for: {}", doctorsDto.getFirstName()+" "+doctorsDto.getLastName()); // Log the doctor creation request
//            DoctorsDto doctor = doctorProfileService.createDoctor(doctorsDto);
//            logger.info("Doctor profile created successfully with ID: {}", doctor.getId());
//            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
//        } catch (Exception e) {
//            logger.error("Error creating doctor profile: {}", e.getMessage(), e);  // Log the error if it occurs
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Update doctor profile
//    @PutMapping("/update/{doctorId}")
//    public ResponseEntity<DoctorsDto> updateDoctor(@PathVariable Integer doctorId,
//                                                   @Validated @RequestBody DoctorsDto doctorDto) {
//        try {
//            logger.info("Updating doctor profile with ID: {}", doctorId);
//            DoctorsDto updatedDoctor = doctorProfileService.updateDoctor(doctorId, doctorDto);
//            logger.info("Doctor profile updated successfully for ID: {}", doctorId);
//            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error updating doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
//
//    // Delete a doctor profile by ID
//    @DeleteMapping("/delete/{doctorId}")
//    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
//        try {
//            logger.info("Deleting doctor profile with ID: {}", doctorId);
//            doctorProfileService.deleteDoctorsProfile(doctorId);
//            logger.info("Doctor profile with ID: {} deleted successfully.", doctorId);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            logger.error("Error deleting doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PutMapping("/{doctorId}/verify")
//    public ResponseEntity<DoctorsDto> verifyDoctorsProfile(@PathVariable Integer doctorId) {
//        try {
//            logger.info("Verifying doctor profile with ID: {}", doctorId);
//            DoctorsDto verifyDoctorsProfile = doctorProfileService.verifyDoctorsProfile(doctorId);
//            logger.info("Doctor profile with ID: {} verified.", doctorId);
//            return new ResponseEntity<>(verifyDoctorsProfile, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error verifying doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//    // Get a doctor profile by ID
//    @GetMapping("/{doctorId}")
//    public ResponseEntity<DoctorsDto> getDoctorById(@PathVariable Integer doctorId) {
//        try {
//            logger.info("Fetching doctor profile with ID: {}", doctorId);
//            DoctorsDto getById = doctorProfileService.getDoctorById(doctorId);
//            if (getById != null) {
//                logger.info("Doctor profile fetched successfully for ID: {}", doctorId);
//                return new ResponseEntity<>(getById, HttpStatus.OK);
//            } else {
//                logger.warn("Doctor profile not found for ID: {}", doctorId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            logger.error("Error fetching doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Get the list of all doctors
//    @GetMapping
//    public ResponseEntity<List<DoctorsDto>> getDoctorsList() {
//        try {
//            logger.info("Fetching list of all doctors.");
//            List<DoctorsDto> allDoctors = doctorProfileService.getAllDoctors();
//            logger.info("Fetched {} doctors.", allDoctors.size());
//            return new ResponseEntity<>(allDoctors, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error fetching doctor list: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
//
//    @GetMapping("/verified-list")
//    public ResponseEntity<List<DoctorsDto>> getVerifiedDoctorsList() {
//        try {
//            logger.info("Fetching list of verified doctors.");
//            List<DoctorsDto> allVerifiedDoctorsList = doctorProfileService.getAllVerifiedDoctorsList();
//            logger.info("Fetched {} verified doctors.", allVerifiedDoctorsList.size());
//            return new ResponseEntity<>(allVerifiedDoctorsList, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error fetching verified doctors list: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/un-verified-list")
//    public ResponseEntity<List<DoctorsDto>> getUnVerifiedDoctorsList() {
//        try {
//            logger.info("Fetching list of unverified doctors.");
//            List<DoctorsDto> allUnVerifiedDoctorsList = doctorProfileService.getAllUnVerifiedDoctorsList();
//            logger.info("Fetched {} unverified doctors.", allUnVerifiedDoctorsList.size());
//            return new ResponseEntity<>(allUnVerifiedDoctorsList, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error fetching unverified doctors list: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<DoctorsDto>> searchDoctors(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String specialization,
//            @RequestParam(required = false) String location,
//            @RequestParam(required = false) String clinicName,
//            @RequestParam(required = false) Integer minExperience,
//            @RequestParam(required = false) BigDecimal maxFee,
//            @RequestParam(required = false) DoctorStatus status) {
//
//        try {
//            logger.info("Searching doctors with criteria: keyword={}, specialization={}, location={}, clinicName={}, minExperience={}, maxFee={}, status={}",
//                        keyword, specialization, location, clinicName, minExperience, maxFee, status);
//            DoctorSearchCriteria criteria = DoctorSearchCriteria.builder()
//                    .keyword(keyword)
//                    .specialization(specialization)
//                    .location(location)
//                    .clinicName(clinicName)
//                    .minExperience(minExperience)
//                    .maxFee(maxFee)
//                    .status(status)
//                    .build();
//            List<DoctorsDto> doctors = doctorProfileService.searchDoctors(criteria);
//            logger.info("Found {} doctors matching the search criteria.", doctors.size());
//            return ResponseEntity.ok(doctors);
//        } catch (Exception e) {
//            logger.error("Error searching doctors: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
//
////@RequiredArgsConstructor
////@RestController
////@RequestMapping("/api/v1/doctor-profile")
////public class DoctorProfileController {
////
////	private final DoctorProfileService doctorProfileService;
////
////	// Create a new doctor profile
////	@PostMapping("/create")
////	public ResponseEntity<DoctorsDto> createDoctorsProfile(@RequestBody DoctorsDto doctorsDto) {
////		DoctorsDto doctor = doctorProfileService.createDoctor(doctorsDto);
////		return new ResponseEntity<>(doctor, HttpStatus.CREATED);
////	}
////
////	// Get a doctor profile by ID
////	@GetMapping("/{doctorId}")
////	public ResponseEntity<DoctorsDto> getDoctorById(@PathVariable Integer doctorId) {
////		DoctorsDto getById = doctorProfileService.getDoctorById(doctorId);
////		return new ResponseEntity<>(getById, HttpStatus.OK);
////	}
////
////	// Get the list of all doctors
////	@GetMapping
////	public ResponseEntity<List<DoctorsDto>> getDoctorsList() {
////		List<DoctorsDto> allDoctors = doctorProfileService.getAllDoctors();
////		return new ResponseEntity<>(allDoctors, HttpStatus.OK);
////	}
////
////	@PutMapping("/update/{doctorId}")
////	public ResponseEntity<DoctorsDto> updateDoctor(@PathVariable Integer doctorId,
////			@Validated @RequestBody DoctorsDto doctorDto) {
////		DoctorsDto updatedDoctor = doctorProfileService.updateDoctor(doctorId, doctorDto);
////		return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
////	}
////
////	// Delete a doctor profile by ID
////	@DeleteMapping("/delete/{doctorId}")
////	public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
////		doctorProfileService.deleteDoctorsProfile(doctorId);
////		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
////	}
////
////	@PutMapping("/{doctorId}/verify")
////	public ResponseEntity<DoctorsDto> verifyDoctorsProfile(@PathVariable Integer doctorId) {
////		DoctorsDto verifyDoctorsProfile = doctorProfileService.verifyDoctorsProfile(doctorId);
////		return new ResponseEntity<>(verifyDoctorsProfile, HttpStatus.OK);
////	}
////
////	@GetMapping("/verified-list")
////	public ResponseEntity<List<DoctorsDto>> getVerifiedDoctorsList() {
////	  List<DoctorsDto> allVerifiedDoctorsList = doctorProfileService.getAllVerifiedDoctorsList();
////		return new ResponseEntity<>(allVerifiedDoctorsList, HttpStatus.OK);
////	}
////	@GetMapping("/un-verified-list")
////	public ResponseEntity<List<DoctorsDto>> getUnVerifiedDoctorsList() {
////		List<DoctorsDto> allUnVerifiedDoctorsList = doctorProfileService.getAllUnVerifiedDoctorsList();
////		return new ResponseEntity<>(allUnVerifiedDoctorsList, HttpStatus.OK);
////	}
////
////	@GetMapping("/search")
////    public ResponseEntity<List<DoctorsDto>> searchDoctors(
////            @RequestParam(required = false) String keyword,
////            @RequestParam(required = false) String specialization,
////            @RequestParam(required = false) String location,
////            @RequestParam(required = false) String clinicName,
////            @RequestParam(required = false) Integer minExperience,
////            @RequestParam(required = false) BigDecimal maxFee,
////            @RequestParam(required = false) DoctorStatus status) {
////
////        DoctorSearchCriteria criteria = DoctorSearchCriteria.builder()
////                .keyword(keyword)
////                .specialization(specialization)
////                .location(location)
////                .clinicName(clinicName)
////                .minExperience(minExperience)
////                .maxFee(maxFee)
////                .status(status)
////                .build();
////
////        List<DoctorsDto> doctors = doctorProfileService.searchDoctors(criteria);
////        return ResponseEntity.ok(doctors);
////    }
////}