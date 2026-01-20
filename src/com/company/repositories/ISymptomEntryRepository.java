package com.company.repositories;

import com.company.models.SymptomEntry;
import java.util.List;

public interface ISymptomEntryRepository {

    boolean addSymptomEntry(SymptomEntry entry);

    List<SymptomEntry> getSymptomsByPatientId(int patientId);
}
