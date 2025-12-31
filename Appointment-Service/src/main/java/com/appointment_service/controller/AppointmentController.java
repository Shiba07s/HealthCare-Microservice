package com.appointment_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appointment_service.dtos.AppointmentDto;
import com.appointment_service.exception.ResourceNotFoundException;
import com.appointment_service.services.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
	private final AppointmentService appointmentService;

	@PostMapping("/create")
	public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
		logger.info("Received request to create appointment: {}", appointmentDto);
		System.out.println("------------"+appointmentDto);

		try {
			AppointmentDto createdAppointment = appointmentService.bookAppointment(appointmentDto);
			logger.info("Appointment successfully booked with id: {}", createdAppointment.getId());
			return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
		} catch (ResourceNotFoundException ex) {
			logger.error("Resource not found: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Failed to book appointment: {}", ex.getMessage(), ex);
			throw new RuntimeException("Failed to book appointment: " + ex.getMessage());
		}
	}

	@GetMapping("/{appointmentId}")
	public ResponseEntity<AppointmentDto> getAppointmentDetailsById(@PathVariable int appointmentId) {
		logger.info("Fetching appointment details for id: {}", appointmentId);

		AppointmentDto appointment = appointmentService.getAppointment(appointmentId);
		logger.info("Appointment details retrieved for id: {}", appointmentId);

		return new ResponseEntity<>(appointment, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<AppointmentDto>> getAllAppointmentDetails() {
		logger.info("Fetching all appointment details");

		List<AppointmentDto> allAppointments = appointmentService.getAllAppointments();
		logger.info("Retrieved all appointments. Total count: {}", allAppointments.size());

		return new ResponseEntity<>(allAppointments, HttpStatus.OK);
	}

	@PutMapping("/update/{appointmentId}")
	public ResponseEntity<AppointmentDto> updateAppointmentDetails(@PathVariable Integer appointmentId,
																   @RequestBody AppointmentDto appointmentDto) {
		logger.info("Received request to update appointment with id: {}", appointmentId);

		AppointmentDto updatedAppointment = appointmentService.updateAppointment(appointmentId, appointmentDto);
		logger.info("Appointment with id {} successfully updated", appointmentId);

		return new ResponseEntity<>(updatedAppointment, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{appointmentId}")
	public ResponseEntity<String> deleteAppointmentDetails(@PathVariable Integer appointmentId) {
		logger.info("Received request to delete appointment with id: {}", appointmentId);

		appointmentService.deleteAppointmentData(appointmentId);
		logger.info("Appointment with id {} successfully deleted", appointmentId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
