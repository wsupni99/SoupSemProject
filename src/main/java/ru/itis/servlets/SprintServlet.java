package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.entities.Sprint;
import ru.itis.exceptions.EntityNotEmptyException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.SprintService;
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
            req.getRequestDispatcher("/jsp/sprint/sprintNewForm.jsp").forward(req, resp);
            return;
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
            req.getRequestDispatcher("/jsp/sprint/sprints.jsp").forward(req, resp);
            return;
        }

        if ("/sprint/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
                return;
            }
            try {
                Long id = Long.parseLong(idStr.trim());
                Sprint sprint = sprintService.getById(id);
                List<Project> projects = projectService.getAll();
                req.setAttribute("sprint", sprint);
                req.setAttribute("projects", projects);
                req.getRequestDispatcher("/jsp/sprint/sprintEditForm.jsp").forward(req, resp);
                return;
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
                return;
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/jsp/error/404.jsp").forward(req, resp);
                return;
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/jsp/error/500.jsp").forward(req, resp);
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req.getRequestDispatcher("/jsp/error/404.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/sprint/create".equals(path)) {
            String name = req.getParameter("name");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String projectIdStr = req.getParameter("projectId");

            if (name == null || name.trim().isEmpty()
                    || startDateStr == null || startDateStr.trim().isEmpty()
                    || endDateStr == null || endDateStr.trim().isEmpty()
                    || projectIdStr == null || projectIdStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
                return;
            }

            try {
                Sprint sprint = new Sprint();
                sprint.setName(name.trim());
                sprint.setStartDate(Date.valueOf(startDateStr.trim()));
                sprint.setEndDate(Date.valueOf(endDateStr.trim()));
                sprint.setProjectId(Long.valueOf(projectIdStr.trim()));
                sprintService.create(sprint);
                resp.sendRedirect(req.getContextPath() + "/sprints");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/jsp/error/500.jsp").forward(req, resp);
            }
            return;
        }

        if ("/sprint/update".equals(path)) {
            String sprintIdStr = req.getParameter("sprintId");
            String name = req.getParameter("name");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String projectIdStr = req.getParameter("projectId");

            if (sprintIdStr == null || sprintIdStr.trim().isEmpty()
                    || name == null || name.trim().isEmpty()
                    || startDateStr == null || startDateStr.trim().isEmpty()
                    || endDateStr == null || endDateStr.trim().isEmpty()
                    || projectIdStr == null || projectIdStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
                return;
            }

            try {
                Sprint sprint = new Sprint();
                sprint.setSprintId(Long.valueOf(sprintIdStr.trim()));
                sprint.setName(name.trim());
                sprint.setStartDate(Date.valueOf(startDateStr.trim()));
                sprint.setEndDate(Date.valueOf(endDateStr.trim()));
                sprint.setProjectId(Long.valueOf(projectIdStr.trim()));
                sprintService.update(sprint);
                resp.sendRedirect(req.getContextPath() + "/sprints");
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/jsp/error/404.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/jsp/error/500.jsp").forward(req, resp);
            }
            return;
        }

        if ("/sprint/delete".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
                return;
            }
            try {
                Long sprintId = Long.parseLong(idParam.trim());
                sprintService.delete(sprintId);
                resp.sendRedirect(req.getContextPath() + "/sprints");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/jsp/error/400.jsp").forward(req, resp);
            } catch (EntityNotEmptyException e) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                req.getRequestDispatcher("/jsp/error/500.jsp").forward(req, resp);
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                req.getRequestDispatcher("/jsp/error/404.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                req.getRequestDispatcher("/jsp/error/500.jsp").forward(req, resp);
            }
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req.getRequestDispatcher("/jsp/error/404.jsp").forward(req, resp);
    }
}
