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

    @Override
    public String toString() {
        return patientName + " | " + symptom + " | " + appointmentDate + " | " + doctorName + " | " + specialization;
    }
}
