package com.company.controllers;

import com.company.models.MedicalRecord;
import com.company.repositories.IMedicalRecordRepository;

public class MedicalRecordController {

    private final IMedicalRecordRepository repo;

    public MedicalRecordController(IMedicalRecordRepository repo) {
        this.repo = repo;
    }

    public void show(int patientId) {
        MedicalRecord record = repo.getByPatientId(patientId);
        System.out.println("Medical history:");
        record.symptoms.forEach(System.out::println);
    }
}
