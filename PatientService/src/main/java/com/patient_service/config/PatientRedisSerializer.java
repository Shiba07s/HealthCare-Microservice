package com.patient_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.patient_service.query.dtos.PatientQueryDto;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class PatientRedisSerializer implements RedisSerializer<PatientQueryDto> {

    private final ObjectMapper objectMapper;

    public PatientRedisSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public byte[] serialize(PatientQueryDto patientQueryDto) throws SerializationException {
        if (patientQueryDto == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(patientQueryDto);
        } catch (Exception e) {
            throw new SerializationException("Error serializing PatientQueryDto", e);
        }
    }

    @Override
    public PatientQueryDto deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, PatientQueryDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing PatientQueryDto", e);
        }
    }
}