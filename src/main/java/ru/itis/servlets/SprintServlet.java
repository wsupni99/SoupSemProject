package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.entities.Sprint;

import ru.itis.repositories.jdbc.ProjectRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.SprintRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.TaskRepositoryJdbcImpl;
import ru.itis.services.impl.ProjectServiceImpl;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.SprintService;
import ru.itis.services.impl.SprintServiceImpl;
import ru.itis.services.interfaces.UserRoleService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({
        "/sprints",
        "/sprint/edit",
        "/sprint/update",
        "/sprint/delete",
        "/sprint/new",
        "/sprint/create"
})
public class SprintServlet extends HttpServlet {
    private SprintService sprintService;
    private ProjectService projectService;
    private UserRoleService userRoleService;

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

        sprintService = (SprintService) services.get("sprintService");
        projectService = (ProjectService) services.get("projectService");
        userRoleService = (UserRoleService) services.get("userRoleService");

        if (sprintService == null || projectService == null || userRoleService == null) {
            throw new RuntimeException("Sprint services failed to initialize");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/sprint/new".equals(path)) {
            req.setAttribute("projects", projectService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/sprint/sprintNewForm.jsp").forward(req, resp);
        }

        if ("/sprints".equals(path)) {
            List<Sprint> sprints = sprintService.getAllSprints();
            Map<Long, String> projectNames = new HashMap<>();
            for (Sprint sprint : sprints) {
                Long sprintId = sprint.getSprintId();
                String projectName = sprintService.getProjectNameBySprintId(sprintId);
                projectNames.put(sprintId, projectName);
            }
            req.setAttribute("sprints", sprints);
            req.setAttribute("projectNames", projectNames);
            req.getRequestDispatcher("/WEB-INF/jsp/sprint/sprints.jsp").forward(req, resp);
        }

        if ("/sprint/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                return;
            }
            Long id = Long.parseLong(idStr);
            Sprint sprint = sprintService.getById(id);
            List<Project> projects = projectService.getAll();
            req.setAttribute("sprint", sprint);
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/WEB-INF/jsp/sprint/sprintEditForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/sprint/create".equals(path)) {
            Sprint sprint = new Sprint();
            sprint.setName(req.getParameter("name"));
            sprint.setStartDate(Date.valueOf(req.getParameter("startDate")));
            sprint.setEndDate(Date.valueOf(req.getParameter("endDate")));
            sprint.setProjectId(Long.valueOf(req.getParameter("projectId")));
            sprintService.create(sprint);
            resp.sendRedirect(req.getContextPath() + "/sprints");
        }

        if ("/sprint/update".equals(path)) {
            Sprint sprint = new Sprint();
            sprint.setSprintId(Long.valueOf(req.getParameter("sprintId")));
            sprint.setName(req.getParameter("name"));
            sprint.setStartDate(Date.valueOf(req.getParameter("startDate")));
            sprint.setEndDate(Date.valueOf(req.getParameter("endDate")));
            sprint.setProjectId(Long.valueOf(req.getParameter("projectId")));
            sprintService.update(sprint);
            resp.sendRedirect(req.getContextPath() + "/sprints");
        }
        if ("/sprint/delete".equals(path)) {
            try {
                String idParam = req.getParameter("id");
                if (idParam == null || idParam.trim().isEmpty()) {
                    throw new IllegalArgumentException("Sprint ID not provided");
                }
                Long sprintId = Long.parseLong(idParam);
                sprintService.delete(sprintId);
                resp.sendRedirect(req.getContextPath() + "/sprints");
            } catch (NumberFormatException e) {
                resp.getWriter().write("Invalid ID format");
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg != null && (msg.contains("foreign key") || msg.contains("violates foreign key"))) {
                    resp.getWriter().write("Cannot delete sprint if tasks are attached to it");
                } else {
                    resp.getWriter().write("Sprint deletion error");
                }
            }
        }
    }
}
