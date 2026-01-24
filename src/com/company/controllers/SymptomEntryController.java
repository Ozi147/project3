package com.company.controllers;

import com.company.models.SymptomEntry;
import com.company.repositories.ISymptomEntryRepository;

public class SymptomEntryController {

    private final ISymptomEntryRepository repo;

    public SymptomEntryController(ISymptomEntryRepository repo) {
        this.repo = repo;
    }

    public void add(int patientId, String text) {
        repo.addSymptomEntry(new SymptomEntry(patientId, text));
        System.out.println("Symptom added");
    }
}
