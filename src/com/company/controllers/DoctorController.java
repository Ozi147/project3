package com.company.controllers;

import com.company.models.Doctor;
import com.company.repositories.IDoctorRepository;

import java.util.List;

public class DoctorController {

    private final IDoctorRepository repo;

    public DoctorController(IDoctorRepository repo){
        this.repo = repo;
    }

    public Doctor addDoctor(String name, String specialization){
        if(name == null  name.isBlank()) throw new RuntimeException("invalid name");
        if(specialization == null  specialization.isBlank()) throw new RuntimeException("invalid specialization");
        Doctor doctor = new Doctor(name, specialization);
        Doctor created = repo.addDoctor(doctor);
        System.out.println("Doctor added: " + created.getName());
        return created;
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization){
        return repo.getDoctorsBySpecialization(specialization);
    }

    public Doctor getDoctorById(int id){
        return repo.getDoctorById(id);
    }
}
