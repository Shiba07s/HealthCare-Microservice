package com.patient_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientEvent {
    public enum EventType {
        CREATED, UPDATED,DELETED
    }

    private EventType eventType;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String height;
    private String weight;
    private String emergencyContact;
    private String medicalHistory;
    private String allergies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}