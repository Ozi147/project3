package com.company;

import com.company.controllers.*;
import com.company.data.PostgresDB;
import com.company.models.Patient;
import com.company.models.MedicalRecord;
import com.company.repositories.impl.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        try(Connection conn = PostgresDB.getConnection();
            Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to database");

            // repositories
            var patientRepo = new PatientRepositoryImpl(conn);
            var doctorRepo = new DoctorRepositoryImpl(conn);
            var appointmentRepo = new AppointmentRepositoryImpl(conn);
            var symptomRepo = new SymptomEntryRepositoryImpl(conn);
            var symptomDoctorRepo = new SymptomDoctorRepositoryImpl(conn);
            var medicalRecordRepo = new MedicalRecordRepositoryImpl(conn);

            var accountRepo = new PatientAccountRepositoryImpl(conn);
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

                case "guest" -> {
                    System.out.println("You are a guest. You need to register to continue.");
                    System.out.println("Continue to registration? (yes/no)");
                    String answer = sc.nextLine().trim();
                    if (!answer.equalsIgnoreCase("yes")) return;

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

                    Patient patient = patientCtrl.register(name, age, gender); // uses PatientController
                    accountRepo.createAccount(patient.getId(), username, Password.sha256(password)); // patient account

                    System.out.println("You have registered successfully.");
                }

                case "patient" -> {
                    System.out.println("Please enter your username:");
                    String username = sc.nextLine().trim();

                    System.out.println("Please enter your password:");
                    String password = sc.nextLine();

                    Integer patientId = accountRepo.findPatientIdByCredentials(username, Password.sha256(password));
                    if (patientId == null) {
                        System.out.println("Invalid username or password.");
                        return;
                    }

                    System.out.println("You have successfully logged in.");
                    Patient patient = patientCtrl.getById(patientId); // uses PatientController
                    System.out.println("Do you want to create an appointment request (leave a symptom)? (yes/no)");
                    String want = sc.nextLine().trim();
                    if (want.equalsIgnoreCase("yes")) {
                        System.out.println("Enter your symptom:");
                        String symptom = sc.nextLine().trim();

                        symptomCtrl.addSymptom(patient.getId(), symptom); // uses SymptomEntryController

                        String specialization = symptomDoctorRepo.getSpecializationBySymptom(symptom);
                        var doctors = doctorCtrl.getDoctorsBySpecialization(specialization); // uses DoctorController
                        if (doctors.isEmpty()) throw new RuntimeException("No doctors found for specialization: " + specialization);

                        appointmentCtrl.createAppointment(patient.getId(), doctors.get(0).getId()); // uses AppointmentController
                        System.out.println("Request saved, appointment created.");
                    }

                    System.out.println("Do you want to view your medical card? (yes/no)");
                    String view = sc.nextLine().trim();
                    if (view.equalsIgnoreCase("yes")) {
                        List<MedicalRecord> records = medicalCtrl.getByPatientName(patient.getName()); // uses MedicalRecordController
                        System.out.println("Your medical records:");
                        if (records.isEmpty()) System.out.println("No records found yet.");
                        records.forEach(r -> System.out.println(
                                r.getPatientName() + " | " + r.getSymptom() + " | " +
                                        r.getAppointmentDate() + " | " + r.getDoctorName() + " | " +
                                        r.getSpecialization()
                        ));
                    }
                }

                case "doctor" -> {
                    System.out.println("Please enter your username:");
                    String username = sc.nextLine().trim();

                    System.out.println("Please enter your password:");
                    String password = sc.nextLine();

                    Integer doctorId = doctorAccountRepo.findDoctorIdByCredentials(username, Password.sha256(password));
                    if (doctorId == null) {
                        System.out.println("Invalid username or password.");
                        return;
                    }

                    System.out.println("You have successfully logged in.");

                    System.out.println("Do you want to view patients for a specific day? (yes/no)");
                    String want = sc.nextLine().trim();

                    if (want.equalsIgnoreCase("yes")) {
                        System.out.println("Enter date (yyyy-MM-dd):");
                        LocalDate day = InputValidator.readDate(sc);

                        List<MedicalRecord> list = medicalCtrl.getByDoctorIdAndDate(doctorId, day); // uses MedicalRecordController
                        if (list.isEmpty()) {
                            System.out.println("No open appointments for this day.");
                        } else {
                            System.out.println("Patients for " + day + ":");
                            list.forEach(r -> System.out.println(
                                    "PatientId=" + r.getPatientId() + " | " +
                                            r.getPatientName() + " | Symptom: " + r.getSymptom() + " | " +
                                            "Date: " + r.getAppointmentDate()
                            ));
                        }
                    }

                    System.out.println("Do you want to close an appointment? (yes/no)");
                    String close = sc.nextLine().trim();
                    if (close.equalsIgnoreCase("yes")) {
                        System.out.println("Enter patient id:");
                        int patientId = Integer.parseInt(sc.nextLine().trim());
                        System.out.println("Enter patient name (English letters only):");
                        String patientName = InputValidator.readRealName(sc);

                        System.out.println("Enter appointment date (yyyy-MM-dd):");
                        LocalDate date = InputValidator.readDate(sc);

                        Patient p = patientCtrl.getById(patientId); // uses PatientController
                        if (!p.getName().equalsIgnoreCase(patientName.trim())) {
                            System.out.println("Patient id and patient name do not match.");
                            return;
                        }

                        boolean closedOk = appointmentCtrl.closeAppointment(doctorId, patientId, date); // uses AppointmentController
                        System.out.println(closedOk ? "Appointment closed." : "Could not close appointment.");
                    }
                }

                case "receptionist" -> {
                    System.out.println("Register new patient:");
                    System.out.println("Name:");
                    String name = InputValidator.readRealName(sc);
                    System.out.println("Age:");
                    int age = InputValidator.readAge(sc);
                    System.out.println("Gender:");
                    String gender = sc.nextLine().trim();

                    Patient patient = patientCtrl.register(name, age, gender); // PatientController

                    System.out.println("Enter symptom for patient:");
                    String symptom = sc.nextLine().trim();
                    symptomCtrl.addSymptom(patient.getId(), symptom); // SymptomEntryController

                    String specialization = symptomDoctorRepo.getSpecializationBySymptom(symptom);
                    var doctors = doctorCtrl.getDoctorsBySpecialization(specialization); // DoctorController
                    if (doctors.isEmpty()) throw new RuntimeException("No doctors for specialization: " + specialization);

                    appointmentCtrl.createAppointment(patient.getId(), doctors.get(0).getId()); // AppointmentController
                    System.out.println("Appointment created for " + patient.getName());
                }

                case "admin" -> {
                    System.out.println("Admin: add doctor");
                    System.out.println("Doctor name:");
                    String dName = InputValidator.readRealName(sc);
                    System.out.println("Specialization:");
                    String spec = sc.nextLine().trim();

                    doctorCtrl.addDoctor(dName, spec); // DoctorController
                    System.out.println("Doctor created.");
                }

                default -> System.out.println("Invalid role");
            }
        }
    }
}