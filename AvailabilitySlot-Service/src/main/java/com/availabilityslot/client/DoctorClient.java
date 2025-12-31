package com.availabilityslot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "Doctor-Service", path = "/api/v1/doctor-profile/")
public interface DoctorClient {
	
	@GetMapping("/{doctorsProfileId}")
	ResponseEntity<DoctorsDto> getDoctorsById(@PathVariable int doctorsProfileId);

}
