package com.appointment_service.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.appointment_service.dtos.AppointmentStatus;
import com.appointment_service.dtos.ConsultationType;

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
@Table(name="appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Doctor and Patient relationships as foreign keys
    @Column(name = "doctor_id")
    private Integer doctorId;
    
    @Column(name = "patient_id")
    private Integer patientId;
    
    

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // BOOKED, CANCELLED, COMPLETED

    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
    
//    @Column(name="duration_of_appointment")
//    private String durationOfAppointment;

    @Column(name = "consultation_type")
    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType;

    @Column(name = "consultation_link")
    private String consultationLink;

    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;


    @Column(name = "cancellation_reason")
    private String cancellationReason;
    
    // Audit fields
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    

//    @manytoone
//    @joincolumn(name = "doctor_id")
//    private doctors doctors;
//
//    @manytoone
//    @joincolumn(name = "patient_id")
//    private patient patient;
//    
//
//    @onetoone(mappedby = "appointment", cascade = cascadetype.all)
//    private payment payment;
//
//    @onetoone(mappedby = "appointment", cascade = cascadetype.all)
//    private prescription prescription;

}