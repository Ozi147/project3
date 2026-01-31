package com.company.repositories;

import com.company.models.MedicalRecord;
import java.util.List;

public interface IMedicalRecordRepository {
    MedicalRecord getByPatientId(int patientId);
    List<MedicalRecord> getMedicalRecordsByPatientName(String name);
}
