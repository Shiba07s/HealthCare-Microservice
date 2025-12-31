package com.doctor_service.command.service;

import com.doctor_service.command.dtos.DoctorsCommandDto;

public interface DoctorProfileCommandService {
    DoctorsCommandDto createDoctor(DoctorsCommandDto doctorDto);

    DoctorsCommandDto updateDoctor(Integer doctorId, DoctorsCommandDto doctorDto);

    void deleteDoctorsProfile(Integer doctorId);

    DoctorsCommandDto verifyDoctorsProfile(Integer doctorId);
}
