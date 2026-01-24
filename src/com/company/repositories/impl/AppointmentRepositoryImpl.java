package com.company.repositories.impl;

import com.company.models.Appointment;
import com.company.repositories.IAppointmentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements IAppointmentRepository {

    private final Connection connection;

    public AppointmentRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, visit_date) VALUES (?, ?, ?)";


        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, appointment.patientId);
            ps.setInt(2, appointment.doctorId);
            ps.setDate(3, java.sql.Date.valueOf(appointment.date));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("visit_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
