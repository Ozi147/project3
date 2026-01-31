package com.company.repositories.impl;

import com.company.models.MedicalRecord;
import com.company.repositories.IMedicalRecordRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordRepositoryImpl implements IMedicalRecordRepository {

    private final Connection connection;

    public MedicalRecordRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public MedicalRecord getByPatientId(int patientId) {
        MedicalRecord record = null;
        String sql = """
            SELECT p.id AS patient_id, p.name AS patient_name, se.symptom, a.appointment_date, 
                   d.name AS doctor_name, d.specialization
            FROM patients p
            JOIN symptom_entries se ON p.id = se.patient_id
            JOIN appointments a ON a.patient_id = p.id
            JOIN doctors d ON a.doctor_id = d.id
            WHERE p.id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                record = new MedicalRecord(
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("symptom"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getString("doctor_name"),
                        rs.getString("specialization")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return record;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientName(String name) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = """
            SELECT p.id AS patient_id, p.name AS patient_name, se.symptom, a.appointment_date, 
                   d.name AS doctor_name, d.specialization
            FROM patients p
            JOIN symptom_entries se ON p.id = se.patient_id
            JOIN appointments a ON a.patient_id = p.id
            JOIN doctors d ON a.doctor_id = d.id
            WHERE p.name = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(new MedicalRecord(
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("symptom"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getString("doctor_name"),
                        rs.getString("specialization")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
