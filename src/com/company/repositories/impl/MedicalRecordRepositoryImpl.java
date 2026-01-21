package repositories.impl;

import repositories.MedicalRecordRepository;

import java.sql.*;

public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private final Connection connection;

    public MedicalRecordRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(int patientId, String diagnosis) {
        String sql = "INSERT INTO medical_records(patient_id, diagnosis) VALUES (?, ?) RETURNING id";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, diagnosis);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
