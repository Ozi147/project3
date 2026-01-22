package com.company.controllers;

import com.company.models.Patient;
import com.company.repositories.IPatientRepository;

public class PatientController {

    private final IPatientRepository repo;
    private int nextId = 1;

    public PatientController(IPatientRepository repo) {
        this.repo = repo;
    }

    // старый метод: просто регистрирует без возврата
    public void register(String name, int age, String gender) {
        Patient patient = new Patient(nextId++, name, age, gender);
        repo.addPatient(patient);
        System.out.println("Patient registered: " + patient.getName());
    }

    // новый метод: регистрирует и возвращает объект Patient
    public Patient registerReturn(String name, int age, String gender) {
        Patient patient = new Patient(nextId++, name, age, gender);
        repo.addPatient(patient);
        System.out.println("Patient registered: " + patient.getName());
        return patient;
    }
}
