package com.company.repositories.impl;

import com.company.repositories.ISymptomDoctorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SymptomDoctorRepositoryImpl implements ISymptomDoctorRepository {

    private final Connection connection;

    public SymptomDoctorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getSpecializationBySymptom(String symptom) {
        String sql = "SELECT specialization FROM symptom_doctor WHERE symptom = ?";
        String specialization = "general";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, symptom.toLowerCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                specialization = rs.getString("specialization");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return specialization;
    }
}
