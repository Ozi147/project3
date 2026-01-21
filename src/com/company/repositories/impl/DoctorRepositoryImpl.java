package repositories.impl;

import models.Doctor;
import repositories.DoctorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepositoryImpl implements DoctorRepository {

    private final Connection connection;

    public DoctorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Doctor doctor) {
        String sql = "INSERT INTO doctors(full_name, specialization) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setString(2, doctor.getSpecialization());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialization")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }
}
