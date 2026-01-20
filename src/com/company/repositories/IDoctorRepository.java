package com.company.repositories;

import com.company.models.Doctor;
import java.util.List;

public interface IDoctorRepository {

    boolean addDoctor(Doctor doctor);

    Doctor getDoctorById(int id);

    List<Doctor> getAllDoctors();
}
