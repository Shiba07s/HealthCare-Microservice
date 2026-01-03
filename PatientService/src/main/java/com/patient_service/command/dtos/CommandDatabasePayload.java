package com.patient_service.command.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "command.datasource")
public class CommandDatabasePayload  {
    private String url;
    private String username;
    private String password;
}
