package com.company.controllers;

import com.company.repositories.SymptomEntryRepository;

public class SymptomEntryController {

    private final SymptomEntryRepository symptomEntryRepository;

    public SymptomEntryController(SymptomEntryRepository symptomEntryRepository) {
        this.symptomEntryRepository = symptomEntryRepository;
    }

    public void addSymptom(int appointmentId, String description) {
        symptomEntryRepository.create(appointmentId, description);
        System.out.println("Symptom added");
    }
}
