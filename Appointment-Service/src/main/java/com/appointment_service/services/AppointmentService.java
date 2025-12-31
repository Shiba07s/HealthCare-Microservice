package com.appointment_service.services;

import java.util.List;

import com.appointment_service.dtos.AppointmentDto;

public interface AppointmentService {
	AppointmentDto bookAppointment(AppointmentDto appointmentDto);

	AppointmentDto getAppointment(Integer appointmentId);

	List<AppointmentDto> getAllAppointments();

	AppointmentDto updateAppointment(Integer appointmentId, AppointmentDto appointmentDto);

	void deleteAppointmentData(Integer appointmentId);

}
