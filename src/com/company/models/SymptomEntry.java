package com.company.models;

import java.time.LocalDate;

public class SymptomEntry {
    private int id;
    private int patientId;
    private String symptom;
    private LocalDate entryDate;

    public SymptomEntry(int patientId, String symptom) {
        if(symptom == null || symptom.isBlank()) throw new RuntimeException("Symptom cannot be empty");
        this.patientId = patientId;
        this.symptom = symptom;
        this.entryDate = LocalDate.now();
    }

    public SymptomEntry(int patientId, String symptom, LocalDate entryDate) {
        this.patientId = patientId;
        this.symptom = symptom;
        this.entryDate = entryDate;
    }

    public int getPatientId() { return patientId; }
    public String getSymptom() { return symptom; }
    public LocalDate getEntryDate() { return entryDate; }
}
