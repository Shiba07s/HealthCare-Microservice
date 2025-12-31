package com.patient_service.query.repository;

import com.patient_service.command.entities.Patient;
import com.patient_service.query.entities.PatientView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientQueryRepository extends JpaRepository<PatientView, Integer> {
}
