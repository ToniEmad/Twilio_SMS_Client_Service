package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ChkLoginServlet")
public class ChkLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 24 * 60 * 60); // 30 يوم

        ServletContext context = getServletContext();
        String contextPath = request.getContextPath();
        
        String sqlAdmin = "SELECT role FROM account WHERE username = ? AND password = ?";
        String sqlUser = "SELECT username, twillio_sid, twillio_token, email, msisdn FROM customer WHERE username = ? AND password = ?";

        try (Connection conn = PSQL.getConnection()) {
            // ✅ Check if Admin
            try (PreparedStatement pst = conn.prepareStatement(sqlAdmin)) {
                pst.setString(1, username);
                pst.setString(2, password);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        session.setAttribute("username", username);
                        session.setAttribute("role", "admin");

                        Integer visitCount = (Integer) context.getAttribute("Active_Users_Visit_Count");
                        if (visitCount == null) visitCount = 0;
                        context.setAttribute("Active_Users_Visit_Count", visitCount + 1);

                        response.sendRedirect(contextPath + "/admin_profile.html");
                        return;
                    }
                }
            }
            
            // ✅ Check if User
            try (PreparedStatement pst = conn.prepareStatement(sqlUser)) {
                pst.setString(1, username);
                pst.setString(2, password);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        session.setAttribute("username", username);
                        session.setAttribute("role", "customer");
                        session.setAttribute("twillio_sid", rs.getString("twillio_sid"));
                        session.setAttribute("twillio_token", rs.getString("twillio_token"));
                        session.setAttribute("email", rs.getString("email"));
                        session.setAttribute("msisdn", rs.getString("msisdn"));

                        response.sendRedirect(contextPath + "/send_Messages.html");
                        return;
                    }
                }
            }

            // ❌ Login failed: Redirect with error
            response.sendRedirect(request.getContextPath() + "/login.html?error=1");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
