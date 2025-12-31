package com.doctor_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {
	
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
