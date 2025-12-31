package com.doctor_service.command.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.doctor_service.command.entities.Doctors;

//public interface DoctorRepository extends JpaRepository<Doctors, Integer> {
public interface DoctorsCommandRepository extends JpaRepository<Doctors, Integer>, JpaSpecificationExecutor<Doctors> {

}
