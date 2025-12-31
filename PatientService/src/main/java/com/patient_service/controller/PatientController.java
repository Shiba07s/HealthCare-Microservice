//package com.patient_service.controller;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.patient_service.command.dtos.PatientDto;
//import com.patient_service.services.PatientService;
//
//
//@RestController
//@RequestMapping("/api/v1/patient-profile")
//public class PatientController {
//
//    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
//
//    @Autowired
//    private PatientService patientService;
//
//    @PostMapping("/create")
//    public ResponseEntity<PatientDto> registerPatient(@RequestBody PatientDto patientDto) {
//        logger.info("Registering new patient: {}", patientDto);
//        try {
//            PatientDto savedPatient = patientService.createNewPatient(patientDto);
//            logger.info("Patient registered successfully: {}", savedPatient);
//            return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
//        } catch (Exception e) {
//            logger.error("Error registering patient: {}", e.getMessage(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/update/{patientId}")
//    public ResponseEntity<PatientDto> updatePatient(@PathVariable int patientId, @RequestBody PatientDto patientDTO) {
//        logger.info("Updating patient with ID: {}", patientId);
//        try {
//            PatientDto updatedPatient = patientService.updatePatient(patientId, patientDTO);
//            logger.info("Patient updated successfully: {}", updatedPatient);
//            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error updating patient with ID {}: {}", patientId, e.getMessage(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/{patientId}")
//    public ResponseEntity<PatientDto> getPatient(@PathVariable int patientId) {
//        logger.info("Fetching patient with ID: {}", patientId);
//        try {
//            PatientDto patientDTO = patientService.getPatient(patientId);
//            logger.info("Fetched patient: {}", patientDTO);
//            return new ResponseEntity<>(patientDTO, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error fetching patient with ID {}: {}", patientId, e.getMessage(), e);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PatientDto>> getAllPatient() {
//        logger.info("Fetching all patient details");
//        try {
//            List<PatientDto> allDetails = patientService.getALlDetails();
//            logger.info("Fetched {} patient(s)", allDetails.size());
//            return new ResponseEntity<>(allDetails, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error fetching all patients: {}", e.getMessage(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
//
////@RestController
////@RequestMapping("/api/v1/patient-profile")
////public class PatientController {
////
////	@Autowired
////	private PatientService patientService;
////
////	@PostMapping("/create")
////	public ResponseEntity<PatientDto> registerPatient(@RequestBody PatientDto patientDto) {
////		PatientDto savedPatient = patientService.createNewPatient(patientDto);
////		return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
////	}
////
////	@PutMapping("/update/{patientId}")
////	public ResponseEntity<PatientDto> updatePatient(@PathVariable int patientId, @RequestBody PatientDto patientDTO) {
////		PatientDto updatedPatient = patientService.updatePatient(patientId, patientDTO);
////		return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
////	}
////
////	@GetMapping("/{patientId}")
////	public ResponseEntity<PatientDto> getPatient(@PathVariable int patientId) {
////		PatientDto patientDTO = patientService.getPatient(patientId);
////		return new ResponseEntity<>(patientDTO, HttpStatus.OK);
////	}
////
////	@GetMapping
////	public ResponseEntity<List<PatientDto>> getAllPatient() {
////		List<PatientDto> aLlDetails = patientService.getALlDetails();
////		return new ResponseEntity<>(aLlDetails, HttpStatus.OK);
////	}
////}