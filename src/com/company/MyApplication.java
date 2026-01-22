package com.company;

import com.company.controllers.*;
import com.company.data.PostgresDB;
import com.company.models.Patient;

public class MyApplication {

    public static void start() {
        // подключение к базе
        PostgresDB db = new PostgresDB();

        // создаем репозитории
        var patientRepo = new com.company.repositories.impl.PatientRepositoryImpl(db.getConnection());
        var symptomRepo = new com.company.repositories.impl.SymptomEntryRepositoryImpl(db.getConnection());
        var doctorRepo = new com.company.repositories.impl.DoctorRepositoryImpl(db.getConnection());
        var appointmentRepo = new com.company.repositories.impl.AppointmentRepositoryImpl(db.getConnection());
        var medicalRecordRepo = new com.company.repositories.impl.MedicalRecordRepositoryImpl(db.getConnection());

        // создаем контроллеры
        PatientController patientController = new PatientController(patientRepo);
        SymptomEntryController symptomController = new SymptomEntryController(symptomRepo);
        DoctorController doctorController = new DoctorController(doctorRepo);
        AppointmentController appointmentController = new AppointmentController(appointmentRepo);
        MedicalRecordController medicalRecordController = new MedicalRecordController(medicalRecordRepo);

        // --- регистрируем пациентов и сохраняем объекты сразу ---
        Patient alice = patientController.registerReturn("Alice", 28, "female");
        Patient bob = patientController.registerReturn("Bob", 35, "male");

        // --- добавляем врачей ---
        doctorController.addDoctor("Dr. Smith", "Cardiology");
        doctorController.addDoctor("Dr. Johnson", "Neurology");

        // --- добавляем симптомы ---
        symptomController.add(alice.getId(), "headache");
        symptomController.add(alice.getId(), "fever");
        symptomController.add(bob.getId(), "cough");

        // --- создаем приёмы ---
        appointmentController.make(alice.getId(), 1);
        appointmentController.make(bob.getId(), 2);

        // --- выводим медицинские записи ---
        System.out.println("\nMedical records for Alice:");
        medicalRecordController.show(alice.getId());

        System.out.println("\nMedical records for Bob:");
        medicalRecordController.show(bob.getId());

        // --- выводим всех пациентов ---
        System.out.println("\nAll patients:");
        for (Patient p : patientRepo.getAllPatients()) {
            System.out.println(p);
        }

        // --- выводим всех врачей ---
        System.out.println("\nAll doctors:");
        doctorRepo.getAllDoctors().forEach(System.out::println);

        System.out.println("\n--- application started successfully ---");
    }
}
