package com.doctor_service.query.service;

import com.doctor_service.command.dtos.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSearchCriteria {
    private String keyword;
    private String specialization;
    private String location;
    private String clinicName;
    private Integer minExperience;
    private BigDecimal maxFee;
    private DoctorStatus status;
}