package com.availabilityslot.services;

import java.util.List;

import com.availabilityslot.dtos.AvailabilitySlotDto;

public interface AvailabilitySlotService {

	AvailabilitySlotDto createNewAvailabilitySlot(AvailabilitySlotDto availabilitySlotDto);

	AvailabilitySlotDto getAvailabilitySlot(Integer availabilitySlotId);

	List<AvailabilitySlotDto> getAllAvailabilitySlot();

	AvailabilitySlotDto updateAvailabilitySlotDetails(Integer availabilitySlotId,
			AvailabilitySlotDto availabilitySlotDto);

	void deleteAvailabilitySlotDetails(Integer availabilitySlotId);

}