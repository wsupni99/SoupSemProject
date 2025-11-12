package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.services.interfaces.UserService;
import ru.itis.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        try {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPasswordHash(PasswordUtil.hash(password)); // пароль захэширован!
            userService.createUser(user);
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            req.setAttribute("error", "Registration Error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}
