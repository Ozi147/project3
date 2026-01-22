package com.company.models;

import java.time.LocalDate;

public class SymptomEntry {
    private int id;
    private int patientId;
    private String symptom;
    private LocalDate entryDate;

    // конструктор с patientId, симптом и дата
    public SymptomEntry(int patientId, String symptom, LocalDate entryDate) {
        this.patientId = patientId;
        this.symptom = symptom;
        this.entryDate = entryDate;
    }

    // конструктор с patientId и симптом (дата = сегодня)
    public SymptomEntry(int patientId, String symptom) {
        this.patientId = patientId;
        this.symptom = symptom;
        this.entryDate = LocalDate.now();
    }

    // **новый конструктор с описанием и датой**, чтобы работал код из репозитория
    public SymptomEntry(String symptom, LocalDate entryDate) {
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
