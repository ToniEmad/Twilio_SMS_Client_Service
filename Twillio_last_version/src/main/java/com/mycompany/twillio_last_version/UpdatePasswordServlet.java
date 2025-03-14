/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author syousrei
 */

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = {"/UpdatePasswordServlet"})
public class UpdatePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("user_profile.html?status=password_mismatch");
            return;
        }

        try (Connection conn = PSQL.getConnection()) {
            // تحقق من كلمة المرور الحالية
            String checkPasswordQuery = "SELECT password FROM customer WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkPasswordQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    
                    if (!storedPassword.equals(currentPassword)) {
                        response.sendRedirect("user_profile.html?status=wrong_password");
                        return;
                    }
                } else {
                    response.sendRedirect("user_profile.html?status=user_not_found");
                    return;
                }
            }

            // تحديث كلمة المرور
            String updateQuery = "UPDATE customer SET password = ? WHERE username = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);
                
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect("user_profile.html?status=success");
                } else {
                    response.sendRedirect("user_profile.html?status=error");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("user_profile.html?status=db_error");
        }
    }
}
