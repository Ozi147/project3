package com.company.models;

import java.time.LocalDate;

public class MedicalRecord {
    private int patientId;
    private String patientName;
    private String symptom;
    private LocalDate appointmentDate;
    private String doctorName;
    private String specialization;

    public MedicalRecord(int patientId, String patientName, String symptom,
                         LocalDate appointmentDate, String doctorName, String specialization) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.symptom = symptom;
        this.appointmentDate = appointmentDate;
        this.doctorName = doctorName;
        this.specialization = specialization;
    }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getSymptom() { return symptom; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialization() { return specialization; }
}
