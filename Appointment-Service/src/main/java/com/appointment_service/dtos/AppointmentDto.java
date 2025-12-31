package com.appointment_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.appointment_service.client.DoctorsDto;
import com.appointment_service.client.PatientDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

	private Integer id;
	
    // IDs for database operations
    private Integer doctorId;
    private Integer patientId;
    
    // Full objects for response data
    private DoctorsDto doctor;
    private PatientDto patient;
    
	private LocalDateTime appointmentDate;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
//    private String durationOfAppointment;
	private String consultationLink;
	private BigDecimal consultationFee;
	
	private String cancellationReason;
	
	private LocalDateTime createdAt;
    
	private LocalDateTime updatedAt;

	private AppointmentStatus status; // BOOKED, CANCELLED, COMPLETED
	private ConsultationType consultationType;

//	private DoctorsDto doctors;
//	private PatientDto patient;
//	private PrescriptionDto prescription;
//	private Payment payment;

}