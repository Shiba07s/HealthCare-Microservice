package com.patient_service.query.dtos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "query.datasource")
public class QueryDatabasePayload {
    private String url;
    private String username;
    private String password;
 }