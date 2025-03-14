package com.mycompany.twillio_last_version;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");

        String query = "SELECT username, birthday, msisdn, email, job, address, twillio_sid, twillio_token FROM customer WHERE username = ?";

        try (Connection conn = PSQL.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            String userProfileHTML = new String(Files.readAllBytes(Paths.get(getServletContext().getRealPath("/user_profile.html"))));

            if (rs.next()) {
                userProfileHTML = userProfileHTML.replace("{{username}}", rs.getString("username") == null ? "" : rs.getString("username"));
                userProfileHTML = userProfileHTML.replace("{{birthday}}", rs.getDate("birthday") == null ? "" : rs.getDate("birthday").toString());
                userProfileHTML = userProfileHTML.replace("{{msisdn}}", rs.getString("msisdn") == null ? "" : rs.getString("msisdn"));
                userProfileHTML = userProfileHTML.replace("{{email}}", rs.getString("email") == null ? "" : rs.getString("email"));
                userProfileHTML = userProfileHTML.replace("{{job}}", rs.getString("job") == null ? "" : rs.getString("job"));
                userProfileHTML = userProfileHTML.replace("{{address}}", rs.getString("address") == null ? "" : rs.getString("address"));
                userProfileHTML = userProfileHTML.replace("{{twillio_sid}}", rs.getString("twillio_sid") == null ? "" : rs.getString("twillio_sid"));
                userProfileHTML = userProfileHTML.replace("{{twillio_token}}", rs.getString("twillio_token") == null ? "" : rs.getString("twillio_token"));
            }

            response.getWriter().write(userProfileHTML);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

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

        String birthdayStr = request.getParameter("birthday");
        Date birthday = null;
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            try {
                birthday = Date.valueOf(birthdayStr);  // تحويل النص إلى تاريخ
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format. Use YYYY-MM-DD.");
                return;
            }
        }

        String msisdn = request.getParameter("msisdn");
        String email = request.getParameter("email");
        String job = request.getParameter("job");
        String address = request.getParameter("address");
        String twillio_sid = request.getParameter("twillio_sid");
        String twillio_token = request.getParameter("twillio_token");

        String updateQuery = "UPDATE customer SET birthday=?, msisdn=?, email=?, job=?, address=?, twillio_sid=?, twillio_token=? WHERE username=?";

        try (Connection conn = PSQL.getConnection();
             PreparedStatement pst = conn.prepareStatement(updateQuery)) {

            pst.setDate(1, birthday); // استخدام java.sql.Date بدلاً من String
            pst.setString(2, msisdn);
            pst.setString(3, email);
            pst.setString(4, job);
            pst.setString(5, address);
            pst.setString(6, twillio_sid);
            pst.setString(7, twillio_token);
            pst.setString(8, username);

            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                response.sendRedirect(request.getContextPath() + "/UserProfileServlet");
            } else {
                response.getWriter().write("Error updating profile.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database update error: " + e.getMessage());
        }
    }
}
