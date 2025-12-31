package com.doctor_service.command.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.doctor_service.command.dtos.DoctorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors_profile")
public class Doctors {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String registrationNumber;

	@Column(name = "year_of_registration")
	private String yearOfRegistration;

	@Column(name = "council_name")
	private String councilName;

	private String specialization;
	private String qualification;

	@Column(name = "years_of_experience")
	private Integer yearsOfExperience;

	@Column(name = "clinic_name")
	private String clinicName;

	@Column(name = "clinic_address")
	private String clinicAddress;

	@Column(name = "clinic_location")
	private String clinicLocation;

	@Column(name = "consultation_fee")
	private BigDecimal consultationFee;

	private String letterheadFormat; // Path to the stored letterhead file

	private String bankAccountNumber;
	private String ifscCode;



	@Enumerated(EnumType.STRING)
	private DoctorStatus status;


	
	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

//	@ManyToOne
//	private Department departments;
//
//	@OneToMany(mappedBy = "doctors")
//	private List<Appointment> appointments;
//
//	@OneToMany(mappedBy = "doctors")
//	private List<AvailabilitySlot> availabilitySlots;
//	@OneToMany(mappedBy = "doctors")
//	private List<Prescription> prescriptions;

}
