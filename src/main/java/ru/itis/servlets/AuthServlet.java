package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.services.impl.UserRoleServiceImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.UserRoleService;
import ru.itis.services.interfaces.UserService;
import ru.itis.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthServlet", urlPatterns = "/login")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private final UserRoleService userRoleService = new UserRoleServiceImpl(new UserRoleRepositoryJdbcImpl(), new RoleRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Email and password are required");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
            return;
        }

        Optional<User> userOpt = userService.getByEmail(email.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.matches(password.trim(), user.getPasswordHash())) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }

        req.setAttribute("error", "Invalid email or password");
        req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
    }
}
