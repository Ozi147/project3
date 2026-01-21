package com.company.controllers;

import com.company.models.Patient;
import com.company.repositories.IPatientRepository;

public class PatientController {

    private final IPatientRepository repo;

    public PatientController(IPatientRepository repo) {
        this.repo = repo;
    }

    public void register(String name, int age, String gender) {
        repo.addPatient(new Patient(0, name, age, gender));
        System.out.println("Patient registered");
    }
}
