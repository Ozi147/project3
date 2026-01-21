package com.company.repositories;

import com.company.models.Appointment;
import java.util.List;

public interface IAppointmentRepository {
    boolean addAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByPatientId(int patientId);
}