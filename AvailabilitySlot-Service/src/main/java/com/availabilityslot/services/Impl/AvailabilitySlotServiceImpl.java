package com.availabilityslot.services.Impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.availabilityslot.client.DoctorClient;
import com.availabilityslot.client.DoctorStatus;
import com.availabilityslot.client.DoctorsDto;
import com.availabilityslot.dtos.AvailabilitySlotDto;
import com.availabilityslot.dtos.AvailabilityStatus;
import com.availabilityslot.dtos.SlotFrequency;
import com.availabilityslot.entities.AvailabilitySlot;
import com.availabilityslot.exception.ResourceNotFoundException;
import com.availabilityslot.repositories.AvailabilitySlotRepository;
import com.availabilityslot.services.AvailabilitySlotService;
	
import lombok.RequiredArgsConstructor;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AvailabilitySlotServiceImpl implements AvailabilitySlotService {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilitySlotServiceImpl.class);

    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final ModelMapper modelMapper;
    private final DoctorClient doctorClient;

    @Override
    public AvailabilitySlotDto createNewAvailabilitySlot(AvailabilitySlotDto availabilitySlotDto) {
        logger.info("Starting to create a new availability slot: {}", availabilitySlotDto);

        ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlotDto.getDoctorId());
        DoctorsDto doctorDto = doctorResponse.getBody();

        if (doctorResponse == null || doctorDto == null) {
            logger.error("Doctor not found for ID: {}", availabilitySlotDto.getDoctorId());
            throw new ResourceNotFoundException("Doctor not found");
        } else if (!doctorDto.getStatus().equals(DoctorStatus.VERIFIED)) {
            logger.error("Doctor with ID {} is not verified.", availabilitySlotDto.getDoctorId());
            throw new ResourceNotFoundException("Doctor is not verified. Cannot book an appointment.");
        }

        logger.info("Doctor found: {}", doctorDto);

        // Extract parameters from the DTO
        LocalDateTime startTime = availabilitySlotDto.getStartTime();
        LocalDateTime endTime = availabilitySlotDto.getEndTime();
        SlotFrequency frequency = availabilitySlotDto.getFrequency();
        Integer slotDuration = availabilitySlotDto.getSlotDuration();

        List<AvailabilitySlot> slots = new ArrayList<>();
        long totalMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
        int slotsPerDay = (int) (totalMinutes / slotDuration);

        LocalDateTime currentDate = startTime.toLocalDate().atStartOfDay();
        LocalDateTime lastDate;

        switch (frequency) {
            case DAILY:
                lastDate = currentDate;
                break;
            case WEEKLY:
                lastDate = currentDate.plusDays(6);
                break;
            case MONTHLY:
                lastDate = currentDate.plusMonths(1).plusMonths(1).minusMinutes(1);
                break;
            default:
                lastDate = currentDate;
        }

        // Generate slots for each day in the range
        logger.info("Generating availability slots from {} to {}", currentDate, lastDate);
        while (!currentDate.isAfter(lastDate)) {
            LocalDateTime dailyStartTime = currentDate.withHour(startTime.getHour()).withMinute(startTime.getMinute());

            for (int i = 0; i < slotsPerDay; i++) {
                LocalDateTime slotStartTime = dailyStartTime.plusMinutes(i * slotDuration);
                LocalDateTime slotEndTime = slotStartTime.plusMinutes(slotDuration);

                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setDoctorId(doctorDto.getId());
                slot.setDate(currentDate);
                slot.setStartTime(slotStartTime);
                slot.setEndTime(slotEndTime);
                slot.setSlotDuration(slotDuration);
                slot.setFrequency(frequency);
                slot.setStatus(AvailabilityStatus.AVAILABLE);
                slots.add(slot);
            }

            currentDate = currentDate.plusDays(1);
        }

        // Save all slots as a single batch
        List<AvailabilitySlot> savedSlots = availabilitySlotRepository.saveAll(slots);
        AvailabilitySlotDto responseDto = modelMapper.map(savedSlots.get(0), AvailabilitySlotDto.class);
        responseDto.setDoctors(doctorDto);

        logger.info("Successfully created new availability slot for doctor ID: {}", doctorDto.getId());
        return responseDto;
    }

    @Override
    public AvailabilitySlotDto getAvailabilitySlot(Integer availabilitySlotId) {
        logger.info("Fetching availability slot with ID: {}", availabilitySlotId);

        AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
                .orElseThrow(() -> {
                    logger.error("Availability slot with ID {} not found.", availabilitySlotId);
                    return new ResourceNotFoundException("AvailabilitySlot not present with this id: " + availabilitySlotId);
                });

        ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlot.getDoctorId());
        DoctorsDto doctorDto = doctorResponse.getBody();
        if (doctorResponse == null || doctorDto == null) {
            logger.error("Doctor not found for availability slot ID: {}", availabilitySlotId);
            throw new ResourceNotFoundException("Doctor not found");
        }

        AvailabilitySlotDto responseDto = modelMapper.map(availabilitySlot, AvailabilitySlotDto.class);
        responseDto.setDoctors(doctorDto);

        logger.info("Successfully fetched availability slot with ID: {}", availabilitySlotId);
        return responseDto;
    }

    @Override
    public List<AvailabilitySlotDto> getAllAvailabilitySlot() {
        logger.info("Fetching all availability slots");

        List<AvailabilitySlot> all = availabilitySlotRepository.findAll();
        List<AvailabilitySlotDto> availabilitySlotDtos = all.stream().map(availabilitySlot -> {
            AvailabilitySlotDto map = modelMapper.map(availabilitySlot, AvailabilitySlotDto.class);

            ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(map.getDoctorId());
            DoctorsDto doctorDto = doctorResponse.getBody();
            if (doctorResponse == null || doctorDto == null) {
                logger.error("Doctor not found for availability slot ID: {}", map.getId());
                throw new ResourceNotFoundException("Doctor not found");
            }
            map.setDoctors(doctorDto);
            return map;
        }).collect(Collectors.toList());

        logger.info("Successfully fetched {} availability slots", availabilitySlotDtos.size());
        return availabilitySlotDtos;
    }

    @Override
    public AvailabilitySlotDto updateAvailabilitySlotDetails(Integer availabilitySlotId,
                                                              AvailabilitySlotDto availabilitySlotDto) {
        logger.info("Updating availability slot with ID: {}", availabilitySlotId);

        AvailabilitySlot existingAvailabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
                .orElseThrow(() -> {
                    logger.error("Availability slot with ID {} not found.", availabilitySlotId);
                    return new ResourceNotFoundException("AvailabilitySlot not present with this id: " + availabilitySlotId);
                });

        existingAvailabilitySlot.setDate(availabilitySlotDto.getDate());
        existingAvailabilitySlot.setStartTime(availabilitySlotDto.getStartTime());
        existingAvailabilitySlot.setEndTime(availabilitySlotDto.getEndTime());
        existingAvailabilitySlot.setSlotDuration(availabilitySlotDto.getSlotDuration());
        existingAvailabilitySlot.setFrequency(availabilitySlotDto.getFrequency());
        existingAvailabilitySlot.setStatus(availabilitySlotDto.getStatus());
        existingAvailabilitySlot.setUpdatedAt(LocalDateTime.now());
        existingAvailabilitySlot.setDoctorId(availabilitySlotDto.getDoctorId());

        AvailabilitySlot saved = availabilitySlotRepository.save(existingAvailabilitySlot);

        ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlotDto.getDoctorId());
        DoctorsDto doctorDto = doctorResponse.getBody();
        if (doctorResponse == null || doctorDto == null) {
            logger.error("Doctor not found for ID: {}", availabilitySlotDto.getDoctorId());
            throw new ResourceNotFoundException("Doctor not found");
        }

        AvailabilitySlotDto responseDto = modelMapper.map(saved, AvailabilitySlotDto.class);
        responseDto.setDoctors(doctorDto);

        logger.info("Successfully updated availability slot with ID: {}", availabilitySlotId);
        return responseDto;
    }

    @Override
    public void deleteAvailabilitySlotDetails(Integer availabilitySlotId) {
        logger.info("Deleting availability slot with ID: {}", availabilitySlotId);

        AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
                .orElseThrow(() -> {
                    logger.error("Availability slot with ID {} not found.", availabilitySlotId);
                    return new ResourceNotFoundException("AvailabilitySlot not present with this id: " + availabilitySlotId);
                });

        availabilitySlotRepository.delete(availabilitySlot);
        logger.info("Successfully deleted availability slot with ID: {}", availabilitySlotId);
    }
}


