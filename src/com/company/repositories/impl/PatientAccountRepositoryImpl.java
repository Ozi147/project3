package com.company.repositories.impl;

import com.company.repositories.IPatientAccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientAccountRepositoryImpl implements IPatientAccountRepository {

    private final Connection connection;

    public PatientAccountRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createAccount(int patientId, String username, String passwordHash) {
        String sql = "INSERT INTO patient_accounts(patient_id, username, password_hash) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer findPatientIdByCredentials(String username, String passwordHash) {
        String sql = "SELECT patient_id FROM patient_accounts WHERE username = ? AND password_hash = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("patient_id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
