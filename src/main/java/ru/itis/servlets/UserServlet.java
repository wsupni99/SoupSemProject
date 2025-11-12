package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.services.interfaces.UserRoleService;
import ru.itis.services.interfaces.UserService;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.impl.UserRoleServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/user/*"})
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private final UserRoleService userRoleService = new UserRoleServiceImpl(
            new UserRoleRepositoryJdbcImpl(), new RoleRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        User currentUser = (User) req.getSession().getAttribute("user");
        try {
            if (uri.equals("/users") && currentUser != null && userRoleService.isAdmin(currentUser.getUserId())) {
                req.setAttribute("users", userService.getAllUsers());
                req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
            } else if (uri.matches("/user/\\d+")) {
                Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
                req.setAttribute("profile",
                        userService.getUserById(id).orElseThrow(() ->
                                new EntityNotFoundException("User not found")));
                req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
            }
        } catch (EntityNotFoundException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.matches("/user/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            try {
                User user = userService.getUserById(id).
                        orElseThrow(() ->
                                new EntityNotFoundException("User not found"));
                // обновить user из request
                userService.updateUser(user);
                resp.sendRedirect("/user/" + id);
            } catch (EntityNotFoundException e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
            }
        }
    }
}
