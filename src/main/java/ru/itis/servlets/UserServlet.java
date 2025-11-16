package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.services.impl.RoleServiceImpl;
import ru.itis.services.impl.UserRoleServiceImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.RoleService;
import ru.itis.services.interfaces.UserRoleService;
import ru.itis.services.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({
        "/users",
        "/user/delete"
})
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private final RoleService roleService = new RoleServiceImpl(new RoleRepositoryJdbcImpl());
    private final UserRoleService userRoleService = new UserRoleServiceImpl(new UserRoleRepositoryJdbcImpl(), new RoleRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/users".equals(path)) {
            List<User> users = userService.getAllUsers();
            Map<Long, String> roleMap = new HashMap<>();
            for (User user : users) {
                String roleName = userRoleService.getRoleNameByUserId(user.getUserId());
                roleMap.put(user.getUserId(), roleName != null ? roleName : "Не назначено");
            }
            req.setAttribute("users", users);
            req.setAttribute("roleMap", roleMap);
            req.getRequestDispatcher("/WEB-INF/jsp/user/users.jsp").forward(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/user/delete".equals(path)) {
            resp.setContentType("application/json; charset=UTF-8");
            try {
                String idStr = req.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("User ID not provided");
                }
                Long id = Long.parseLong(idStr);
                String currentRole = userRoleService.getRoleNameByUserId(id);
                if (currentRole != null && !"Не назначено".equals(currentRole)) {
                    Long roleId = roleService.getRoleIdByName(currentRole).orElse(null);
                    if (roleId != null) {
                        userRoleService.delete(id, roleId);
                    }
                }
                userService.deleteUser(id);
                resp.getWriter().write("{\"success\": true}");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid ID format\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"message\": \"An error occurred while deleting the user. Please try again.\"}");
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
