package com.patient_service.query.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class PatientCacheEvictService {

    private static final Logger logger = LoggerFactory.getLogger(PatientCacheEvictService.class);

    private static final String CACHE_NAME = "patients";

    @CacheEvict(value = CACHE_NAME, key = "#patientId")
    public void evictPatientCache(Integer patientId) {
        logger.info("Evicting cache for patient ID: {}", patientId);
    }

    @CacheEvict(value = CACHE_NAME, key = "'allPatients'")
    public void evictAllPatientsCache() {
        logger.info("Evicting cache for all patients");
    }
}