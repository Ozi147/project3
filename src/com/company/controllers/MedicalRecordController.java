package com.company.controllers;

import com.company.models.MedicalRecord;
import com.company.repositories.IMedicalRecordRepository;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecordController {

    private final IMedicalRecordRepository repo;

    public MedicalRecordController(IMedicalRecordRepository repo){
        this.repo = repo;
    }

    public List<MedicalRecord> getByPatientName(String name){
        return repo.getMedicalRecordsByPatientName(name);
    }

    public List<MedicalRecord> getByDoctorId(int doctorId){
        return repo.getMedicalRecordsByDoctorId(doctorId);
    }

    public List<MedicalRecord> getByDoctorIdAndDate(int doctorId, LocalDate date) {
        return repo.getMedicalRecordsByDoctorIdAndDate(doctorId, date);
    }
}