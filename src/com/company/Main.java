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
        try(Connection conn = PostgresDB.getConnection();
            Scanner sc = new Scanner(System.in)) {

            System.out.println("connected to database");

            // репозитории
            var patientRepo = new PatientRepositoryImpl(conn);
            var doctorRepo = new DoctorRepositoryImpl(conn);
            var appointmentRepo = new AppointmentRepositoryImpl(conn);
            var symptomRepo = new SymptomEntryRepositoryImpl(conn);
            var symptomDoctorRepo = new SymptomDoctorRepositoryImpl(conn);
            var medicalRecordRepo = new MedicalRecordRepositoryImpl(conn);

            // контроллеры
            var patientCtrl = new PatientController(patientRepo);
            var doctorCtrl = new DoctorController(doctorRepo);
            var appointmentCtrl = new AppointmentController(appointmentRepo);
            var symptomCtrl = new SymptomEntryController(symptomRepo);
            var medicalCtrl = new MedicalRecordController(medicalRecordRepo);

            System.out.println("enter your role (guest / patient / doctor / receptionist / admin):");
            String role = sc.nextLine().trim().toLowerCase();

            switch(role){
                case "guest" -> {
                    System.out.println("guest: you can register or see basic info");
                    System.out.println("do you want to register? (yes/no)");
                    if(sc.nextLine().equalsIgnoreCase("yes")){
                        System.out.println("name:");
                        String name = sc.nextLine();
                        System.out.println("age:");
                        int age = Integer.parseInt(sc.nextLine());
                        System.out.println("gender:");
                        String gender = sc.nextLine();
                        patientCtrl.register(name, age, gender);
                    }
                }
                case "patient" -> {
                    System.out.println("enter patient name:");
                    String name = sc.nextLine();
                    List<MedicalRecord> records = medicalCtrl.getByPatientName(name);
                    System.out.println("your medical records:");
                    records.forEach(r -> System.out.println(
                            r.getPatientName() + " | " + r.getSymptom() + " | " +
                                    r.getAppointmentDate() + " | " + r.getDoctorName() + " | " +
                                    r.getSpecialization()
                    ));
                }
                case "doctor" -> {
                    System.out.println("enter your doctor id:");
                    int docId = Integer.parseInt(sc.nextLine());
                    List<MedicalRecord> patients = medicalCtrl.getByDoctorId(docId);
                    System.out.println("your patients:");
                    patients.forEach(r -> System.out.println(
                            r.getPatientName() + " | " + r.getSymptom() + " | " +
                                    r.getAppointmentDate()
                    ));
                }
                case "receptionist" -> {
                    System.out.println("register new patient:");
                    System.out.println("name:");
                    String name = sc.nextLine();
                    System.out.println("age:");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.println("gender:");
                    String gender = sc.nextLine();
                    Patient patient = patientCtrl.register(name, age, gender);

                    System.out.println("enter symptom for patient:");
                    String symptom = sc.nextLine();
                    symptomCtrl.addSymptom(patient.getId(), symptom);

                    String specialization = symptomDoctorRepo.getSpecializationBySymptom(symptom);
                    var doctors = doctorCtrl.getDoctorsBySpecialization(specialization);
                    if(doctors.isEmpty()) throw new RuntimeException("no doctors for specialization");

                    var appointment = appointmentCtrl.createAppointment(patient.getId(), doctors.get(0).getId());
                    System.out.println("appointment created for " + patient.getName());
                }
                case "admin" -> {
                    System.out.println("admin: you can add doctors, manage specializations");
                    System.out.println("add doctor name:");
                    String dName = sc.nextLine();
                    System.out.println("specialization:");
                    String spec = sc.nextLine();
                    doctorCtrl.addDoctor(dName, spec);
                }
                default -> System.out.println("invalid role");
            }
        }
    }
}
