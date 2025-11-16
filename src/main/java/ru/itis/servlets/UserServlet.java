package ru.itis.servlets;

import ru.itis.entities.User;
import ru.itis.entities.UserRole;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.services.impl.RoleServiceImpl;
import ru.itis.services.impl.UserRoleServiceImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.RoleService;
import ru.itis.services.interfaces.UserRoleService;
import ru.itis.services.interfaces.UserService;

import javax.servlet.ServletContext;
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

    private UserService userService;
    private UserRoleService userRoleService;
    private RoleService roleService;

    private Map<String, Object> getServices(ServletContext servletContext) {
        return (Map<String, Object>) servletContext.getAttribute("services");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        Map<String, Object> services = getServices(getServletContext());
        if (services == null) {
            throw new RuntimeException("Services not initialized in context");
        }

        userService = (UserService) services.get("userService");
        userRoleService = (UserRoleService) services.get("userRoleService");
        roleService = (RoleService) services.get("roleService");

        if (userService == null || userRoleService == null || roleService == null) {
            throw new RuntimeException("User services failed to initialize");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/users".equals(path)) {
            List<User> users = userService.getAllUsers();
            Map<Long, String> roleMap = new HashMap<>();
            for (User user : users) {
                Long userId = user.getUserId();
                String roleName;
                if (userRoleService.isAdmin(userId)) {
                    roleName = "ADMIN";
                } else if (userRoleService.isManager(userId)) {
                    roleName = "MANAGER";
                } else if (userRoleService.isDeveloper(userId)) {
                    roleName = "DEVELOPER";
                } else if (userRoleService.isTester(userId)) {
                    roleName = "TESTER";
                } else {
                    roleName = "Not assigned";
                }
                roleMap.put(userId, roleName);
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
                List<UserRole> userRoles = userRoleService.findByUserId(id);
                for (UserRole ur : userRoles) {
                    userRoleService.delete(ur.getUserId(), ur.getRoleId());
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
