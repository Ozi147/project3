package com.company.repositories.impl;

import com.company.repositories.ISymptomDoctorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SymptomDoctorRepositoryImpl implements ISymptomDoctorRepository {

    private final Connection connection;

    public SymptomDoctorRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public String getSpecializationBySymptom(String symptom) {
        String sql = "SELECT specialization FROM symptom_doctor WHERE symptom = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, symptom.toLowerCase());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getString("specialization");
            }
        } catch(SQLException e){ throw new RuntimeException(e);}
        return "general"; // default
    }
}