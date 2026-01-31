package com.company.services;

import com.company.models.*;
import com.company.repositories.*;

import java.time.LocalDate;

public class MedicalService {

    private final IPatientRepository patientRepo;
    private final ISymptomEntryRepository symptomRepo;
    private final IDoctorRepository doctorRepo;
    private final IAppointmentRepository appointmentRepo;
    private final IMedicalRecordRepository medicalRecordRepo;
    private final ISymptomDoctorRepository symptomDoctorRepo;

    public MedicalService(
            IPatientRepository patientRepo,
            ISymptomEntryRepository symptomRepo,
            IDoctorRepository doctorRepo,
            IAppointmentRepository appointmentRepo,
            IMedicalRecordRepository medicalRecordRepo,
            ISymptomDoctorRepository symptomDoctorRepo
    ) {
        this.patientRepo = patientRepo;
        this.symptomRepo = symptomRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
        this.medicalRecordRepo = medicalRecordRepo;
        this.symptomDoctorRepo = symptomDoctorRepo;
    }

    public void createAppointmentFlow(
            String name,
            int age,
            String gender,
            String symptom
    ) {

        // validation
        if (name == null || name.isBlank()) {
            System.out.println("invalid name");
            return;
        }

        if (age <= 0) {
            System.out.println("invalid age");
            return;
        }

        if (symptom == null || symptom.isBlank()) {
            System.out.println("invalid symptom");
            return;
        }

        // add patient
        Patient patient = new Patient(name, age, gender);
        patientRepo.addPatient(patient);
        int patientId = patient.getId();

        // add symptom
        symptomRepo.addSymptomEntry(
                new SymptomEntry(patientId, symptom)
        );

        // get specialization from database
        String specialization =
                symptomDoctorRepo.getSpecializationBySymptom(symptom);

        // find doctor by specialization
        Doctor doctor = doctorRepo.getAllDoctors()
                .stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .findFirst()
                .orElse(null);

        if (doctor == null) {
            System.out.println("no doctor found");
            return;
        }

        // create appointment
        Appointment appointment = new Appointment(
                patientId,
                doctor.getId(),
                LocalDate.now()
        );
        appointmentRepo.addAppointment(appointment);


        System.out.println("\nAppointment created successfully:");
        System.out.println("Patient: " + name);
        System.out.println("Symptom: " + symptom);
        System.out.println("Appointment Date: " + LocalDate.now());
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
    }
}
