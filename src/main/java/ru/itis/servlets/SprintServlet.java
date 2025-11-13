package ru.itis.servlets;

import ru.itis.entities.Sprint;

import ru.itis.repositories.jdbc.ProjectRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.SprintRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.TaskRepositoryJdbcImpl;
import ru.itis.services.impl.ProjectServiceImpl;
import ru.itis.services.interfaces.SprintService;
import ru.itis.services.impl.SprintServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/sprints")
public class SprintServlet extends HttpServlet {
    SprintService sprintService = new SprintServiceImpl(
            new SprintRepositoryJdbcImpl(),
            new ProjectServiceImpl(
                    new ProjectRepositoryJdbcImpl(),
                    new SprintRepositoryJdbcImpl(),
                    new TaskRepositoryJdbcImpl()
            )
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Sprint> sprints = sprintService.getAllSprints();
        Map<Long, String> projectNames = new HashMap<>();

        for (Sprint sprint : sprints) {
            Long sprintId = sprint.getSprintId();
            String projectName = sprintService.getProjectNameBySprintId(sprintId);
            projectNames.put(sprintId, projectName);
        }

        req.setAttribute("sprints", sprints);
        req.setAttribute("projectNames", projectNames);
        req.getRequestDispatcher("/WEB-INF/jsp/sprints.jsp").forward(req, resp);
    }
}
