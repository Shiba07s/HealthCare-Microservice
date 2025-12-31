package com.appointment_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus {
    BOOKED("Booked"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String displayName;
 
}
