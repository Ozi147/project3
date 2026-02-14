package com.company;

import com.company.controllers.*;
import com.company.data.PostgresDB;
import com.company.models.*;
import com.company.repositories.impl.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        try (
                Connection conn = PostgresDB.getConnection();
                Scanner sc = new Scanner(System.in)
        ) {

            System.out.println("Connected to database");

            // repositories
            var patientRepo = new PatientRepositoryImpl(conn);
            var doctorRepo = new DoctorRepositoryImpl(conn);
            var appointmentRepo = new AppointmentRepositoryImpl(conn);
            var symptomRepo = new SymptomEntryRepositoryImpl(conn);
            var symptomDoctorRepo = new SymptomDoctorRepositoryImpl(conn);
            var medicalRecordRepo = new MedicalRecordRepositoryImpl(conn);

            var patientAccountRepo = new PatientAccountRepositoryImpl(conn);
            var doctorAccountRepo = new DoctorAccountRepositoryImpl(conn);

            // controllers
            var patientCtrl = new PatientController(patientRepo);
            var doctorCtrl = new DoctorController(doctorRepo);
            var appointmentCtrl = new AppointmentController(appointmentRepo);
            var symptomCtrl = new SymptomEntryController(symptomRepo);
            var medicalCtrl = new MedicalRecordController(medicalRecordRepo);

            System.out.println("Enter your role (guest / patient / doctor / receptionist / admin):");
            String role = sc.nextLine().trim().toLowerCase();

            switch (role) {

                /* ===================== GUEST ===================== */
                case "guest" -> {
                    System.out.println("You are a guest. You need to register to continue.");
                    System.out.println("Continue to registration? (yes/no)");
                    if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;

                    System.out.println("Create a username:");
                    String username = InputValidator.readUsername(sc);

                    System.out.println("Create a password:");
                    String password = InputValidator.readPassword(sc);

                    System.out.println("Enter your name:");
                    String name = InputValidator.readRealName(sc);

                    System.out.println("Enter your age:");
                    int age = InputValidator.readAge(sc);

                    System.out.println("Enter your gender:");
                    String gender = sc.nextLine().trim();

                    Patient patient = patientCtrl.register(name, age, gender);
                    patientAccountRepo.createAccount(
                            patient.getId(),
                            username,
                            Password.sha256(password)
                    );

                    System.out.println("Registration completed.");
                }

                /* ===================== PATIENT ===================== */
                case "patient" -> {
                    System.out.println("Please enter your username:");
                    String username = sc.nextLine().trim();

                    System.out.println("Please enter your password:");
                    String password = sc.nextLine();

                    // 1) пробуем как хеш (если в БД лежат SHA-256)
                    String hashed = Password.sha256(password);
                    Integer patientId = patientAccountRepo
                            .findPatientIdByCredentials(username, hashed);

                    // 2) если не нашли — пробуем как "сырой" пароль (как у тебя сейчас в БД: 1111/2222/7777)
                    if (patientId == null) {
                        patientId = patientAccountRepo
                                .findPatientIdByCredentials(username, password);
                    }

                    if (patientId == null) {
                        System.out.println("Invalid credentials");
                        return;
                    }

                    Patient patient = patientCtrl.getById(patientId);
                    System.out.println("Welcome, " + patient.getName());
                    System.out.println("Do you want to create an appointment request (leave a symptom)? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {

                        System.out.println("Enter symptom:");
                        String symptom = sc.nextLine().trim();

                        symptomCtrl.addSymptom(patient.getId(), symptom);

                        String specialization =
                                symptomDoctorRepo.getSpecializationBySymptom(symptom);

                        var doctors =
                                doctorCtrl.getDoctorsBySpecialization(specialization);

                        appointmentCtrl.createAppointment(
                                patient.getId(),
                                doctors.get(0).getId()
                        );

                        System.out.println("Appointment created.");
                    }

                    System.out.println("Do you want to see your appointments? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        appointmentCtrl
                                .getAppointmentsByPatient(patient.getId())
                                .forEach(a ->
                                        System.out.println("DoctorId=" + a.getDoctorId()
                                                + " | Date=" + a.getDate()));
                    }

                    System.out.println("Do you want to see your symptom history? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        symptomCtrl
                                .getSymptomsByPatient(patient.getId())
                                .forEach(s ->
                                        System.out.println(s.getEntryDate()
                                                + " | " + s.getSymptom()));
                    }

                    System.out.println("Do you want to see your medical card? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        medicalCtrl
                                .getByPatientName(patient.getName())
                                .forEach(r ->
                                        System.out.println(
                                                r.getPatientName() + " | " +
                                                        r.getSymptom() + " | " +
                                                        r.getAppointmentDate() + " | " +
                                                        r.getDoctorName()));
                    }
                }

                /* ===================== DOCTOR ===================== */
                case "doctor" -> {
                    System.out.println("Username:");
                    String username = sc.nextLine().trim();

                    System.out.println("Password:");
                    String password = sc.nextLine();

                    // 1) пробуем как хеш (если в БД лежат SHA-256)
                    String hashed = Password.sha256(password);
                    Integer doctorId =
                            doctorAccountRepo.findDoctorIdByCredentials(
                                    username,
                                    hashed
                            );

                    // 2) если не нашли — пробуем как "сырой" пароль (как у тебя сейчас в БД: 1111/2222/7777)
                    if (doctorId == null) {
                        doctorId =
                                doctorAccountRepo.findDoctorIdByCredentials(
                                        username,
                                        password
                                );
                    }

                    if (doctorId == null) {
                        System.out.println("Invalid credentials");
                        return;
                    }

                    System.out.println("Doctor logged in.");

                    System.out.println("View all your appointments? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        appointmentCtrl
                                .getAppointmentsByDoctor(doctorId)
                                .forEach(a ->
                                        System.out.println("PatientId=" + a.getPatientId()
                                                + " | Date=" + a.getDate()));
                    }

                    System.out.println("Do you want to view patients for a date? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.println("Enter date (yyyy-MM-dd):");
                        LocalDate date = InputValidator.readDate(sc);

                        medicalCtrl
                                .getByDoctorIdAndDate(doctorId, date)
                                .forEach(r ->
                                        System.out.println(
                                                r.getPatientName() + " | " +
                                                        r.getSymptom()));
                    }

                    System.out.println("Do you want to close an appointment? (yes/no)");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.println("Patient id:");
                        int pid = Integer.parseInt(sc.nextLine());

                        System.out.println("Date:");
                        LocalDate date = InputValidator.readDate(sc);

                        appointmentCtrl.closeAppointment(doctorId, pid, date);
                        System.out.println("Appointment closed.");
                    }
                }

                /* ===================== RECEPTIONIST ===================== */
                case "receptionist" -> {
                    System.out.println("Register new patient:");

                    System.out.println("Name:");
                    String name = InputValidator.readRealName(sc);

                    System.out.println("Age:");
                    int age = InputValidator.readAge(sc);

                    System.out.println("Gender:");
                    String gender = sc.nextLine();

                    Patient patient = patientCtrl.register(name, age, gender);

                    System.out.println("Enter symptom:");
                    String symptom = sc.nextLine();

                    symptomCtrl.addSymptom(patient.getId(), symptom);

                    String specialization =
                            symptomDoctorRepo.getSpecializationBySymptom(symptom);

                    var doctors =
                            doctorCtrl.getDoctorsBySpecialization(specialization);

                    appointmentCtrl.createAppointment(
                            patient.getId(),
                            doctors.get(0).getId()
                    );

                    System.out.println("Appointment created.");
                }

                /* ===================== ADMIN ===================== */
                case "admin" -> {
                    System.out.println("Admin: add doctor");

                    System.out.println("Doctor name:");
                    String dName = InputValidator.readRealName(sc);

                    System.out.println("Specialization:");
                    String spec = sc.nextLine();

                    Doctor d = doctorCtrl.addDoctor(dName, spec);

                    Doctor loaded = doctorCtrl.getDoctorById(d.getId());
                    System.out.println("Created: " + loaded.getName());

                    System.out.println("All patients:");
                    patientRepo.getAllPatients()
                            .forEach(p ->
                                    System.out.println(p.getId() + " " + p.getName()));
                }

                default -> System.out.println("Invalid role");
            }
        }
    }
}