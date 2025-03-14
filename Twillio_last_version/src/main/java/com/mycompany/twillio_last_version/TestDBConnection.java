package com.mycompany.twillio_last_version;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author syousrei
 */


public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/sms_db";
        String user = "twillo";
        String password = "TSMS";

        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("✅ Database connected successfully!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }
}

