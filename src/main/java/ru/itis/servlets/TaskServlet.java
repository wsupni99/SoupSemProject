package ru.itis.servlets;

import ru.itis.entities.Task;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.PermissionDeniedException;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.repositories.jdbc.TaskRepositoryJdbcImpl;
import ru.itis.services.interfaces.TaskService;
import ru.itis.services.impl.TaskServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TaskServlet", urlPatterns = {"/tasks", "/task/*"})
public class TaskServlet extends HttpServlet {
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/tasks")) {
            List<Task> tasks = taskService.getFilteredTasks(req.getParameterMap());
            req.setAttribute("tasks", tasks);
            req.getRequestDispatcher("/WEB-INF/jsp/tasks.jsp").forward(req, resp);
        } else if (uri.matches("/task/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            req.setAttribute("task", taskService.getById(id));
            req.getRequestDispatcher("/WEB-INF/jsp/task.jsp").forward(req, resp);
        } else if (uri.equals("/task/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/taskForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        try {
            if (uri.equals("/tasks")) {
                Task task = new Task();
                // заполнить task из request
                taskService.create(task);
                resp.sendRedirect("/tasks");
            } else if (uri.matches("/task/\\d+")) {
                Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
                Task updated = taskService.getById(id);
                // обновить updated из request
                taskService.update(id, updated);
                resp.sendRedirect("/tasks");
            }
        } catch (EntityNotFoundException | InvalidDataException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.matches("/task/\\d+")) {
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            try {
                taskService.delete(id);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (EntityNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (PermissionDeniedException e) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
