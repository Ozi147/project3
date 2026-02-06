package com.company.repositories.impl;

import com.company.repositories.IDoctorAccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorAccountRepositoryImpl implements IDoctorAccountRepository {

    private final Connection connection;

    public DoctorAccountRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createAccount(int doctorId, String username, String passwordHash) {
        String sql = "INSERT INTO doctor_accounts(doctor_id, username, password_hash) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer findDoctorIdByCredentials(String username, String passwordHash) {
        String sql = "SELECT doctor_id FROM doctor_accounts WHERE username = ? AND password_hash = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("doctor_id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}