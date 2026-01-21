package com.company.controllers;

import com.company.repositories.IMedicalRecordRepository;

public class MedicalRecordController {

    private final IMedicalRecordRepository repo;

    public MedicalRecordController(IMedicalRecordRepository repo) {
        this.repo = repo;
    }

    public void show(int patientId) {
        System.out.println(repo.getByPatientId(patientId));
    }
}
