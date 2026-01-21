package com.company.models;

import java.time.LocalDate;

public class Appointment {
    public int patientId;
    public int doctorId;
    public LocalDate date;

    public Appointment(int patientId, int doctorId, LocalDate date) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
    }
}
