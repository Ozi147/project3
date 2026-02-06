package com.company.repositories.impl;

import com.company.models.Appointment;
import com.company.repositories.IAppointmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements IAppointmentRepository {

    private final Connection connection;

    public AppointmentRepositoryImpl(Connection connection){ this.connection = connection; }

    @Override
    public boolean addAppointment(Appointment appointment){
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, Date.valueOf(appointment.getDate()));
            ps.executeUpdate();
            return true;
        } catch(SQLException e){ throw new RuntimeException(e);}
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId){
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, patientId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new Appointment(
                            rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            rs.getDate("appointment_date").toLocalDate()
                    ));
                }
            }
        } catch(SQLException e){ throw new RuntimeException(e);}
        return list;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(int doctorId){
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, doctorId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new Appointment(
                            rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            rs.getDate("appointment_date").toLocalDate()
                    ));
                }
            }
        } catch(SQLException e){ throw new RuntimeException(e);}
        return list;
    }
}