package com.doctor_service.kafka;

import com.doctor_service.events.DoctorEvent;
import com.doctor_service.query.entities.DoctorsView;
import com.doctor_service.query.repositories.DoctorsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DoctorEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DoctorEventConsumer.class);

    private final DoctorsQueryRepository doctorsQueryRepository;
    private final ModelMapper modelMapper;


    @KafkaListener(topics = "doctors-events2", groupId = "doctor-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumePatientEvent(@Payload DoctorEvent event) {
        logger.info("Consumed event: {}", event);

        switch (event.getEventType()) {
            case CREATED:
                DoctorsView doctorsView = modelMapper.map(event, DoctorsView.class);
                doctorsQueryRepository.save(doctorsView);
                logger.info("Doctor view created in read model: {}", doctorsView);
                break;
            case UPDATED:
                DoctorsView doctorsViewForUpdate = modelMapper.map(event, DoctorsView.class);
                doctorsQueryRepository.save(doctorsViewForUpdate);
                logger.info("Doctor view updated in read model: {}", doctorsViewForUpdate);
                break;
            case DELETED:
                if (event.getId() != null) {
                        doctorsQueryRepository.deleteById(event.getId());
                        logger.info("Doctor view deleted from Query DB with id : {}", event.getId());
                }
                break;
            default:
                logger.warn("Unknown event type: {}", event.getEventType());
        }
    }


//    @KafkaListener(topics = "doctors-events", groupId = "doctor-service-group",
//            containerFactory = "kafkaListenerContainerFactory")
//    public void consumePatientEvent(@Payload DoctorEvent event) {
//        logger.info("Consumed event: {}", event);
//
//        switch (event.getEventType()) {
//            case CREATED:
//            case UPDATED:
//                DoctorsView doctorsView = modelMapper.map(event, DoctorsView.class);
//                doctorsQueryRepository.save(doctorsView);
//                logger.info("doctors view saved/updated in read model: {}", doctorsView);
//                break;
//            default:
//                logger.warn("Unknown event type: {}", event.getEventType());
//        }
//    }
}