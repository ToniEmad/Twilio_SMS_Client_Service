package com.mycompany.tsms;

//package newpackage;
//
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import com.mycompany.twillo.SMS;
//
//
//@WebServlet("/TwilioSmsServlet")
//public class TwilioSmsServlet extends HttpServlet {
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String from = request.getParameter("from");
//        String to = request.getParameter("to");
//        String body = request.getParameter("body");
//
//        SMS sms = new SMS("", "");
//        sms.sendSms(to, from, body);
//        response.sendRedirect("/pages/send_Messages.html");   // redirect to the send_Messages.html page
//        request.setAttribute("messageeee", "Message sent successfully"); // set the message attribute to the messageeee variable
//
//    }   
//}
