package com.patient_service.command.repository;

import com.patient_service.command.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientCommandRepository extends JpaRepository<Patient, Integer> {
}
