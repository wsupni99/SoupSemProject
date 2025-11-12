package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.PermissionDeniedException;
import ru.itis.services.impl.ProjectServiceImpl;
import ru.itis.services.impl.TaskServiceImpl;
import ru.itis.repositories.jdbc.ProjectRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.TaskRepositoryJdbcImpl;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.TaskService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProjectServlet", urlPatterns = {"/projects", "/project/*"})
public class ProjectServlet extends HttpServlet {
    private final ProjectService projectService = new ProjectServiceImpl(new ProjectRepositoryJdbcImpl());
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/projects")) {
            req.setAttribute("projects", projectService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/projects.jsp").forward(req, resp);
        } else if (uri.matches("/project/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            req.setAttribute("project", projectService.getById(id));
            req.setAttribute("tasks", taskService.getByProjectId(id));
            req.getRequestDispatcher("/WEB-INF/jsp/project.jsp").forward(req, resp);
        } else if (uri.equals("/project/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/projectForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/projects")) {
            Project project = new Project();
            project.setName(req.getParameter("name"));
            project.setDescription(req.getParameter("description"));
            projectService.create(project);
            resp.sendRedirect("/projects");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.matches("/project/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            try {
                projectService.delete(id);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (PermissionDeniedException e) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
