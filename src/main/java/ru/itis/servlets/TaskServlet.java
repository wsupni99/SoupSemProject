package ru.itis.servlets;

import ru.itis.entities.*;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.impl.*;
import ru.itis.services.interfaces.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@WebServlet({
        "/tasks",
        "/task",
        "/task/new",
        "/task/newWithProject",
        "/task/edit",
        "/task/delete",
        "/task/addComment"
})
public class TaskServlet extends HttpServlet {
    private final ProjectService projectService = new ProjectServiceImpl(
            new ProjectRepositoryJdbcImpl(), new SprintRepositoryJdbcImpl(), new TaskRepositoryJdbcImpl());
    private final SprintService sprintService = new SprintServiceImpl(new SprintRepositoryJdbcImpl(), projectService);
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryJdbcImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private final UserRoleService userRoleService = new UserRoleServiceImpl(
            new UserRoleRepositoryJdbcImpl(), new RoleRepositoryJdbcImpl());
    private final CommentService commentService = new CommentServiceImpl(new CommentRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Long userId = user.getUserId();
            req.setAttribute("isAdmin", userRoleService.isAdmin(userId));
            req.setAttribute("isManager", userRoleService.isManager(userId));
        }

        if ("/tasks".equals(path)) {
            Map<String, String[]> params = req.getParameterMap();
            List<Task> tasks = taskService.getFilteredTasks(params);

            List<User> users = userService.getAllUsers();
            List<Project> projects = projectService.getAll();
            List<Sprint> sprints = sprintService.getAll();

            req.setAttribute("tasks", tasks);
            req.setAttribute("users", users);
            req.setAttribute("projects", projects);
            req.setAttribute("sprints", sprints);
            req.getRequestDispatcher("/WEB-INF/jsp/task/tasks.jsp").forward(req, resp);
            return;
        }

        if ("/task/new".equals(path)) {
            List<Project> projects = projectService.getAll();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/WEB-INF/jsp/task/taskChooseProject.jsp").forward(req, resp);
            return;
        }

        if ("/task/newWithProject".equals(path)) {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);

            String projectIdStr = req.getParameter("id");
            if (projectIdStr == null || projectIdStr.isEmpty()) {
                throw new InvalidDataException("Нет id проекта");
            }
            Long projectId = Long.parseLong(projectIdStr);
            List<Sprint> sprints = sprintService.getByProjectId(projectId);
            req.setAttribute("projectId", projectId);
            req.setAttribute("sprints", sprints);
            req.getRequestDispatcher("/WEB-INF/jsp/task/taskNewForm.jsp").forward(req, resp);
            return;
        }

        if ("/task".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Нет id задачи");
                return;
            }
            Long taskId;
            try {
                taskId = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный id задачи");
                return;
            }

            Task task = taskService.getById(taskId);
            if (task == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена");
                return;
            }

            List<Project> projects = projectService.getAll();
            List<Sprint> sprints = sprintService.getByProjectId(task.getProjectId());
            List<User> users = userService.getAllUsers();
            String taskUserName = "";
            for (User u : users) {
                if (u.getUserId() != null && u.getUserId().equals(task.getUserId())) {
                    taskUserName = u.getName();
                    break;
                }
            }
            String projectName = "";
            if (task.getProjectId() != null) {
                for (Project p : projects) {
                    if (p.getProjectId() != null && p.getProjectId().equals(task.getProjectId())) {
                        projectName = p.getName();
                        break;
                    }
                }
            }
            String sprintName = "";
            if (task.getSprintId() != null) {
                for (Sprint s : sprints) {
                    if (s.getSprintId() != null && s.getSprintId().equals(task.getSprintId())) {
                        sprintName = s.getName();
                        break;
                    }
                }
            }
            List<Comment> comments = commentService.getByTaskId(taskId);

            req.setAttribute("task", task);
            req.setAttribute("projects", projects);
            req.setAttribute("sprints", sprints);
            req.setAttribute("users", users);
            req.setAttribute("taskUserName", taskUserName);
            req.setAttribute("projectName", projectName);
            req.setAttribute("sprintName", sprintName);
            req.setAttribute("comments", comments);

            if (session != null && session.getAttribute("user") != null) {
                User currentUser = (User) session.getAttribute("user");
                Long currentUserId = currentUser.getUserId();
                if (userRoleService.isManager(currentUserId) || userRoleService.isAdmin(currentUserId)) {
                    req.getRequestDispatcher("/WEB-INF/jsp/task/taskEditForm.jsp").forward(req, resp);
                } else {
                    req.getRequestDispatcher("/WEB-INF/jsp/task/taskViewForm.jsp").forward(req, resp);
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
            return;
        }

        if ("/task/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                throw new InvalidDataException("Нет id задачи");
            }
            Long taskId = Long.parseLong(idStr);
            Task task = taskService.getById(taskId);
            List<Sprint> sprints = sprintService.getByProjectId(task.getProjectId());
            List<User> users = userService.getAllUsers();
            String taskUserName = "";
            for (User user : users) {
                if (user.getUserId().equals(task.getUserId())) {
                    taskUserName = user.getName();
                    break;
                }
            }
            List<Comment> comments = commentService.getByTaskId(taskId);
            req.setAttribute("task", task);
            req.setAttribute("sprints", sprints);
            req.setAttribute("users", users);
            req.setAttribute("taskUserName", taskUserName);
            req.setAttribute("comments", comments);

            req.getRequestDispatcher("/WEB-INF/jsp/task/taskEditForm.jsp").forward(req, resp);
            return;
        }

        if ("/task/delete".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Нет id задачи");
                return;
            }
            Long id = Long.parseLong(idStr);
            taskService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/tasks");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/task/addComment".equals(path)) {
            String text = req.getParameter("text");
            String taskIdStr = req.getParameter("taskId");
            if (text == null || text.trim().isEmpty() || taskIdStr == null || taskIdStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Текст и ID задачи обязательны");
                return;
            }
            Long taskId;
            try {
                taskId = Long.parseLong(taskIdStr.trim());
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID задачи");
                return;
            }

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            User currentUser = (User) session.getAttribute("user");
            Long userId = currentUser.getUserId();

            Comment comment = new Comment();
            comment.setTaskId(taskId);
            comment.setUserId(userId);
            comment.setText(text.trim());
            comment.setCreatedAt(new Date(System.currentTimeMillis()));

            commentService.create(comment);
            resp.sendRedirect(req.getContextPath() + "/task?id=" + taskId);
            return;
        }


        // Добавь другие POST: create, update, etc.
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
