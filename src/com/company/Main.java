package com.company;

import com.company.data.PostgresDB;
import com.company.repositories.impl.*;
import com.company.services.MedicalService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Connection conn = PostgresDB.getConnection();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to PostgreSQL successfully.\n");

            // create repository
            var patientRepo = new PatientRepositoryImpl(conn);
            var symptomRepo = new SymptomEntryRepositoryImpl(conn);
            var doctorRepo = new DoctorRepositoryImpl(conn);
            var appointmentRepo = new AppointmentRepositoryImpl(conn);
            var medicalRecordRepo = new MedicalRecordRepositoryImpl(conn);
            var symptomDoctorRepo = new SymptomDoctorRepositoryImpl(conn);

            // создаем сервис и передаем все репозитории
            var medicalService = new MedicalService(
                    patientRepo,
                    symptomRepo,
                    doctorRepo,
                    appointmentRepo,
                    medicalRecordRepo,
                    symptomDoctorRepo
            );

            System.out.println("Do you want to add a new patient? (yes/no):");
            String answer = sc.nextLine().trim().toLowerCase();

            if (answer.equals("yes")) {

                System.out.println("Enter patient name:");
                String name = sc.nextLine();

                System.out.println("Enter age:");
                int age = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter gender:");
                String gender = sc.nextLine();

                System.out.println("Enter symptom:");
                String symptom = sc.nextLine();

                // вызыв сервисного flow
                medicalService.createAppointmentFlow(name, age, gender, symptom);
            }

            System.out.println("\ndo you want to view your medical record? (yes/no):");
            if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;

            System.out.println("Enter patient name:");
            String name = sc.nextLine();

            var records = medicalRecordRepo.getMedicalRecordsByPatientName(name);

            if (records.isEmpty()) {
                System.out.println("No medical records found for " + name);
                return;
            }

            System.out.println("\nMedical records for " + name + ":");
            records.forEach(rec -> System.out.println(
                    "Patient: " + rec.getPatientName()
                            + " | Symptom: " + rec.getSymptom()
                            + " | Appointment Date: " + rec.getAppointmentDate()
                            + " | Doctor: " + rec.getDoctorName()
                            + " | Specialization: " + rec.getSpecialization()
            ));


        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
