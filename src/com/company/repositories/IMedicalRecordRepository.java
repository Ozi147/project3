package com.company.repositories;

import com.company.models.MedicalRecord;

import java.time.LocalDate;
import java.util.List;

public interface IMedicalRecordRepository {
    List<MedicalRecord> getMedicalRecordsByPatientName(String name);
    List<MedicalRecord> getMedicalRecordsByDoctorId(int doctorId);

    List<MedicalRecord> getMedicalRecordsByDoctorIdAndDate(int doctorId, LocalDate date);
}