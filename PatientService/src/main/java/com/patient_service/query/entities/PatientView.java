package com.patient_service.query.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient_views")
public class PatientView {

	@Id
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

	@Column(columnDefinition = "TEXT")
	private String medicalHistory;

	@Column(columnDefinition = "TEXT")
	private String allergies;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}

