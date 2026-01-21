package com.company.controllers;

import com.company.models.Appointment;
import com.company.repositories.IAppointmentRepository;

import java.time.LocalDate;

public class AppointmentController {

    private final IAppointmentRepository repo;

    public AppointmentController(IAppointmentRepository repo) {
        this.repo = repo;
    }

    public void make(int patientId, int doctorId) {
        repo.addAppointment(new Appointment(patientId, doctorId, LocalDate.now()));
        System.out.println("Appointment created");
    }
}
