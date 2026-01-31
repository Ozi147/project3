package com.company.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/health_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";

    private static Connection connection;

    private PostgresDB() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
