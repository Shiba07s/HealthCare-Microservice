package com.doctor_service.query.service;

import com.doctor_service.command.dtos.DoctorStatus;
import com.doctor_service.command.entities.Doctors;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.query.dtos.DoctorsViewDto;
import com.doctor_service.query.entities.DoctorsView;
import com.doctor_service.query.repositories.DoctorsQueryRepository;
  import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class DoctorProfileViewServiceImpl implements DoctorProfileViewService{

    private static final Logger logger = LoggerFactory.getLogger(DoctorProfileViewServiceImpl.class);  // Initialize logger

    private static final String CACHE_NAME = "doctors";
    private final DoctorsQueryRepository doctorRepository;
    private final ModelMapper modelMapper;


    @Override
    @Cacheable(value = CACHE_NAME, key = "#doctorId", unless = "#result == null")
    public DoctorsViewDto getDoctorById(Integer doctorId) {
        logger.info("Cache miss or expired for doctorId: {}, executing method to fetch from DB", doctorId);

        try {
            logger.info("Fetching doctor profile with ID: {}", doctorId);

            DoctorsView doctorsDetails = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor is not present with this ID: " + doctorId));

            logger.info("Doctor profile fetched successfully for ID: {}", doctorId);
            return modelMapper.map(doctorsDetails, DoctorsViewDto.class);
        } catch (Exception e) {
            logger.error("Error fetching doctor profile with ID: {}: {}", doctorId, e.getMessage(), e);
            throw new RuntimeException("Error fetching doctor profile", e);
        }    }


    @Override
    @Cacheable(value = CACHE_NAME, key = "'allDoctors'", unless = "#result == null || #result.isEmpty()")
    public List<DoctorsViewDto> getAllDoctors() {
        logger.info("Cache miss or expired for key 'allDoctors', executing method to fetch from DB");

        try {
            logger.info("Fetching list of all doctors.");
            List<DoctorsView> all = doctorRepository.findAll();
            logger.info("Fetched {} doctors.", all.size());
            return all.stream().map(doctors -> modelMapper.map(doctors, DoctorsViewDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all doctors: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching all doctors", e);
        }
    }

    @Override
    public List<DoctorsViewDto> getAllVerifiedDoctorsList() {
        try {
            logger.info("Fetching list of verified doctors.");
            List<DoctorsView> doctorsList = doctorRepository.findAll();
            List<DoctorsViewDto> verifiedDoctors = doctorsList.stream()
                    .filter(status -> status.getStatus().equals(DoctorStatus.VERIFIED))
                    .map(doctors -> modelMapper.map(doctors, DoctorsViewDto.class))
                    .collect(Collectors.toList());

            logger.info("Fetched {} verified doctors.", verifiedDoctors.size());
            return verifiedDoctors;
        } catch (Exception e) {
            logger.error("Error fetching verified doctors list: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching verified doctors list", e);
        }    }

    @Override
    public List<DoctorsViewDto> getAllUnVerifiedDoctorsList() {
        try {
            logger.info("Fetching list of unverified doctors.");
            List<DoctorsView> doctorsList = doctorRepository.findAll();
            List<DoctorsViewDto> unVerifiedDoctors = doctorsList.stream()
                    .filter(status -> status.getStatus().equals(DoctorStatus.UNVERIFIED))
                    .map(doctors -> modelMapper.map(doctors, DoctorsViewDto.class))
                    .collect(Collectors.toList());

            logger.info("Fetched {} unverified doctors.", unVerifiedDoctors.size());
            return unVerifiedDoctors;
        } catch (Exception e) {
            logger.error("Error fetching unverified doctors list: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching unverified doctors list", e);
        }    }

    @Override
    public List<DoctorsViewDto> searchDoctors(DoctorSearchCriteria criteria) {
        try {
            logger.info("Searching doctors with criteria: {}", criteria);
            Specification<Doctors> spec = DoctorSpecification.buildSpecification(criteria);
            List<DoctorsView> doctors = doctorRepository.findAll((Sort) spec);
            logger.info("Found {} doctors matching the search criteria.", doctors.size());
            return doctors.stream().map(doctor -> modelMapper.map(doctor, DoctorsViewDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching doctors with criteria: {}: {}", criteria, e.getMessage(), e);
            throw new RuntimeException("Error searching doctors", e);
        }
    }
}
