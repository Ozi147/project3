package com.company.repositories.impl;

import com.company.models.MedicalRecord;
import com.company.models.SymptomEntry;
import com.company.repositories.IMedicalRecordRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MedicalRecordRepositoryImpl implements IMedicalRecordRepository {

    private final Connection connection;

    public MedicalRecordRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public MedicalRecord getByPatientId(int patientId) {
        MedicalRecord record = new MedicalRecord(patientId);

        String sql = """
                SELECT description, created_at
                FROM symptom_entries
                WHERE patient_id = ?
                ORDER BY created_at
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record.symptoms.add(
                        new SymptomEntry(
                                rs.getString("description"),
                                rs.getDate("created_at").toLocalDate()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return record;
    }
}
