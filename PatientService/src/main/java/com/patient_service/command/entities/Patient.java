package com.patient_service.command.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "patient_profiles")
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private LocalDate dateOfBirth;

	private String gender;

	@Column(name = "blood_group")
	private String bloodGroup;

	private String height;

	private String weight;

	@Column(name = "emergency_contact")
	private String emergencyContact;

	@Column(name = "medical_history")
	private String medicalHistory;

	@Column(name = "allergies")
	private String allergies;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

//	@OneToMany(mappedBy = "patient")
//	private List<Appointment> appointments;

//	@OneToMany(mappedBy = "patient")
//	private List<Prescription> prescriptions;
    
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
//    private List<Appointment> appointments;
//
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
//    private List<MedicalRecord> medicalRecords;
//
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
//    private List<Bill> bills;

}
