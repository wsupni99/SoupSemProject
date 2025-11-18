package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.EntityNotEmptyException;
import ru.itis.services.interfaces.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@WebServlet({
        "/projects",
        "/project/new",
        "/project/create",
        "/project/edit",
        "/project/update",
        "/project/delete"
})
public class ProjectServlet extends HttpServlet {

    private ProjectService projectService;
    private SprintService sprintService;
    private TaskService taskService;
    private UserRoleService userRoleService;
    private UserService userService;

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

        projectService = (ProjectService) services.get("projectService");
        sprintService = (SprintService) services.get("sprintService");
        taskService = (TaskService) services.get("taskService");
        userRoleService = (UserRoleService) services.get("userRoleService");
        userService = (UserService) services.get("userService");

        if (projectService == null || sprintService == null || taskService == null || userRoleService == null) {
            throw new RuntimeException("Project services failed to initialize");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if ("/projects".equals(path)) {
            List<Project> list = projectService.getAll();
            Map<Long, String> managerNames = new HashMap<>();
            for (Project project : list) {
                Long managerId = project.getManagerId();
                userService.getUserById(managerId).ifPresent(user -> managerNames.put(managerId, user.getName()));
            }
            req.setAttribute("projects", list);
            req.setAttribute("managerNames", managerNames);
            req.getRequestDispatcher("/WEB-INF/jsp/project/projects.jsp").forward(req, resp);
            return;
        }
        if ("/project/new".equals(path)) {
            req.setAttribute("managers", userService.getAllManagers());
            req.getRequestDispatcher("/WEB-INF/jsp/project/projectNewForm.jsp").forward(req, resp);
            return;
        }
        if ("/project/edit".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Project ID not provided");
                return;
            }
            try {
                long id = Long.parseLong(idParam);
                Project project = projectService.getById(id);
                req.setAttribute("projectId", project.getProjectId());
                req.setAttribute("projectName", project.getName());
                req.setAttribute("projectDescription", project.getDescription());
                req.setAttribute("projectStartDate", project.getStartDate() != null ? project.getStartDate().toString() : "");
                req.setAttribute("projectEndDate", project.getEndDate() != null ? project.getEndDate().toString() : "");
                req.setAttribute("projectStatus", project.getStatus());
                req.setAttribute("projectManagerId", project.getManagerId());
                req.setAttribute("managers", userService.getAllManagers());
                req.getRequestDispatcher("/WEB-INF/jsp/project/projectEditForm.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID format");
            } catch (EntityNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found");
            }
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if ("/project/create".equals(path)) {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String status = req.getParameter("status");
            String managerIdParam = req.getParameter("managerId");
            if (name == null || name.trim().isEmpty() || startDateStr == null || startDateStr.trim().isEmpty() ||
                    endDateStr == null || endDateStr.trim().isEmpty() || status == null || status.trim().isEmpty() ||
                    managerIdParam == null || managerIdParam.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required fields not provided");
                return;
            }
            try {
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);
                long managerId = Long.parseLong(managerIdParam);
                Project project = new Project();
                project.setName(name.trim());
                project.setDescription(description != null ? description.trim() : "");
                project.setStartDate(startDate);
                project.setEndDate(endDate);
                project.setStatus(status.trim());
                project.setManagerId(managerId);
                projectService.create(project);
                resp.sendRedirect(req.getContextPath() + "/projects");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Creation failed: " + e.getMessage());
            }
            return;
        }
        if ("/project/update".equals(path)) {
            String idParam = req.getParameter("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String status = req.getParameter("status");
            String managerIdParam = req.getParameter("managerId");
            if (idParam == null || idParam.trim().isEmpty() || name == null || name.trim().isEmpty() ||
                    startDateStr == null || startDateStr.trim().isEmpty() || endDateStr == null || endDateStr.trim().isEmpty() ||
                    status == null || status.trim().isEmpty() || managerIdParam == null || managerIdParam.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required fields not provided");
                return;
            }
            try {
                long id = Long.parseLong(idParam);
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);
                long managerId = Long.parseLong(managerIdParam);
                Project project = new Project();
                project.setProjectId(id);
                project.setName(name.trim());
                project.setDescription(description != null ? description.trim() : "");
                project.setStartDate(startDate);
                project.setEndDate(endDate);
                project.setStatus(status.trim());
                project.setManagerId(managerId);
                projectService.update(project);
                resp.sendRedirect(req.getContextPath() + "/projects");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Update failed: " + e.getMessage());
            }
            return;
        }
        if ("/project/delete".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Project ID not provided");
                return;
            }
            try {
                long id = Long.parseLong(idParam);
                projectService.delete(id);
                resp.sendRedirect(req.getContextPath() + "/projects");
            } catch (EntityNotEmptyException e) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("Cannot delete project if sprints or tasks are attached to it");
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Project not found");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Project deletion error");
            }
            return;
        }
        if ("/projects".equals(path)) {
            List<Project> list = projectService.getAll();
            Map<Long, String> managerNames = new HashMap<>();
            for (Project project : list) {
                Long managerId = project.getManagerId();
                userService.getUserById(managerId).ifPresent(user -> managerNames.put(managerId, user.getName()));
            }
            req.setAttribute("projects", list);
            req.setAttribute("managerNames", managerNames);
            req.getRequestDispatcher("/WEB-INF/jsp/project/projects.jsp").forward(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
