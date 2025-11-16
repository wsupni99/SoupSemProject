package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/user/*"})
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/users".equals(path)) {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/jsp/user/users.jsp").forward(req, resp);
            return;
        }

        if (path.startsWith("/user/")) {
            String idStr = path.substring(path.lastIndexOf("/") + 1);
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID not provided");
                return;
            }
            try {
                Long id = Long.parseLong(idStr);
                User user = userService.getUserById(id).orElseThrow(
                        () -> new EntityNotFoundException("User not found"));
                req.setAttribute("user", user);
                req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
                return;
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
            } catch (EntityNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if (path.startsWith("/user/")) {
            String idStr = path.substring(path.lastIndexOf("/") + 1);
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("User ID not provided");
                return;
            }

            try {
                Long id = Long.parseLong(idStr);
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Required fields not provided");
                    return;
                }

                User user = new User();
                user.setUserId(id);
                user.setName(name.trim());
                user.setEmail(email.trim());
                userService.updateUser(user);
                resp.sendRedirect(req.getContextPath() + "/user/" + id);
                return;
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid user ID format");
            } catch (EntityNotFoundException | InvalidDataException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Update failed: " + e.getMessage());
            }
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
