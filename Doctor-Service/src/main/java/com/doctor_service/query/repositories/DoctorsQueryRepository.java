package com.doctor_service.query.repositories;

import com.doctor_service.query.entities.DoctorsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//public interface DoctorRepository extends JpaRepository<Doctors, Integer> {
public interface DoctorsQueryRepository extends JpaRepository<DoctorsView, Integer>, JpaSpecificationExecutor<DoctorsView> {

}
