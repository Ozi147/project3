package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    public int patientId;
    public List<SymptomEntry> symptoms = new ArrayList<>();

    public MedicalRecord(int patientId) {
        this.patientId = patientId;
    }
}