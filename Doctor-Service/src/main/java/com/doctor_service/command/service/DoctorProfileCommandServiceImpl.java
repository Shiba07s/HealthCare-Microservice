package com.doctor_service.command.service;

import com.doctor_service.command.dtos.DoctorStatus;
import com.doctor_service.command.dtos.DoctorsCommandDto;
import com.doctor_service.command.entities.Doctors;
import com.doctor_service.command.repositories.DoctorsCommandRepository;
import com.doctor_service.events.DoctorEvent;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.query.dtos.DoctorsViewDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DoctorProfileCommandServiceImpl implements DoctorProfileCommandService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorProfileCommandServiceImpl.class);  // Initialize logger
    private static final String DOCTOR_TOPIC = "doctors-events2";
    private final DoctorsCommandRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, DoctorEvent> kafkaTemplate;
    private static final String CACHE_NAME = "doctors";

    @Override
    @CachePut(value = CACHE_NAME, key = "#result.id")
    public DoctorsCommandDto createDoctor(DoctorsCommandDto doctorDto) {
        try {
            logger.info("Creating doctor profile: {}", doctorDto.getFirstName() + " " + doctorDto.getLastName()); // Log the doctor creation request

            Doctors map = modelMapper.map(doctorDto, Doctors.class);
            map.setStatus(DoctorStatus.UNVERIFIED);
            Doctors save = doctorRepository.save(map);

            DoctorsCommandDto savedDto = modelMapper.map(save, DoctorsCommandDto.class);

            //create and publish event
            DoctorEvent doctorEvent = modelMapper.map(savedDto, DoctorEvent.class);
            doctorEvent.setEventType(DoctorEvent.EventType.CREATED);

            //send to kafka
            kafkaTemplate.send(DOCTOR_TOPIC, String.valueOf(savedDto.getId()), doctorEvent);
            logger.info("Doctor created event published to Kafka");

            // Clear the allDoctors cache when a doctor is updated
            clearAllDoctorsCache();

            // initiateVerification(save); // Commented as per original code

            logger.info("Doctor profile created successfully with ID: {}", save.getId());
            return savedDto;
        } catch (Exception e) {
            logger.error("Error creating doctor profile: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating doctor profile", e);  // Optionally rethrow or handle error
        }
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "#doctorId")
    public DoctorsCommandDto updateDoctor(Integer doctorId, DoctorsCommandDto doctorDto) {
        try {
            logger.info("Updating doctor profile with ID: {}", doctorId);

            Doctors doctorsDetails = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));

            DoctorStatus status = doctorsDetails.getStatus();
            var createdAt = doctorsDetails.getCreatedAt();
            modelMapper.map(doctorDto, doctorsDetails);
            doctorsDetails.setStatus(status);
            doctorsDetails.setCreatedAt(createdAt);

            Doctors update = doctorRepository.save(doctorsDetails);
            logger.info("Doctor profile with ID: {} updated successfully.", doctorId);

            DoctorsCommandDto updatedDto = modelMapper.map(update, DoctorsCommandDto.class);

            // Create and publish UPDATE event
            DoctorEvent doctorEvent = modelMapper.map(updatedDto, DoctorEvent.class);
            doctorEvent.setEventType(DoctorEvent.EventType.UPDATED);

            // Send to Kafka
            kafkaTemplate.send(DOCTOR_TOPIC, String.valueOf(updatedDto.getId()), doctorEvent);
            logger.info("Doctor updated event published to Kafka");

            // Clear the allDoctors cache when a doctor is updated
            clearAllDoctorsCache();

            return updatedDto;
        } catch (Exception e) {
            logger.error("Error updating doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            throw new RuntimeException("Error updating doctor profile", e);
        }
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#doctorId")
    public void deleteDoctorsProfile(Integer doctorId) {
        try {
            logger.info("Deleting doctor profile with ID: {}", doctorId);

            Doctors doctorsDetails = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));

            // Create DELETE event before deletion
            DoctorsCommandDto doctorDto = modelMapper.map(doctorsDetails, DoctorsCommandDto.class);
            DoctorEvent doctorEvent = modelMapper.map(doctorDto, DoctorEvent.class);
            doctorEvent.setEventType(DoctorEvent.EventType.DELETED);

            // Delete from database
            doctorRepository.delete(doctorsDetails);

            // Send to Kafka after deletion
            kafkaTemplate.send(DOCTOR_TOPIC, String.valueOf(doctorId), doctorEvent);
            logger.info("Doctor delete event published to Kafka");

            // Clear the allDoctors cache when a doctor is updated
            clearAllDoctorsCache();

            logger.info("Doctor profile with ID: {} deleted successfully.", doctorId);
        } catch (Exception e) {
            logger.error("Error deleting doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            throw new RuntimeException("Error deleting doctor profile", e);
        }
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "#doctorId")
    public DoctorsCommandDto verifyDoctorsProfile(Integer doctorId) {
        try {
            logger.info("Verifying doctor profile with ID: {}", doctorId);

            Doctors doctorsDetails = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));

            doctorsDetails.setStatus(DoctorStatus.VERIFIED);
            Doctors updateStatus = doctorRepository.save(doctorsDetails);

            DoctorsCommandDto verifiedDto = modelMapper.map(updateStatus, DoctorsCommandDto.class);

            // Create and publish UPDATE event for verification
            DoctorEvent doctorEvent = modelMapper.map(verifiedDto, DoctorEvent.class);
            doctorEvent.setEventType(DoctorEvent.EventType.UPDATED);

            // Send to Kafka
            kafkaTemplate.send(DOCTOR_TOPIC, String.valueOf(verifiedDto.getId()), doctorEvent);
            logger.info("Doctor verification event published to Kafka");

            // Clear the allDoctors cache when a doctor is updated
            clearAllDoctorsCache();

            logger.info("Doctor profile with ID: {} verified successfully.", doctorId);
            return verifiedDto;
        } catch (Exception e) {
            logger.error("Error verifying doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            throw new RuntimeException("Error verifying doctor profile", e);
        }
    }


    /**
     * Helper method to clear all doctors-related caches
     */
    @CacheEvict(value = CACHE_NAME, key = "'allDoctors'")
    public void clearAllDoctorsCache() {
        logger.info("Cleared 'allDoctors' cache due to data modification");

    }
}
//    @Override
//    public DoctorsCommandDto updateDoctor(Integer doctorId, DoctorsCommandDto doctorDto) {
//        try {
//            logger.info("Updating doctor profile with ID: {}", doctorId);
//
//            Doctors doctorsDetails = doctorRepository.findById(doctorId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));
//
//            DoctorStatus status = doctorsDetails.getStatus();
//            var createdAt = doctorsDetails.getCreatedAt();
//            modelMapper.map(doctorDto, doctorsDetails);
//            doctorsDetails.setStatus(status);
//            doctorsDetails.setCreatedAt(createdAt);
//
//            Doctors update = doctorRepository.save(doctorsDetails);
//            logger.info("Doctor profile with ID: {} updated successfully.", doctorId);
//
//            return modelMapper.map(update, DoctorsCommandDto.class);
//        } catch (Exception e) {
//            logger.error("Error updating doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            throw new RuntimeException("Error updating doctor profile", e);
//        }    }
//
//    @Override
//    public void deleteDoctorsProfile(Integer doctorId) {
//        try {
//            logger.info("Deleting doctor profile with ID: {}", doctorId);
//
//            Doctors doctorsDetails = doctorRepository.findById(doctorId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));
//
//            doctorRepository.delete(doctorsDetails);
//            logger.info("Doctor profile with ID: {} deleted successfully.", doctorId);
//        } catch (Exception e) {
//            logger.error("Error deleting doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            throw new RuntimeException("Error deleting doctor profile", e);
//        }
//    }
//
//    @Override
//    public DoctorsCommandDto verifyDoctorsProfile(Integer doctorId) {
//        try {
//            logger.info("Verifying doctor profile with ID: {}", doctorId);
//
//            Doctors doctorsDetails = doctorRepository.findById(doctorId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));
//
//            doctorsDetails.setStatus(DoctorStatus.VERIFIED);
//            Doctors updateStatus = doctorRepository.save(doctorsDetails);
//
//            logger.info("Doctor profile with ID: {} verified successfully.", doctorId);
//            return modelMapper.map(updateStatus, DoctorsCommandDto.class);
//        } catch (Exception e) {
//            logger.error("Error verifying doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
//            throw new RuntimeException("Error verifying doctor profile", e);
//        }
//    }
