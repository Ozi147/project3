package com.company.repositories;

import com.company.models.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentRepository {
    boolean addAppointment(Appointment appointment);

    List<Appointment> getAppointmentsByPatientId(int patientId);
    List<Appointment> getAppointmentsByDoctorId(int doctorId);

    boolean closeAppointment(int doctorId, int patientId, LocalDate date);
}