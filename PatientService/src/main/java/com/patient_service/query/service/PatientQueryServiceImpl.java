package com.patient_service.query.service;

import com.patient_service.query.dtos.PatientQueryDto;
import com.patient_service.query.entities.PatientView;
import com.patient_service.query.repository.PatientQueryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientQueryServiceImpl implements PatientQueryService {

    private static final Logger logger = LoggerFactory.getLogger(PatientQueryServiceImpl.class);

    private static final String CACHE_NAME = "patients";

    private final PatientQueryRepository patientQueryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(value = CACHE_NAME, key = "#patientId", unless = "#result == null")
    public PatientQueryDto getPatient(int patientId) {
        logger.info("Fetching patient with ID: {} (cache miss)", patientId);

        PatientView patientView = patientQueryRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        logger.info("Fetched patient: {}", patientView);
        return modelMapper.map(patientView, PatientQueryDto.class);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'allPatients'", unless = "#result == null || #result.isEmpty()")
    public List<PatientQueryDto> getAllPatients() {
        logger.info("Fetching all patient details (cache miss)");

        List<PatientView> allPatientViews = patientQueryRepository.findAll();
        List<PatientQueryDto> patientDtos = allPatientViews.stream()
                .map(patientView -> modelMapper.map(patientView, PatientQueryDto.class))
                .collect(Collectors.toList());

        logger.info("Fetched {} patient(s)", patientDtos.size());
        return patientDtos;
    }
}
//    @Override
//    @Cacheable(value = CACHE_NAME, key = "'allPatients'")
//    public List<PatientQueryDto> getAllPatients() {
//        logger.info("Fetching all patient details");
//
//        List<PatientView> allPatientViews = patientQueryRepository.findAll();
//        List<PatientQueryDto> patientDtos = allPatientViews.stream()
//                .map(patientView -> modelMapper.map(patientView, PatientQueryDto.class))
//                .collect(Collectors.toList());
//
//        logger.info("Fetched {} patient(s)", patientDtos.size());
//        return patientDtos;
//    }
//}