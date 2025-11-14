package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.entities.User;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.exceptions.PermissionDeniedException;
import ru.itis.exceptions.ProjectNotEmptyException;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.impl.CommentServiceImpl;
import ru.itis.services.impl.ProjectServiceImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.CommentService;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

//TODO: имена не отображаются - пофиксить. УДАЛЕНИЕ, СОХРАНЕНИЕ

@WebServlet(name = "ProjectServlet", urlPatterns = {"/projects", "/project/*"})
public class ProjectServlet extends HttpServlet {
    private final ProjectService projectService = new ProjectServiceImpl(new ProjectRepositoryJdbcImpl(), new SprintRepositoryJdbcImpl(), new TaskRepositoryJdbcImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private static final List<String> STATUSES = Arrays.asList("активен", "на паузе", "завершён");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.equals(req.getContextPath() + "/projects") || uri.equals("/projects")) {
            List<Project> list = projectService.getAll();
            req.setAttribute("projects", list);
            req.getRequestDispatcher("/WEB-INF/jsp/project/projects.jsp").forward(req, resp);
        }

        if (uri.equals(req.getContextPath() + "/project/new") || uri.equals("/project/new")) {
            req.setAttribute("statuses", STATUSES);
            req.setAttribute("managers", userService.getAllManagers());
            req.getRequestDispatcher("/WEB-INF/jsp/project/projectNewForm.jsp").forward(req, resp);
        }

        if (uri.startsWith(req.getContextPath() + "/project/edit") || uri.startsWith("/project/edit")) {
            long id = Long.parseLong(req.getParameter("id"));
            Project project = projectService.getById(id);

            req.setAttribute("projectId", project.getProjectId());
            req.setAttribute("projectName", project.getName());
            req.setAttribute("projectDescription", project.getDescription());
            req.setAttribute("projectStartDate", project.getStartDate());
            req.setAttribute("projectEndDate", project.getEndDate());
            req.setAttribute("projectStatus", project.getStatus());
            req.setAttribute("projectManagerId", project.getManagerId());
            req.setAttribute("statuses", STATUSES);
            req.setAttribute("managers", userService.getAllManagers());

            req.getRequestDispatcher("/WEB-INF/jsp/project/projectEditForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.equals(req.getContextPath() + "/projects") || uri.equals("/projects")) {
            // Имена менеджеров
            List<Project> list = projectService.getAll();
            Map<Long, String> managerNames = new HashMap<>();
            for (Project project : list) {
                Long managerId = project.getManagerId();
                Optional<User> managerOpt = userService.getUserById(managerId);
                // Если менеджера нет, подставляем "-"
                String managerName = managerOpt.map(User::getName).orElse("-");
                managerNames.put(managerId, managerName);
            }
            req.setAttribute("projects", list);
            req.setAttribute("managerNames", managerNames);

            req.getRequestDispatcher("/WEB-INF/jsp/project/projects.jsp").forward(req, resp);
            return;
        }

        if (uri.equals(req.getContextPath() + "/project/update") || uri.equals("/project/update")) {
            Project project = new Project();
            project.setProjectId(Long.parseLong(req.getParameter("id")));
            project.setName(req.getParameter("name"));
            project.setDescription(req.getParameter("description"));
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                project.setStartDate(sdf.parse(req.getParameter("startDate")));
                project.setEndDate(sdf.parse(req.getParameter("endDate")));
            } catch (Exception e) {
                throw new ServletException("Ошибка парсинга даты", e);
            }
            project.setStatus(req.getParameter("status"));
            project.setManagerId(Long.valueOf(req.getParameter("managerId")));

            projectService.update(project);

            resp.sendRedirect(req.getContextPath() + "/projects");
            return;
        }

        if (uri.equals(req.getContextPath() + "/project/new") || uri.equals("/project/new")) {
            Project project = new Project();
            project.setName(req.getParameter("name"));
            project.setDescription(req.getParameter("description"));
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                project.setStartDate(sdf.parse(req.getParameter("startDate")));
                project.setEndDate(sdf.parse(req.getParameter("endDate")));
            } catch (Exception e) {
                throw new ServletException("Ошибка парсинга даты", e);
            }
            project.setStatus(req.getParameter("status"));
            project.setManagerId(Long.valueOf(req.getParameter("managerId")));

            projectService.create(project);

            resp.sendRedirect(req.getContextPath() + "/projects");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        try {
            String[] parts = uri.split("/");
            long id = Long.parseLong(parts[parts.length - 1]);

            projectService.delete(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (ProjectNotEmptyException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ошибка: проект не найден");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        String[] parts = uri.split("/");
        long id = Long.parseLong(parts[parts.length - 1]);

        Project updated = new Project();
        updated.setName(req.getParameter("name"));
        updated.setDescription(req.getParameter("description"));
        updated.setStartDate(Date.valueOf(req.getParameter("startDate")));
        updated.setEndDate(Date.valueOf(req.getParameter("endDate")));
        updated.setStatus(req.getParameter("status"));
        updated.setManagerId(Long.parseLong(req.getParameter("managerId")));

        try {
            projectService.update(updated);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (EntityNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ошибка: проект не найден");
        } catch (InvalidDataException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные данные: " + e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка обновления: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/projects");
    }
}
