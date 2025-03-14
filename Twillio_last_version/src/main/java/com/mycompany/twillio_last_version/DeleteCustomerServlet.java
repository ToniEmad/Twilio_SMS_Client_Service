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
@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msisdn = request.getParameter("msisdn");

        try (Connection conn = PSQL.getConnection()) {
            String query = "DELETE FROM customer WHERE msisdn = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, msisdn);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
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
