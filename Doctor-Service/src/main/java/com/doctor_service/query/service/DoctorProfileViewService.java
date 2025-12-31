package com.doctor_service.query.service;

import com.doctor_service.query.dtos.DoctorsViewDto;

import java.util.List;


public interface DoctorProfileViewService {


    DoctorsViewDto getDoctorById(Integer doctorId);

    List<DoctorsViewDto> getAllDoctors();

//	void deleteDoctorsProfile(Integer doctorId);

    List<DoctorsViewDto> getAllVerifiedDoctorsList();

    List<DoctorsViewDto> getAllUnVerifiedDoctorsList();

//	DoctorsViewDto verifyDoctorsProfile(Integer doctorId);

    List<DoctorsViewDto> searchDoctors(DoctorSearchCriteria criteria);

//	DoctorsViewDto createDoctor(DoctorsViewDto doctorDto);

//	DoctorsViewDto updateDoctor(Integer doctorId, DoctorsViewDto doctorDto);

}
