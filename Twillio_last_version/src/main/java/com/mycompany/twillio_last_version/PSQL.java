package com.mycompany.twillio_last_version;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQL {         
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/sms_db";
    private static final String USER = "twillo";
    private static final String PASSWORD = "TSMS";

    static {
        try {
            Class.forName(JDBC_DRIVER);  // Load PostgreSQL JDBC driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connection established.");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();  // <-- Print detailed error
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