//@RequiredArgsConstructor
//@Service
//public class AvailabilitySlotServiceImpl implements AvailabilitySlotService {
//
//	private final AvailabilitySlotRepository availabilitySlotRepository;
//	private final ModelMapper modelMapper;
//	private final DoctorClient doctorClient;
//
//	@Override
//	public AvailabilitySlotDto createNewAvailabilitySlot(AvailabilitySlotDto availabilitySlotDto) {
//
//		ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlotDto.getDoctorId());
//		DoctorsDto doctorDto = doctorResponse.getBody();
//		if (doctorResponse == null || doctorResponse.getBody() == null) {
//			throw new ResourceNotFoundException("doctor not Found");
//		} else if (!doctorDto.getStatus().equals(DoctorStatus.VERIFIED)) {
//			throw new ResourceNotFoundException("Doctor is not verified. Cannot book an appointment.");
//		}
//	    // Extract parameters from the DTO
//	    LocalDateTime startTime = availabilitySlotDto.getStartTime();
//	    LocalDateTime endTime = availabilitySlotDto.getEndTime();
//	    SlotFrequency frequency = availabilitySlotDto.getFrequency();
//	    Integer slotDuration = availabilitySlotDto.getSlotDuration();
//
//		List<AvailabilitySlot> slots = new ArrayList<>();
//		long totalMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
//		int slotsPerDay = (int) (totalMinutes / slotDuration);
//
//		LocalDateTime currentDate = startTime.toLocalDate().atStartOfDay();
//		LocalDateTime lastDate;
//
//		switch (frequency) {
//		case DAILY: 
//			lastDate = currentDate;
//			break;
//		case WEEKLY:
//			lastDate=currentDate.plusDays(6);
//			break;
//		case MONTHLY:
//			lastDate=currentDate.plusMonths(1).plusMonths(1).minusMinutes(1);
//			break;
//		default:
//			lastDate=currentDate;
//		}
//		
//		// Generate slots for each day in the range
//		while (!currentDate.isAfter(lastDate)) {
//			// Generate slots for this day
//			LocalDateTime dailyStartTime = currentDate.withHour(startTime.getHour()).withMinute(startTime.getMinute());
//
//			for (int i = 0; i < slotsPerDay; i++) {
//				LocalDateTime slotStartTime = dailyStartTime.plusMinutes(i * slotDuration);
//				LocalDateTime slotEndTime = slotStartTime.plusMinutes(slotDuration);
//
//	            AvailabilitySlot slot = new AvailabilitySlot();
//	            slot.setDoctorId(doctorDto.getId()); // Assuming you have a method to convert DoctorsDto to Doctor entity
//	            slot.setDate(currentDate);
//	            slot.setStartTime(slotStartTime);
//	            slot.setEndTime(slotEndTime);
//	            slot.setSlotDuration(slotDuration);
//	            slot.setFrequency(frequency);
//	            slot.setStatus(AvailabilityStatus.AVAILABLE);
//	            slots.add(slot);
//	        }
//
//	        // Move to next day
//	        currentDate = currentDate.plusDays(1);
//	    }
//
//	    // Save all slots as a single batch
//	    List<AvailabilitySlot> savedSlots = availabilitySlotRepository.saveAll(slots);
//	    AvailabilitySlotDto responseDto = modelMapper.map(savedSlots.get(0), AvailabilitySlotDto.class);
//
//	    responseDto.setDoctors(doctorDto);
//	    return responseDto;
//	}
//
// 
//	@Override
//	public AvailabilitySlotDto getAvailabilitySlot(Integer availabilitySlotId) {
//		AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
//				.orElseThrow(() -> new ResourceNotFoundException(
//						"AvailibilitySlot is not present with this id: " + availabilitySlotId));
//		ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlot.getDoctorId());
//		DoctorsDto doctorDto = doctorResponse.getBody();
//		if (doctorResponse == null || doctorResponse.getBody() == null) {
//			throw new ResourceNotFoundException("doctor not Found");
//		}
//		AvailabilitySlotDto responseDto = modelMapper.map(availabilitySlot, AvailabilitySlotDto.class);
//		responseDto.setDoctors(doctorDto);
//		return responseDto;
//	}
//
//	@Override
//	public List<AvailabilitySlotDto> getAllAvailabilitySlot() {
//		List<AvailabilitySlot> all = availabilitySlotRepository.findAll();
//		return all.stream().map(availabilty->{
//			AvailabilitySlotDto map = modelMapper.map(availabilty, AvailabilitySlotDto.class);
//			
//			ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(map.getDoctorId());
//			DoctorsDto doctorDto = doctorResponse.getBody();
//			if (doctorResponse == null || doctorResponse.getBody() == null) {
//				throw new ResourceNotFoundException("doctor not Found");
//			}
//			map.setDoctors(doctorDto);
//			return map;
//		}).collect(Collectors.toList());
// 
//	}
//
//	@Override
//	public AvailabilitySlotDto updateAvailabilitySlotDetails(Integer availabilitySlotId,
//			AvailabilitySlotDto availabilitySlotDto) {
//		AvailabilitySlot existingAvailabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
//				.orElseThrow(() -> new ResourceNotFoundException(
//						"AvailibilitySlot is not present with this id: " + availabilitySlotId));
//
//		existingAvailabilitySlot.setDate(availabilitySlotDto.getDate());
//		existingAvailabilitySlot.setStartTime(availabilitySlotDto.getStartTime());
//		existingAvailabilitySlot.setEndTime(availabilitySlotDto.getEndTime());
//		existingAvailabilitySlot.setSlotDuration(availabilitySlotDto.getSlotDuration());
//		existingAvailabilitySlot.setFrequency(availabilitySlotDto.getFrequency());
//		existingAvailabilitySlot.setStatus(availabilitySlotDto.getStatus());
//		existingAvailabilitySlot.setUpdatedAt(LocalDateTime.now());
//		existingAvailabilitySlot.setDoctorId(availabilitySlotDto.getDoctorId());
//
//		AvailabilitySlot save = availabilitySlotRepository.save(existingAvailabilitySlot);
//
//		ResponseEntity<DoctorsDto> doctorResponse = doctorClient.getDoctorsById(availabilitySlotDto.getDoctorId());
//		DoctorsDto doctorDto = doctorResponse.getBody();
//		if (doctorResponse == null || doctorResponse.getBody() == null) {
//			throw new ResourceNotFoundException("doctor not Found");
//		}
//		AvailabilitySlotDto responseDto = modelMapper.map(save, AvailabilitySlotDto.class);
//		responseDto.setDoctors(doctorDto);
//		return responseDto;
//	}
//
//	@Override
//	public void deleteAvailabilitySlotDetails(Integer availabilitySlotId) {
//		AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(availabilitySlotId)
//				.orElseThrow(() -> new ResourceNotFoundException(
//						"AvailibilitySlot is not present with this id: " + availabilitySlotId));
//		availabilitySlotRepository.delete(availabilitySlot);
//	}
//
//}
