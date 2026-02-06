package com.company.controllers;

import com.company.models.SymptomEntry;
import com.company.repositories.ISymptomEntryRepository;

import java.util.List;

public class SymptomEntryController {

    private final ISymptomEntryRepository repo;

    public SymptomEntryController(ISymptomEntryRepository repo){
        this.repo = repo;
    }

    public SymptomEntry addSymptom(int patientId, String symptom){
        if(symptom == null || symptom.isBlank()) throw new RuntimeException("invalid symptom");
        SymptomEntry entry = new SymptomEntry(patientId, symptom);
        boolean success = repo.addSymptomEntry(entry);
        if(!success) throw new RuntimeException("failed to add symptom");
        return entry;
    }

    public List<SymptomEntry> getSymptomsByPatient(int patientId){
        return repo.getSymptomsByPatientId(patientId);
    }
}
