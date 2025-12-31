package com.patient_service.command.service;

import com.patient_service.command.dtos.PatientCommandDto;

public interface PatientCommandService {

    PatientCommandDto createPatient(PatientCommandDto patientDto);
    PatientCommandDto updatePatient(int patientId, PatientCommandDto patientDto);
}
