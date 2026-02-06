package com.company.controllers;

import com.company.models.MedicalRecord;
import com.company.repositories.IMedicalRecordRepository;

import java.util.List;

public class MedicalRecordController {

    private final IMedicalRecordRepository repo;

    public MedicalRecordController(IMedicalRecordRepository repo){
        this.repo = repo;
    }

    public List<MedicalRecord> getByPatientName(String name){
        List<MedicalRecord> records = repo.getMedicalRecordsByPatientName(name);
        if(records.isEmpty()) throw new RuntimeException("no records found for patient: " + name);
        return records;
    }

    public List<MedicalRecord> getByDoctorId(int doctorId){
        return repo.getMedicalRecordsByDoctorId(doctorId);
    }
}