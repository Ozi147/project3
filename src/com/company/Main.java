package com.company;

import com.company.controllers.*;
import com.company.data.PostgresDB;
import com.company.models.Patient;
import com.company.models.MedicalRecord;
import com.company.repositories.impl.*;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        try (Connection conn = PostgresDB.getConnection();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to database");

            // repositories
            var patientRepo = new PatientRepositoryImpl(conn);
            var doctorRepo = new DoctorRepositoryImpl(conn);
            var appointmentRepo = new AppointmentRepositoryImpl(conn);
            var symptomRepo = new SymptomEntryRepositoryImpl(conn);
            var symptomDoctorRepo = new SymptomDoctorRepositoryImpl(conn);
            var medicalRecordRepo = new MedicalRecordRepositoryImpl(conn);

            // NEW: accounts repo
            var accountRepo = new PatientAccountRepositoryImpl(conn);

            // controllers
            var patientCtrl = new PatientController(patientRepo);
            var doctorCtrl = new DoctorController(doctorRepo);
            var appointmentCtrl = new AppointmentController(appointmentRepo);
            var symptomCtrl = new SymptomEntryController(symptomRepo);
            var medicalCtrl = new MedicalRecordController(medicalRecordRepo);

            System.out.println("Enter your role (guest / patient / doctor / receptionist / admin):");
            String role = sc.nextLine().trim().toLowerCase();

            switch (role) {
                case "guest" -> {
                    System.out.println("You are a guest. You need to register to continue.");
                    System.out.println("Continue to registration? (yes/no)");
                    String answer = sc.nextLine().trim();

                    if (!answer.equalsIgnoreCase("yes")) {
                        System.out.println("Goodbye.");
                        return;
                    }

                    System.out.println("Create a username:");
                    String username = InputValidator.readUsername(sc);

                    System.out.println("Create a password:");
                    String password = InputValidator.readPassword(sc);

                    System.out.println("Enter your name (English letters only):");
                    String name = InputValidator.readRealName(sc);

                    System.out.println("Enter your age (digits only):");
                    int age = InputValidator.readAge(sc);

                    System.out.println("Enter your gender:");
                    String gender = sc.nextLine().trim();

                    Patient patient = patientCtrl.register(name, age, gender);
                    accountRepo.createAccount(patient.getId(), username, PasswordHasher.sha256(password));

                    System.out.println("You have registered successfully.");
                    System.out.println("Your patient id is: " + patient.getId());
                }

                case "patient" -> {
                    System.out.println("Please enter your username:");
                    String username = sc.nextLine().trim();

                    System.out.println("Please enter your password:");
                    String password = sc.nextLine();

                    Integer patientId = accountRepo.findPatientIdByCredentials(username, PasswordHasher.sha256(password));
                    if (patientId == null) {
                        System.out.println("Invalid username or password.");
                        return;
                    }

                    System.out.println("You have successfully logged in.");

                    Patient patient = patientCtrl.getById(patientId);

                    System.out.println("Do you want to create an appointment request (leave a symptom)? (yes/no)");
                    String want = sc.nextLine().trim();
                    if (want.equalsIgnoreCase("yes")) {
                        System.out.println("Enter your symptom:");
                        String symptom = sc.nextLine().trim();
                        symptomCtrl.addSymptom(patient.getId(), symptom);

                        String specialization = symptomDoctorRepo.getSpecializationBySymptom(symptom);
                        var doctors = doctorCtrl.getDoctorsBySpecialization(specialization);
                        if (doctors.isEmpty()) throw new RuntimeException("No doctors found for specialization: " + specialization);

                        appointmentCtrl.createAppointment(patient.getId(), doctors.get(0).getId());
                        System.out.println("Your request was saved and an appointment was created.");
                    }

                    System.out.println("Do you want to view your medical card? (yes/no)");
                    String view = sc.nextLine().trim();
                    if (view.equalsIgnoreCase("yes")) {
                        List<MedicalRecord> records = medicalCtrl.getByPatientName(patient.getName());
                        System.out.println("Your medical records:");
                        if (records.isEmpty()) {
                            System.out.println("No records found yet.");
                        } else {
                            records.forEach(r -> System.out.println(
                                    r.getPatientName() + " | " + r.getSymptom() + " | " +
                                            r.getAppointmentDate() + " | " + r.getDoctorName() + " | " +
                                            r.getSpecialization()
                            ));
                        }
                    }
                }

                case "doctor" -> {
                    System.out.println("Enter your doctor id:");
                    int docId = Integer.parseInt(sc.nextLine());
                    List<MedicalRecord> patients = medicalCtrl.getByDoctorId(docId);
                    System.out.println("Your patients:");
                    patients.forEach(r -> System.out.println(
                            r.getPatientName() + " | " + r.getSymptom() + " | " +
                                    r.getAppointmentDate()
                    ));
                }

                case "receptionist" -> {
                    System.out.println("Register new patient:");
                    System.out.println("Name:");
                    String name = sc.nextLine();
                    System.out.println("Age:");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.println("Gender:");
                    String gender = sc.nextLine();
                    Patient patient = patientCtrl.register(name, age, gender);

                    System.out.println("Enter symptom for patient:");
                    String symptom = sc.nextLine();
                    symptomCtrl.addSymptom(patient.getId(), symptom);

                    String specialization = symptomDoctorRepo.getSpecializationBySymptom(symptom);
                    var doctors = doctorCtrl.getDoctorsBySpecialization(specialization);
                    if (doctors.isEmpty()) throw new RuntimeException("No doctors for specialization");

                    appointmentCtrl.createAppointment(patient.getId(), doctors.get(0).getId());
                    System.out.println("Appointment created for " + patient.getName());
                }

                case "admin" -> {
                    System.out.println("Admin: you can add doctors, manage specializations");
                    System.out.println("Add doctor name:");
                    String dName = sc.nextLine();
                    System.out.println("Specialization:");
                    String spec = sc.nextLine();
                    doctorCtrl.addDoctor(dName, spec);
                }

                default -> System.out.println("Invalid role");
            }
        }
    }
}