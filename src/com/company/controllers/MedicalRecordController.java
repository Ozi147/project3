package com.company.controllers;

import com.company.models.MedicalRecord;
import com.company.repositories.IMedicalRecordRepository;

import java.util.List;

public class MedicalRecordController {

    private final IMedicalRecordRepository repo;

    public MedicalRecordController(IMedicalRecordRepository repo) {
        this.repo = repo;
    }

    // теперь show принимает имя пациента
    public void show(String patientName) {
        List<MedicalRecord> records = repo.getMedicalRecordsByPatientName(patientName);

        if (records.isEmpty()) {
            System.out.println("No medical records found for " + patientName);
            return;
        }

        System.out.println("Medical history for " + patientName + ":");
        records.forEach(rec -> System.out.println(
                "Patient: " + rec.getPatientName()
                        + " | Symptom: " + rec.getSymptom()
                        + " | Appointment Date: " + rec.getAppointmentDate()
                        + " | Doctor: " + rec.getDoctorName()
                        + " | Specialization: " + rec.getSpecialization()
        ));
    }
}
