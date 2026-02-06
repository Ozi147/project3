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

            switch(role){

                // ... existing code ...

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

                    LocalDate day = null;
                    if (want.equalsIgnoreCase("yes")) {
                        System.out.println("Enter date (yyyy-MM-dd):");
                        day = InputValidator.readDate(sc);

                        List<MedicalRecord> list = medicalCtrl.getByDoctorIdAndDate(doctorId, day);
                        if (list.isEmpty()) {
                            System.out.println("No open appointments for this day.");
                        } else {
                            System.out.println("Patients for " + day + ":");
                            list.forEach(r -> System.out.println(
                                    "PatientId=" + r.getPatientId() + " | " +
                                            r.getPatientName() + " | Symptom: " + r.getSymptom() + " | " +
                                            "Doctor: " + r.getDoctorName() + " | " +
                                            "Spec: " + r.getSpecialization() + " | " +
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

                        Patient p = patientCtrl.getById(patientId);
                        if (!p.getName().equalsIgnoreCase(patientName.trim())) {
                            System.out.println("Patient id and patient name do not match.");
                            return;
                        }

                        boolean closedOk = appointmentCtrl.closeAppointment(doctorId, patientId, date);
                        if (closedOk) {
                            System.out.println("Appointment closed.");
                        } else {
                            System.out.println("Could not close appointment (maybe not found or already closed).");
                        }
                    }
                }

                // ... existing code ...

                default -> System.out.println("Invalid role");
            }
        }
    }
}