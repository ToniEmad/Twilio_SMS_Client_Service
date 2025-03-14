/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author syousrei
 */
@WebServlet("/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String job = request.getParameter("job");
        String address = request.getParameter("address");
        String twilioSid = request.getParameter("twilio_sid");
        String twilioAuthToken = request.getParameter("twilio_auth_token");

        try (Connection conn = PSQL.getConnection()) {
            String query = "INSERT INTO customer (username, email, password, birthday, msisdn, job, address, twillio_sid, twillio_token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, birthday);
                stmt.setString(5, phone);
                stmt.setString(6, job);
                stmt.setString(7, address);
                stmt.setString(8, twilioSid);
                stmt.setString(9, twilioAuthToken);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("error");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("error");
        }
    }
}
