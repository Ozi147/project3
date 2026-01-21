package com.company.repositories;

import com.company.models.MedicalRecord;

public interface IMedicalRecordRepository {
    MedicalRecord getByPatientId(int patientId);
}