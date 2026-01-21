package com.company.controllers;

import com.company.models.Doctor;
import com.company.repositories.IDoctorRepository;

public class DoctorController {

    private final IDoctorRepository repo;

    public DoctorController(IDoctorRepository repo) {
        this.repo = repo;
    }

    public void addDoctor(String name, String spec) {
        repo.addDoctor(new Doctor(0, name, spec));
        System.out.println("Doctor added");
    }
}
