package repositories.impl;

import repositories.AppointmentRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final Connection connection;

    public AppointmentRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(int patientId, int doctorId, Date visitDate) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, visit_date) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setDate(3, visitDate);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
