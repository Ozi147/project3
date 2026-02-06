package com.company.controllers;

import com.company.models.Patient;
import com.company.repositories.IPatientRepository;

import java.util.Optional;

public class PatientController {

    private final IPatientRepository repo;

    public PatientController(IPatientRepository repo) {
        this.repo = repo;
    }

    // регистрация нового пациента
    public Patient register(String name, int age, String gender) {
        if(name == null || name.isBlank()) throw new RuntimeException("invalid name");
        if(age <= 0) throw new RuntimeException("invalid age");
        Patient patient = new Patient(name, age, gender);
        boolean success = repo.addPatient(patient);
        if(!success) throw new RuntimeException("failed to register patient");
        System.out.println("Patient registered: " + patient.getName());
        return patient;
    }

    // получить пациента по id
    public Patient getById(int id){
        return Optional.ofNullable(repo.getPatientById(id))
                .orElseThrow(() -> new RuntimeException("patient not found"));
    }
}      
