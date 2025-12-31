package com.appointment_service.client;

import com.appointment_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "Doctor-Service", configuration = FeignClientConfig.class,path = "/api/v1/doctor-profile/" )
public interface DoctorClient {
	
	@GetMapping("/{doctorsProfileId}")
	ResponseEntity<DoctorsDto> getDoctorsById(@PathVariable int doctorsProfileId);

}
