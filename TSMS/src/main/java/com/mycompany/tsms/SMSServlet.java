package com.mycompany.tsms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sms")
public class SMSServlet extends HttpServlet {
 
           
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       PrintWriter out = response.getWriter();
       String phoneNumber = request.getParameter("phoneNumber");
       String message = request.getParameter("message");

       out.println("SMS Servlet");
   }
}

