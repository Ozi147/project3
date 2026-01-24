package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/health_db";
        String user = "postgres";
        String password = "0000";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to PostgreSQL successfully.\n");

            // ===== ADD NEW PATIENT =====
            System.out.println("Do you want to add a new patient? (yes/no):");
            String answer = sc.nextLine().trim().toLowerCase();

            if (answer.equals("yes")) {

                System.out.println("Enter patient name:");
                String patientName = sc.nextLine();

                System.out.println("Enter age:");
                int age = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter gender:");
                String gender = sc.nextLine();

                System.out.println("Enter symptom:");
                String symptom = sc.nextLine();

                int patientId = 0;

                // insert patient
                String insertPatient =
                        "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?) RETURNING id";
                try (PreparedStatement ps = conn.prepareStatement(insertPatient)) {
                    ps.setString(1, patientName);
                    ps.setInt(2, age);
                    ps.setString(3, gender);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) patientId = rs.getInt("id");
                }

                // insert symptom entry
                String insertSymptom =
                        "INSERT INTO symptom_entries (patient_id, symptom, entry_date) VALUES (?, ?, CURRENT_DATE)";
                try (PreparedStatement ps = conn.prepareStatement(insertSymptom)) {
                    ps.setInt(1, patientId);
                    ps.setString(2, symptom);
                    ps.executeUpdate();
                }

                // get specialization by symptom (default = general)
                String specialization = "general";
                String getSpec =
                        "SELECT specialization FROM symptom_doctor WHERE symptom = ?";
                try (PreparedStatement ps = conn.prepareStatement(getSpec)) {
                    ps.setString(1, symptom.toLowerCase());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        specialization = rs.getString("specialization");
                    }
                }

                // get doctor
                int doctorId = 0;
                String doctorName = "";

                String getDoctor =
                        "SELECT id, name FROM doctors WHERE specialization = ? LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(getDoctor)) {
                    ps.setString(1, specialization);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        doctorId = rs.getInt("id");
                        doctorName = rs.getString("name");
                    }
                }

                if (doctorId == 0) {
                    System.out.println("No doctor found for this symptom.");
                    return;
                }

                // create appointment (WITH symptom + date)
                String insertAppointment =
                        "INSERT INTO appointments (patient_id, doctor_id, appointment_date, notes) " +
                                "VALUES (?, ?, CURRENT_DATE, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertAppointment)) {
                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setString(3, symptom); // сразу кладем симптом
                    ps.executeUpdate();
                }

                // create medical record (связь с appointment, берем symptom)
                String insertMedicalRecord =
                        "INSERT INTO medical_records " +
                                "(patient_id, doctor_id, appointment_id, symptoms, record_date) " +
                                "VALUES (?, ?, currval('appointments_id_seq'), ?, CURRENT_DATE)";
                try (PreparedStatement ps = conn.prepareStatement(insertMedicalRecord)) {
                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setString(3, symptom);
                    ps.executeUpdate();
                }

                System.out.println("\nAppointment created successfully:");
                System.out.println("Patient: " + patientName);
                System.out.println("Symptom: " + symptom);
                System.out.println("Appointment Date: " + java.time.LocalDate.now());
                System.out.println("Doctor: " + doctorName);
                System.out.println("Specialization: " + specialization);
            }

            // ===== VIEW MEDICAL RECORD =====
            System.out.println("\nDo you want to view your medical record? (yes/no):");
            String viewAnswer = sc.nextLine().trim().toLowerCase();

            if (viewAnswer.equals("yes")) {
                System.out.println("Enter your name:");
                String name = sc.nextLine();

                String sql =
                        "SELECT p.name AS patient_name, mr.symptoms, a.appointment_date, d.name AS doctor_name " +
                                "FROM medical_records mr " +
                                "JOIN appointments a ON mr.appointment_id = a.id " +
                                "JOIN patients p ON mr.patient_id = p.id " +
                                "JOIN doctors d ON mr.doctor_id = d.id " +
                                "WHERE p.name = ?";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    boolean found = false;
                    System.out.println("\nMedical records for " + name + ":");
                    while (rs.next()) {
                        found = true;
                        System.out.println(
                                "Patient: " + rs.getString("patient_name") +
                                        " | Symptom: " + rs.getString("symptoms") +
                                        " | Appointment Date: " + rs.getDate("appointment_date") +
                                        " | Doctor: " + rs.getString("doctor_name")
                        );
                    }

                    if (!found) {
                        System.out.println("No medical records found.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
