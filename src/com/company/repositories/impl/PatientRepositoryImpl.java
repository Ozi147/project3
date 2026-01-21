package repositories.impl;

import models.Patient;
import repositories.PatientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepositoryImpl implements PatientRepository {

    private final Connection connection;

    public PatientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO patients(full_name, birth_date) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, patient.getFullName());
            ps.setDate(2, Date.valueOf(patient.getBirthDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDate("birth_date").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
}

