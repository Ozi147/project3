package com.company.repositories.impl;

import com.company.models.Doctor;
import com.company.repositories.IDoctorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorRepositoryImpl implements IDoctorRepository {

    private final Connection connection;

    public DoctorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Doctor addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors(name, specialization) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return new Doctor(id, doctor.getName(), doctor.getSpecialization());
                }
            }
            throw new RuntimeException("Failed to insert doctor (no id returned).");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("specialization"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("specialization")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return getAllDoctors().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }
}