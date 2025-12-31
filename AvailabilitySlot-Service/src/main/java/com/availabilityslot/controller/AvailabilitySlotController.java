package com.availabilityslot.controller;

import java.util.List;

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

import com.availabilityslot.dtos.AvailabilitySlotDto;
import com.availabilityslot.services.AvailabilitySlotService;

import lombok.RequiredArgsConstructor;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/availability-slots")
@RequiredArgsConstructor
public class AvailabilitySlotController {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilitySlotController.class);
    
    private final AvailabilitySlotService availabilitySlotService;

    @PostMapping("/create")
    public ResponseEntity<AvailabilitySlotDto> createNewAvailabilitySlot(
            @RequestBody AvailabilitySlotDto availabilitySlotDto) {
        logger.info("Creating a new availability slot with details: {}", availabilitySlotDto);
        AvailabilitySlotDto newAvailabilitySlot = availabilitySlotService
                .createNewAvailabilitySlot(availabilitySlotDto);
        logger.info("Created new availability slot: {}", newAvailabilitySlot);
        return new ResponseEntity<>(newAvailabilitySlot, HttpStatus.CREATED);
    }

    @GetMapping("/{availabiltySlotId}")
    public ResponseEntity<AvailabilitySlotDto> getAvailabilitySlot(@PathVariable Integer availabiltySlotId) {
        logger.info("Fetching availability slot with ID: {}", availabiltySlotId);
        AvailabilitySlotDto availabilitySlot = availabilitySlotService.getAvailabilitySlot(availabiltySlotId);
        if (availabilitySlot == null) {
            logger.warn("Availability slot with ID {} not found", availabiltySlotId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Fetched availability slot: {}", availabilitySlot);
        return new ResponseEntity<>(availabilitySlot, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<?>> allAvailabilitySlot() {
        logger.info("Fetching all availability slots");
        List<AvailabilitySlotDto> allAvailabilitySlot = availabilitySlotService.getAllAvailabilitySlot();
        logger.info("Fetched {} availability slots", allAvailabilitySlot.size());
        return new ResponseEntity<>(allAvailabilitySlot, HttpStatus.OK);
    }

    @PutMapping("/update/{availabiltySlotId}")
    public ResponseEntity<AvailabilitySlotDto> updateAvailabilitySlotDetails(@PathVariable Integer availabiltySlotId,
            @RequestBody AvailabilitySlotDto availabilitySlotDto) {
        logger.info("Updating availability slot with ID {} with new details: {}", availabiltySlotId, availabilitySlotDto);
        AvailabilitySlotDto updatedAvailabilitySlot = availabilitySlotService
                .updateAvailabilitySlotDetails(availabiltySlotId, availabilitySlotDto);
        logger.info("Updated availability slot with ID {}: {}", availabiltySlotId, updatedAvailabilitySlot);
        return new ResponseEntity<>(updatedAvailabilitySlot, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{availabiltySlotId}")
    public ResponseEntity<String> deleteSlot(@PathVariable Integer availabiltySlotId) {
        logger.info("Deleting availability slot with ID: {}", availabiltySlotId);
        availabilitySlotService.deleteAvailabilitySlotDetails(availabiltySlotId);
        logger.info("Successfully deleted availability slot with ID: {}", availabiltySlotId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


//@RestController
//@RequestMapping("/api/v1/availability-slots")
//@RequiredArgsConstructor
//public class AvailabilitySlotController {
//
//	private final AvailabilitySlotService availabilitySlotService;
//
//	@PostMapping("/create")
//	public ResponseEntity<AvailabilitySlotDto> createNewAvailabilitySlot(
//			@RequestBody AvailabilitySlotDto availabilitySlotDto) {
//		AvailabilitySlotDto newAvailabilitySlot = availabilitySlotService
//				.createNewAvailabilitySlot(availabilitySlotDto);
//		return new ResponseEntity<AvailabilitySlotDto>(newAvailabilitySlot, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/{availabiltySlotId}")
//	public ResponseEntity<AvailabilitySlotDto> getAvailabilitySlot(@PathVariable Integer availabiltySlotId) {
//		AvailabilitySlotDto availabilitySlot = availabilitySlotService.getAvailabilitySlot(availabiltySlotId);
//		return new ResponseEntity<AvailabilitySlotDto>(availabilitySlot, HttpStatus.OK);
//	}
//
//	@GetMapping
//	public ResponseEntity<List<?>> allAvailabilitySlot() {
//		 List<AvailabilitySlotDto> allAvailabilitySlot = availabilitySlotService.getAllAvailabilitySlot();
//		return new ResponseEntity<List<?>>(allAvailabilitySlot, HttpStatus.OK);
//	}
//
//	@PutMapping("/update/{availabiltySlotId}")
//	public ResponseEntity<AvailabilitySlotDto> updateAvailabilitySlotDetails(@PathVariable Integer availabiltySlotId,
//			@RequestBody AvailabilitySlotDto availabilitySlotDto) {
//
//		AvailabilitySlotDto updateAvailabilitySlotDetails = availabilitySlotService
//				.updateAvailabilitySlotDetails(availabiltySlotId, availabilitySlotDto);
//		return new ResponseEntity<AvailabilitySlotDto>(updateAvailabilitySlotDetails, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/delete/{availabiltySlotId}")
//	public ResponseEntity<String> deleteSlot(@PathVariable Integer availabiltySlotId) {
//		availabilitySlotService.deleteAvailabilitySlotDetails(availabiltySlotId);
//		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//	}
//}