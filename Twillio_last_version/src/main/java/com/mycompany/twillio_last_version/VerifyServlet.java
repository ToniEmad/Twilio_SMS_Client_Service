package com.mycompany.twillio_last_version;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/VerifyServlet"})
public class VerifyServlet extends HttpServlet {
   public VerifyServlet() {
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      String msisdn = (String) session.getAttribute("msisdn");
      
      if (msisdn == null || msisdn.isEmpty()) {
         response.sendRedirect(request.getContextPath() + "/verify.html?error=Phone number is required");
         return;
      }
      
      String ACCOUNT_SID = "AC00000000000000000000000000000000";
      String AUTH_TOKEN = "00000000000000000000000000000000";
      String TWILIO_NUMBER = "+201000000000";
      Random random = new Random();
      int verificationCode = random.nextInt(9000) + 1000;

      try {
         Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
         Message.creator(new PhoneNumber("+2" + msisdn), new PhoneNumber(TWILIO_NUMBER), "Your TSMS verification code is: " + verificationCode).create();
         session.setAttribute("verification_code", String.valueOf(verificationCode));
         response.sendRedirect(request.getContextPath() + "/verify.html?phone=" + msisdn);
      } catch (Exception e) {
         e.printStackTrace();
         response.sendRedirect(request.getContextPath() + "/verify.html?error=Failed to send verification code");
      }
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      String savedCode = (String) session.getAttribute("verification_code");
      String msisdn = (String) session.getAttribute("msisdn");
      String enteredCode = request.getParameter("code");
      String resend = request.getParameter("resend");

      if (resend != null && resend.equals("true")) {
         doPost(request, response);
         return;
      }

      if (savedCode == null || msisdn == null) {
         response.sendRedirect(request.getContextPath() + "/verify.html?error=Session expired. Please request a new code.");
         return;
      }

      if (savedCode.equals(enteredCode)) {
         try (Connection conn = PSQL.getConnection()) {
            String sql = "UPDATE customer SET sms_validate = TRUE WHERE msisdn = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
               pst.setString(1, msisdn);
               int rowsUpdated = pst.executeUpdate();
               if (rowsUpdated > 0) {
                  session.removeAttribute("verification_code");
                  session.removeAttribute("msisdn");
                  response.sendRedirect(request.getContextPath() + "/user_profile.html");
               } else {
                  response.sendRedirect(request.getContextPath() + "/verify.html?error=User not found");
               }
            }
         } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/verify.html?error=Database error");
         }
      } else {
         response.sendRedirect(request.getContextPath() + "/verify.html?error=Invalid code");
      }
   }
}
