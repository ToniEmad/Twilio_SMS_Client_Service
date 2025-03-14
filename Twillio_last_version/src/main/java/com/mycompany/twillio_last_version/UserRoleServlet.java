package com.mycompany.twillio_last_version;

//package newpackage;
//
//import javax.servlet.ServletException;  
//import javax.servlet.annotation.WebServlet;  
//import javax.servlet.http.HttpServlet;  
//import javax.servlet.http.HttpServletRequest;  
//import javax.servlet.http.HttpServletResponse;  
//import java.io.IOException;  
//
//@WebServlet("/UserRoleServlet")  
//public class UserRoleServlet extends HttpServlet {  
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
//        String username = request.getParameter("username");  
//        String password = request.getParameter("password");  
//
//        UserDAO userDao = new UserDAO();  
//        
//        // First, authenticate the user  
//        if (userDao.authenticateUser(username, password)) {  
//            // If authenticated, then check the role  
//            String role = userDao.checkUserRole(username);  
//            if (role != null) {  
//                if (role.equals("administrator")) {  
//                    response.sendRedirect("/pages/admin.html"); // Redirect to admin dashboard  
//                } else if (role.equals("customer")) {  
//                    response.sendRedirect("/pages/customer.html"); // Redirect to customer area  
//                }  
//            } else {  
//                request.setAttribute("errorMessage", "Role not found");  
//                request.getRequestDispatcher("/pages/login.html").forward(request, response);  
//            }  
//        } else {  
//            request.setAttribute("errorMessage", "Invalid username or password");  
//            request.getRequestDispatcher("/pages/login.html").forward(request, response);  
//        }  
//    }  
//}  