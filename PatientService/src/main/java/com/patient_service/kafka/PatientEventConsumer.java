package com.patient_service.kafka;

import com.patient_service.events.PatientEvent;
import com.patient_service.query.dtos.PatientQueryDto;
import com.patient_service.query.entities.PatientView;
import com.patient_service.query.repository.PatientQueryRepository;
import com.patient_service.query.service.PatientCacheEvictService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;


// ❌ DON'T call @CacheEvict methods from same class — Spring AOP won't intercept
// ✅ INSTEAD: use another bean to invoke @CacheEvict methods


@RequiredArgsConstructor
@Component
public class PatientEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PatientEventConsumer.class);

    private final PatientQueryRepository patientQueryRepository;
    private final ModelMapper modelMapper;
    private static final String CACHE_NAME = "patients";
    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PatientCacheEvictService patientCacheEvictService;

    @KafkaListener(topics = "patient-events4", groupId = "patient-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumePatientEvent(@Payload PatientEvent event) {
        logger.info("Consumed event: {}", event);

        switch (event.getEventType()) {
            case CREATED:
            case UPDATED:
                PatientView patientView = modelMapper.map(event, PatientView.class);
                patientQueryRepository.save(patientView);
                logger.info("Patient view saved/updated in read model: {}", patientView);

                // ✅ Call eviction methods from a different bean so @CacheEvict works
                patientCacheEvictService.evictPatientCache(event.getId());
                patientCacheEvictService.evictAllPatientsCache();

                break;

            default:
                logger.warn("Unknown event type: {}", event.getEventType());
        }
    }



//    @KafkaListener(topics = "patient-events4", groupId = "patient-service-group",
//            containerFactory = "kafkaListenerContainerFactory")
//    @Transactional
//    public void consumePatientEvent(@Payload PatientEvent event) {
//        logger.info("Consumed event: {}", event);
//
//        try {
//            switch (event.getEventType()) {
//                case CREATED:
//                case UPDATED:
//                    PatientView patientView = modelMapper.map(event, PatientView.class);
//                    patientQueryRepository.save(patientView);
//                    logger.info("Patient view saved/updated in read model: {}", patientView);
//
//                    // Evict any existing cache entries
//                    evictCacheEntries(event.getId());
//
//                    // Directly store the updated data in Redis
//                    storePatientInRedis(patientView);
//                    break;
//
//                case DELETED:
//                    patientQueryRepository.deleteById(event.getId());
//                    logger.info("Patient with ID {} deleted from read model", event.getId());
//
//                    // Evict cache entries for deleted patient
//                    evictCacheEntries(event.getId());
//                    break;
//
//                default:
//                    logger.warn("Unknown event type: {}", event.getEventType());
//            }
//        } catch (Exception e) {
//            logger.error("Error processing Kafka event: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    private void evictCacheEntries(Integer patientId) {
//        try {
//            // Clear specific patient cache using both approaches
//            cacheManager.getCache(CACHE_NAME).evict(patientId);
//            redisTemplate.delete(CACHE_NAME + "::" + patientId);
//            logger.info("Evicted cache for patient ID: {}", patientId);
//
//            // Clear all patients cache using both approaches
//            cacheManager.getCache(CACHE_NAME).evict("allPatients");
//            redisTemplate.delete(CACHE_NAME + "::allPatients");
//            logger.info("Evicted cache for all patients");
//        } catch (Exception e) {
//            logger.error("Error evicting cache: {}", e.getMessage(), e);
//        }
//    }
//
//    private void storePatientInRedis(PatientView patientView) {
//        try {
//            // Convert to DTO for consistent serialization
//            PatientQueryDto patientDto = modelMapper.map(patientView, PatientQueryDto.class);
//
//            // Store individual patient with key pattern that matches @Cacheable
//            String patientKey = CACHE_NAME + "::" + patientView.getId();
//            redisTemplate.opsForValue().set(patientKey, patientDto);
//            logger.info("Stored patient data directly in Redis with key: {}", patientKey);
//
//            // Also store as plain key for easier viewing in Redis Commander
//            String plainKey = "patient:" + patientView.getId();
//            redisTemplate.opsForValue().set(plainKey, patientDto);
//            logger.info("Stored patient data with plain key for visibility: {}", plainKey);
//
//            // Update the allPatients collection too
//            // First get existing collection if available
//            updateAllPatientsCollection(patientView.getId(), patientDto);
//        } catch (Exception e) {
//            logger.error("Error storing patient in Redis: {}", e.getMessage(), e);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void updateAllPatientsCollection(Integer patientId, PatientQueryDto updatedPatient) {
//        try {
//            String allPatientsKey = CACHE_NAME + "::allPatients";
//            String plainAllPatientsKey = "allPatients";
//
//            // Try to update the existing collection if it exists
//            List<PatientQueryDto> allPatients = (List<PatientQueryDto>) redisTemplate.opsForValue().get(allPatientsKey);
//
//            if (allPatients != null) {
//                // Update or add this patient
//                boolean found = false;
//                for (int i = 0; i < allPatients.size(); i++) {
//                    if (allPatients.get(i).getId().equals(patientId)) {
//                        allPatients.set(i, updatedPatient);
//                        found = true;
//                        break;
//                    }
//                }
//
//                if (!found) {
//                    allPatients.add(updatedPatient);
//                }
//
//                // Store updated collection
//                redisTemplate.opsForValue().set(allPatientsKey, allPatients);
//                redisTemplate.opsForValue().set(plainAllPatientsKey, allPatients);
//                logger.info("Updated allPatients collection in Redis");
//            } else {
//                // Collection doesn't exist yet, no need to update it
//                logger.info("allPatients collection not found in Redis, skipping update");
//            }
//        } catch (Exception e) {
//            logger.error("Error updating allPatients collection: {}", e.getMessage(), e);
//        }
//    }



}