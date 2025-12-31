package com.availabilityslot.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.availabilityslot.dtos.AvailabilityStatus;
import com.availabilityslot.dtos.SlotFrequency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "availability_slot")
public class AvailabilitySlot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	@Column(name = "slot_duration")
	private Integer slotDuration;

	@Enumerated(EnumType.STRING)
	private AvailabilityStatus status;

	@Enumerated(EnumType.STRING)
	private SlotFrequency frequency;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
	

//    @ManyToOne
//    @JoinColumn(name = "doctor_id")
//    private Doctors doctors;
}