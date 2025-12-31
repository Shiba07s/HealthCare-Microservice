package com.patient_service.command.service;


import com.patient_service.command.dtos.PatientCommandDto;
import com.patient_service.command.entities.Patient;
import com.patient_service.command.repository.PatientCommandRepository;
import com.patient_service.events.PatientEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PatientCommandServiceImpl implements PatientCommandService {

    private static final Logger logger = LoggerFactory.getLogger(PatientCommandServiceImpl.class);
    private static final String PATIENT_TOPIC = "patient-events4";

    private final PatientCommandRepository patientRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, PatientEvent> kafkaTemplate;

    @Override
    public PatientCommandDto createPatient(PatientCommandDto patientDto) {
        logger.info("Creating new patient: {}", patientDto);

        Patient patient = modelMapper.map(patientDto, Patient.class);

        Patient savedPatient = patientRepository.save(patient);
        logger.info("Patient created successfully: {}", savedPatient);

        PatientCommandDto savedDto = modelMapper.map(savedPatient, PatientCommandDto.class);

        // Create and publish event
        PatientEvent event = modelMapper.map(savedPatient, PatientEvent.class);
        event.setEventType(PatientEvent.EventType.CREATED);

        // Send to Kafka
        kafkaTemplate.send(PATIENT_TOPIC, String.valueOf(savedPatient.getId()), event);
        logger.info("Patient created event published to Kafka");

        return savedDto;
    }

    @Override
    public PatientCommandDto updatePatient(int patientId, PatientCommandDto patientDto) {
        logger.info("Updating patient with ID: {}", patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        LocalDateTime createdAt = patient.getCreatedAt();
        modelMapper.map(patientDto, patient);
        patient.setId(patientId);
        patient.setCreatedAt(createdAt);

        Patient updatedPatient = patientRepository.save(patient);
        logger.info("Patient updated successfully: {}", updatedPatient);

        // Map back to DTO
        PatientCommandDto updatedDto = modelMapper.map(updatedPatient, PatientCommandDto.class);

        // Create and publish event
        PatientEvent event = modelMapper.map(updatedPatient, PatientEvent.class);
        event.setEventType(PatientEvent.EventType.UPDATED);

        // Send to Kafka
        kafkaTemplate.send(PATIENT_TOPIC, String.valueOf(updatedPatient.getId()), event);
        logger.info("Patient updated event published to Kafka");

        return updatedDto;
    }
}