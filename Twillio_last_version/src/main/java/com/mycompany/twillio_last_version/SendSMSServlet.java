/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.twillio_last_version;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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

@WebServlet("/SendSMSServlet")
public class SendSMSServlet extends HttpServlet {
    private static final String ACCOUNT_SID = "your_twilio_sid";
    private static final String AUTH_TOKEN = "your_twilio_auth_token";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fromMsisdn = request.getParameter("from_msisdn");
        String toMsisdn = request.getParameter("to_msisdn");
        String messageBody = request.getParameter("message");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");

        // إرسال الرسالة عبر Twilio
        SMS smsService = new SMS(ACCOUNT_SID, AUTH_TOKEN);
        smsService.sendSms(toMsisdn, fromMsisdn, messageBody);

        // حفظ تفاصيل الرسالة في قاعدة البيانات
        try (Connection conn = PSQL.getConnection()) {
            String sql = "INSERT INTO sms (from_msisdn, to_msisdn, body, status, customer_id) VALUES (?, ?, ?, ?, (SELECT customer_id FROM customer WHERE username = ?))";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, fromMsisdn);
                pst.setString(2, toMsisdn);
                pst.setString(3, messageBody);
                pst.setString(4, "SENT");
                pst.setString(5, username);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            return;
        }

        response.sendRedirect("message_history.html");
    }
}
