package com.company.controllers;

import com.company.models.Doctor;
import com.company.repositories.IDoctorRepository;

public class DoctorController {

    private final IDoctorRepository repo;

    public DoctorController(IDoctorRepository repo) {
        this.repo = repo;
    }

    public void addDoctor(String name, String specialization) {
        Doctor doctor = new Doctor(name, specialization); // БЕЗ id
        repo.addDoctor(doctor);
        System.out.println("Doctor added");
    }
}
