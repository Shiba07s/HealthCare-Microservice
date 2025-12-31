package com.appointment_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appointment_service.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

}
