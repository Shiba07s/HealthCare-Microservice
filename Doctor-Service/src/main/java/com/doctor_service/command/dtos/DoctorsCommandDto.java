package com.doctor_service.command.dtos;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorsCommandDto {

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
	
//    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")   
	private LocalDateTime createdAt;
    
//    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")   
	private LocalDateTime updatedAt;
	private DoctorStatus status;

//	private DepartmentDtoResponse departments;

//	private List<AppointmentDto> appointments;
//	private List<AvailabilitySlot> availabilitySlots;
//	private List<Prescription> prescriptions;

}