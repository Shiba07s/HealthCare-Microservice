package com.availabilityslot.dtos;

import java.time.LocalDateTime;

import com.availabilityslot.client.DoctorsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilitySlotDto {

	private Integer id;
	private Integer doctorId;
	private DoctorsDto doctors;
	private LocalDateTime date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Integer slotDuration;
	private AvailabilityStatus status;
	private SlotFrequency frequency;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
//	private DoctorsDtoResponse doctors;

}
