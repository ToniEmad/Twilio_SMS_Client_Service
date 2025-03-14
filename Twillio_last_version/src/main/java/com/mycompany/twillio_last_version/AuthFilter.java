/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.twillio_last_version;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author syousrei
 */

@WebFilter("/*") // تطبيق الفلتر على جميع الصفحات
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        // السماح بصفحات تسجيل الدخول والخروج فقط
        String path = req.getRequestURI();
        boolean isPublicPage = path.endsWith("login.html") || path.endsWith("ChkLoginServlet") || path.endsWith("LogoutServlet");

        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // منع الدخول إذا لم يكن هناك جلسة
        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect(req.getContextPath() + "/login.html?error=unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
