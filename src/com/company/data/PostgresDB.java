package com.company.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB {

    private Connection connection;

    public PostgresDB() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/hospital_db",
                    "postgres",
                    "0000"
            );
            System.out.println("connected to postgresql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
