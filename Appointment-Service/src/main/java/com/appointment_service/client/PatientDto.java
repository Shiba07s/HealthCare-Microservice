package com.appointment_service.client;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

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
	
//    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")   
	private LocalDateTime createdAt;
    
//    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")   
	private LocalDateTime updatedAt;
//	private List<AppointmentDto> appointments;
//	private List<Prescription> prescriptions;

}