package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.entities.User;
import ru.itis.entities.UserRole;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.RoleService;
import ru.itis.services.interfaces.UserService;
import ru.itis.services.interfaces.UserRoleService;
import ru.itis.util.PasswordUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private UserService userService;
    private UserRoleService userRoleService;
    private RoleService roleService;
    private ProjectService projectService;

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
        projectService = (ProjectService) services.get("projectService");

        if (userService == null || userRoleService == null || roleService == null || projectService == null) {
            throw new RuntimeException("Register services failed to initialize");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Project> projects = projectService.getAll();
        req.setAttribute("projects", projects);
        req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email").trim();
        String name = req.getParameter("name").trim();
        String password = req.getParameter("password");
        String roleName = req.getParameter("role");
        if (roleName != null) {
            roleName = roleName.trim();
        }
        String projectIdStr = req.getParameter("projectId");

        if (roleName == null || roleName.isEmpty()) {
            req.setAttribute("error", "Select a role");
            List<Project> projects = projectService.getAll();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
            return;
        }

        if ("Менеджер".equals(roleName)) {
            if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
                req.setAttribute("error", "For manager, select a project");
                List<Project> projects = projectService.getAll();
                req.setAttribute("projects", projects);
                req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
                return;
            }
            try {
                Long.parseLong(projectIdStr.trim());
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Invalid project ID");
                List<Project> projects = projectService.getAll();
                req.setAttribute("projects", projects);
                req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
                return;
            }
        }

        try {
            if (userService.getByEmail(email).isPresent()) {
                req.setAttribute("error", "Email already registered");
                List<Project> projects = projectService.getAll();
                req.setAttribute("projects", projects);
                req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
                return;
            }

            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPasswordHash(PasswordUtil.hash(password));
            user.setContactInfo("");
            userService.createUser(user);

            Optional<Long> roleIdOpt = roleService.getRoleIdByName(roleName);
            if (!   roleIdOpt.isPresent()) {
                req.setAttribute("error", "Invalid role: " + roleName);
                List<Project> projects = projectService.getAll();
                req.setAttribute("projects", projects);
                req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
                return;
            }
            Long roleId = roleIdOpt.get();

            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            userRoleService.create(userRole);

            if ("Менеджер".equals(roleName)) {
                long projectId = Long.parseLong(projectIdStr.trim());
                Project project;
                try {
                    project = projectService.getById(projectId);
                } catch (EntityNotFoundException e) {
                    req.setAttribute("error", "Project not found");
                    List<Project> projects = projectService.getAll();
                    req.setAttribute("projects", projects);
                    req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
                    return;
                }
                project.setManagerId(user.getUserId());
                projectService.update(project);
            }

            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            req.setAttribute("error", "Registration error: " + e.getMessage());
            List<Project> projects = projectService.getAll();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
        }
    }


}

