package com.doctor_service.events;


import com.doctor_service.command.dtos.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEvent {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }
    private EventType eventType;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String registrationNumber;
    private String yearOfRegistration;
    private String councilName;
    private String specialization;
    private String qualification;
    private Integer yearsOfExperience;
    private String clinicName;
    private String clinicAddress;
    private String clinicLocation;
    private BigDecimal consultationFee;
    private String letterheadFormat;
    private String bankAccountNumber;
    private String ifscCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DoctorStatus status;

}
