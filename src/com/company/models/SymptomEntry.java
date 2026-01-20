package com.company.models;

import java.time.LocalDate;

public class SymptomEntry {
    private int id;
    private int patientId;
    private String symptom;
    private LocalDate entryDate;

    public SymptomEntry(int patientId, String symptom, LocalDate entryDate) {
        this.patientId = patientId;
        this.symptom = symptom;
        this.entryDate = entryDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
}