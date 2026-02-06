package com.company.controllers;

import com.company.models.Appointment;
import com.company.repositories.IAppointmentRepository;

import java.time.LocalDate;
import java.util.List;

public class AppointmentController {

    private final IAppointmentRepository repo;

    public AppointmentController(IAppointmentRepository repo){
        this.repo = repo;
    }

    public Appointment createAppointment(int patientId, int doctorId){
        Appointment appointment = new Appointment(patientId, doctorId, LocalDate.now());
        boolean success = repo.addAppointment(appointment);
        if(!success) throw new RuntimeException("failed to create appointment");
        System.out.println("Appointment created");
        return appointment;
    }

    public List<Appointment> getAppointmentsByPatient(int patientId){
        return repo.getAppointmentsByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId){
        return repo.getAppointmentsByDoctorId(doctorId);
    }

    public boolean closeAppointment(int doctorId, int patientId, LocalDate date) {
        return repo.closeAppointment(doctorId, patientId, date);
    }
}
