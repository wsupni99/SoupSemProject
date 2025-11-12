package ru.itis.servlets;

import ru.itis.entities.WorkloadSummary;
import ru.itis.repositories.jdbc.TaskRepositoryJdbcImpl;
import ru.itis.services.interfaces.WorkloadSummaryService;
import ru.itis.services.impl.WorkloadSummaryServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "WorkloadSummaryServlet", urlPatterns = {"/workload"})
public class WorkloadSummaryServlet extends HttpServlet {
    private final WorkloadSummaryService workloadSummaryService =
            new WorkloadSummaryServiceImpl(new TaskRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        // Если пользак не залогинен
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Long projectId = getLong(req.getParameter("project_id"));
        Long sprintId = getLong(req.getParameter("sprint_id"));
        WorkloadSummary summary = workloadSummaryService.buildSummary(userId, projectId, sprintId);
        req.setAttribute("summary", summary);
        req.getRequestDispatcher("/WEB-INF/jsp/workload.jsp").forward(req, resp);
    }

    private Long getLong(String value) {
        if (value == null || value.isEmpty()) return null;
        return Long.parseLong(value);
    }
}
