package com.patient_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final LettuceConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisHealthIndicator(LettuceConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public Health health() {
        try {
            RedisConnection connection = redisConnectionFactory.getConnection();
            connection.ping();
            connection.close();
            return Health.up().withDetail("status", "Redis is running").build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Redis is down")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}