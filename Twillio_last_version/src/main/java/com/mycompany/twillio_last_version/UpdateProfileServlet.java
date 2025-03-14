/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String msisdn = request.getParameter("phone"); 
        String username = request.getParameter("full_name");
        String birthday = request.getParameter("birthday");
        String email = request.getParameter("email");
        String job = request.getParameter("job_title");
        String address = request.getParameter("address");
        String twilio_sid = request.getParameter("twilio_sid");
        String twilio_token = request.getParameter("twilio_token");

        if (msisdn == null || msisdn.isEmpty()) {
            out.println("<h3 style='color:red;'>Error: MSISDN (Phone Number) is required!</h3>");
            return;
        }

        try (Connection conn = PSQL.getConnection()) {
            String updateQuery = "UPDATE customer SET username=?, birthday=?, email=?, job=?, address=?, twillio_sid=?, twillio_token=? WHERE msisdn=?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, username);
                stmt.setString(2, birthday);
                stmt.setString(3, email);
                stmt.setString(4, job);
                stmt.setString(5, address);
                stmt.setString(6, twilio_sid);
                stmt.setString(7, twilio_token);
                stmt.setString(8, msisdn);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    out.println("<h3 style='color:green;'>Profile updated successfully!</h3>");
                } else {
                    out.println("<h3 style='color:red;'>Error: Profile update failed. User might not exist.</h3>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Database error: " + e.getMessage() + "</h3>");
        }
    }
}

