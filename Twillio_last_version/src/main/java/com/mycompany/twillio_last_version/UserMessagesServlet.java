package com.mycompany.twillio_last_version;

import javax.servlet.ServletException;  
import javax.servlet.annotation.WebServlet;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.io.IOException;  
import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.List;  

@WebServlet("/UserMessagesServlet")  
public class UserMessagesServlet extends HttpServlet {  

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        String action = request.getParameter("action");  
        String username = request.getParameter("username"); // Assumed to be passed from the form  
       
        switch (action) {  
            case "storeMessage":  
                storeMessage(username, request, response);  
                break;  

            case "deleteMessage":  
                deleteMessage(request, response);  
                break;  

            case "fetchMessages":  
               fetchMessages(username, request, response);  
                break;  

           default:  
               request.setAttribute("errorMessage", "Invalid action");  
                response.sendRedirect("pages/inbox.html");  
                break;  
        }  
    }  

    private void storeMessage(String username, HttpServletRequest request, HttpServletResponse response) throws IOException {  
        String message = request.getParameter("message");  
        String sql = "INSERT INTO sms (customer_username, message) VALUES (?, ?)";  
        
        try (Connection connection = PSQL.getConnection();  
            PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, username);  
            statement.setString(2, message);  
            statement.executeUpdate();  
            request.setAttribute("messageStored", true);  
        } catch (SQLException e) {  
            e.printStackTrace();  
            request.setAttribute("messageStored", false);  
        }  
       
        response.sendRedirect("pages/send_Messages.html");  
    }  

    private void deleteMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        int messageIdToDelete = Integer.parseInt(request.getParameter("messageId"));  
        String sql = "DELETE FROM sms WHERE id = ?";  
        
        try (Connection connection = PSQL.getConnection();  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setInt(1, messageIdToDelete);  
            statement.executeUpdate();  
            request.setAttribute("messageDeleted", true);  
        } catch (SQLException e) {  
            e.printStackTrace();  
            request.setAttribute("messageDeleted", false);  
        }  
        
        response.sendRedirect("pages/manage_customer.html");  
    }  

    private void fetchMessages(String username, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {  
        List<String> messages = new ArrayList<>();  
        String sql = "SELECT message FROM messages WHERE customer_username = ?";  
        
        try (Connection connection = PSQL.getConnection();  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, username);  
            ResultSet resultSet = statement.executeQuery();  
            while (resultSet.next()) {  
               messages.add(resultSet.getString("message"));  
            }  
           request.setAttribute("messages", messages);  
            request.getRequestDispatcher("pages/message_history.html").forward(request, response); // Forward to history page  
       } catch (SQLException e) {  
           e.printStackTrace();  
       }  
    }  
}  