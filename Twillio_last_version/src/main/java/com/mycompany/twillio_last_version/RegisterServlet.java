package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String job = request.getParameter("job");
        String address = request.getParameter("address");
        String twilioSid = request.getParameter("twilio_sid");
        String twilioAuth = request.getParameter("twilio_auth_token");
        
        // Convert birthday string to java.sql.Date
        String birthdayStr = request.getParameter("birthday");
        Date birthday = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(birthdayStr);
            birthday = new Date(utilDate.getTime()); // Convert to java.sql.Date
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
            return; // Stop execution if the date conversion fails
        }

        String sql = "INSERT INTO customer (username, email, password, birthday, msisdn, job, address, twillio_sid, twillio_token, acc_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = PSQL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setDate(4, birthday); // ✅ Fix: Setting birthday as java.sql.Date
            pst.setString(5, phone);
            pst.setString(6, job);
            pst.setString(7, address);
            pst.setString(8, twilioSid);
            pst.setString(9, twilioAuth);
            pst.setNull(10, java.sql.Types.INTEGER); // Assuming acc_id is not provided

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✅ Registration successful for user: " + name);
                response.sendRedirect(request.getContextPath() + "/VerifyServlet?msisdn=" + phone);
            } else {
                System.out.println("❌ Registration failed for user: " + name);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}