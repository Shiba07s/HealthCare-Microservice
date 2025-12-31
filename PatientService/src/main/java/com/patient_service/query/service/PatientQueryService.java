package com.patient_service.query.service;

import com.patient_service.command.dtos.PatientCommandDto;
import com.patient_service.query.dtos.PatientQueryDto;

import java.util.List;

public interface PatientQueryService {
    PatientQueryDto getPatient(int patientId);
    List<PatientQueryDto> getAllPatients();
}