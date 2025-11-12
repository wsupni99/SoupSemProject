package ru.itis.servlets;

import ru.itis.entities.Sprint;
import ru.itis.repositories.jdbc.SprintRepositoryJdbcImpl;
import ru.itis.services.interfaces.SprintService;
import ru.itis.services.impl.SprintServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "SprintServlet", urlPatterns = {"/sprints", "/sprint/*"})
public class SprintServlet extends HttpServlet {
    private final SprintService sprintService = new SprintServiceImpl(new SprintRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/sprints")) {
            req.setAttribute("sprints", sprintService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/sprints.jsp").forward(req, resp);
        } else if (uri.matches("/sprint/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            req.setAttribute("sprint", sprintService.getById(id));
            req.getRequestDispatcher("/WEB-INF/jsp/sprint.jsp").forward(req, resp);
        } else if (uri.equals("/sprint/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/sprintForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/sprints")) {
            Sprint sprint = new Sprint();
            sprint.setName(req.getParameter("name"));
            sprint.setProjectId(Long.parseLong(req.getParameter("project_id")));
            sprint.setStartDate(parseDate(req.getParameter("start_date")));
            sprint.setEndDate(parseDate(req.getParameter("end_date")));
            sprintService.create(sprint);
            resp.sendRedirect("/sprints");
        }
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return java.sql.Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.matches("/sprint/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            sprintService.delete(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
