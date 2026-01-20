package com.company.repositories;

import com.company.models.Patient;
import java.util.List;

public interface IPatientRepository {

    boolean addPatient(Patient patient);

    Patient getPatientById(int id);

    List<Patient> getAllPatients();
}
