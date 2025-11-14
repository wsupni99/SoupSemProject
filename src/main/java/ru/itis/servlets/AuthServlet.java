package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.services.interfaces.UserService;
import ru.itis.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith("/login")) {
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
        } else if (uri.endsWith("/logout")) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            Optional<User> userOpt = userService.getByEmail(login);
            if (userOpt.isPresent() && PasswordUtil.matches(password, userOpt.get().getPasswordHash())) {
                req.getSession().setAttribute("user", userOpt.get());
                resp.sendRedirect(req.getContextPath() + "/projects");
            } else {
                req.setAttribute("error", "Invalid username or password");
                req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
            }
        } catch (EntityNotFoundException e) {
            req.setAttribute("error", "The user was not found");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
        }
    }
}
