package com.disasterrelief.servlet;

import com.disasterrelief.exception.DisasterReliefException;
import com.disasterrelief.model.User;
import com.disasterrelief.service.AuthService;
import com.disasterrelief.thread.ThreadManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    public void init() throws ServletException {
        super.init();
        ThreadManager.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/logout".equalsIgnoreCase(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=logged_out");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            resp.sendRedirect(req.getContextPath() + "/" + user.getDashboardRoute());
            return;
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = authService.login(email, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());

            resp.sendRedirect(req.getContextPath() + "/" + user.getDashboardRoute());
        } catch (DisasterReliefException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
