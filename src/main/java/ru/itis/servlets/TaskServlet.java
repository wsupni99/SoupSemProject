package ru.itis.servlets;

import ru.itis.entities.*;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.impl.*;
import ru.itis.services.interfaces.*;

import javax.servlet.ServletContext;
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
        "/tasks", "/task", "/task/new", "/task/newWithProject", "/task/create",
        "/task/update", "/task/edit", "/task/delete", "/task/addComment"
})
public class TaskServlet extends HttpServlet {
    private ProjectService projectService;
    private SprintService sprintService;
    private TaskService taskService;
    private UserService userService;
    private UserRoleService userRoleService;
    private CommentService commentService;

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
        userService = (UserService) services.get("userService");
        userRoleService = (UserRoleService) services.get("userRoleService");
        commentService = (CommentService) services.get("commentService");

        if (projectService == null || sprintService == null || taskService == null ||
                userService == null || userRoleService == null || commentService == null) {
            throw new RuntimeException("Some services failed to initialize");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        // Проверка авторизации и установка атрибутов ролей
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
            req.getRequestDispatcher("/jsp/task/tasks.jsp").forward(req, resp);
            return;
        }

        if ("/task/new".equals(path)) {
            List<Project> projects = projectService.getAll();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/jsp/task/taskChooseProject.jsp").forward(req, resp);
            return;
        }

        if ("/task/newWithProject".equals(path)) {
            String projectIdStr = req.getParameter("id");
            if (projectIdStr == null || projectIdStr.isEmpty()) {
                throw new InvalidDataException("ID проекта не указан");
            }

            Long projectId = Long.parseLong(projectIdStr);

            List<User> users = userService.getAllUsers();
            List<Sprint> sprints = sprintService.getByProjectId(projectId);
            List<Task> projectTasks = taskService.getByProjectId(projectId);

            req.setAttribute("users", users);
            req.setAttribute("sprints", sprints);
            req.setAttribute("projectId", projectId);
            req.setAttribute("projectTasks", projectTasks);

            req.getRequestDispatcher("/jsp/task/taskNewForm.jsp")
                    .forward(req, resp);
            return;
        }

        if ("/task".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/error/404");
                return;
            }
            Long taskId;
            try {
                taskId = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/error/404");
                return;
            }
            Task task = taskService.getById(taskId);
            if (task == null) {
                resp.sendRedirect(req.getContextPath() + "/error/404");
                return;
            }

            List<Project> projects = projectService.getAll();
            List<Sprint> sprints = sprintService.getByProjectId(task.getProjectId());
            List<User> users = userService.getAllUsers();
            List<Comment> comments = commentService.getByTaskId(taskId);

            String projectName = "";
            if (task.getProjectId() != null) {
                for (Project p : projects) {
                    if (p.getProjectId().equals(task.getProjectId())) {
                        projectName = p.getName();
                        break;
                    }
                }
            }

            String taskUserName = "";
            if (task.getUserId() != null) {
                for (User u : users) {
                    if (u.getUserId().equals(task.getUserId())) {
                        taskUserName = u.getName();
                        break;
                    }
                }
            }

            String sprintName = "";
            if (task.getSprintId() != null) {
                for (Sprint s : sprints) {
                    if (s.getSprintId().equals(task.getSprintId())) {
                        sprintName = s.getName();
                        break;
                    }
                }
            }

            req.setAttribute("task", task);
            req.setAttribute("projects", projects);
            req.setAttribute("sprints", sprints);
            req.setAttribute("users", users);
            req.setAttribute("comments", comments);
            req.setAttribute("projectName", projectName);
            req.setAttribute("taskUserName", taskUserName);
            req.setAttribute("sprintName", sprintName);


            if (session != null && session.getAttribute("user") != null) {
                User currentUser = (User) session.getAttribute("user");
                Long currentUserId = currentUser.getUserId();
                if (userRoleService.isManager(currentUserId) || userRoleService.isAdmin(currentUserId)) {
                    req.getRequestDispatcher("/jsp/task/taskEditForm.jsp").forward(req, resp);
                } else {
                    req.getRequestDispatcher("/jsp/task/taskViewForm.jsp").forward(req, resp);
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
            return;
        }

        if ("/task/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                throw new InvalidDataException("Не указан ID задачи");
            }

            Long taskId = Long.parseLong(idStr);
            Task task = taskService.getById(taskId);
            if (task == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена");
                return;
            }

            List<Sprint> sprints = sprintService.getByProjectId(task.getProjectId());
            List<User> users = userService.getAllUsers();
            List<Comment> comments = commentService.getByTaskId(taskId);
            List<Task> projectTasks = taskService.getByProjectId(task.getProjectId());

            String taskUserName = "";
            for (User user : users) {
                if (user.getUserId().equals(task.getUserId())) {
                    taskUserName = user.getName();
                    break;
                }
            }

            req.setAttribute("task", task);
            req.setAttribute("sprints", sprints);
            req.setAttribute("users", users);
            req.setAttribute("taskUserName", taskUserName);
            req.setAttribute("comments", comments);
            req.setAttribute("projectTasks", projectTasks);

            req.getRequestDispatcher("/jsp/task/taskEditForm.jsp").forward(req, resp);
            return;
        }


        if ("/task/delete".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID задачи");
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
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        Long currentUserId = currentUser.getUserId();
        boolean isAdminOrManager = userRoleService.isAdmin(currentUserId) || userRoleService.isManager(currentUserId);



        if ("/task/create".equals(path)) {
            if (!isAdminOrManager) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на создание задач");
                return;
            }

            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String priorityStr = req.getParameter("priority");
            String statusStr = req.getParameter("status");
            String deadlineStr = req.getParameter("deadline");
            String sprintIdStr = req.getParameter("sprintId");
            String userIdStr = req.getParameter("userId");
            String projectIdStr = req.getParameter("projectId");
            String parentTaskIdStr = req.getParameter("parentTaskId");

            if (name == null || name.trim().isEmpty() ||
                    description == null || description.trim().isEmpty() ||
                    priorityStr == null || priorityStr.trim().isEmpty() ||
                    statusStr == null || statusStr.trim().isEmpty() ||
                    sprintIdStr == null || sprintIdStr.trim().isEmpty() ||
                    userIdStr == null || userIdStr.trim().isEmpty() ||
                    projectIdStr == null || projectIdStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Заполните все обязательные поля");
                return;
            }

            try {
                Task task = new Task();
                task.setName(name.trim());
                task.setDescription(description.trim());
                task.setPriority(priorityStr.trim());
                task.setStatus(statusStr.trim());

                if (!deadlineStr.trim().isEmpty()) {
                    task.setDeadline(Date.valueOf(deadlineStr));
                }

                task.setSprintId(Long.parseLong(sprintIdStr));
                task.setUserId(Long.parseLong(userIdStr));
                task.setProjectId(Long.parseLong(projectIdStr));

                if (parentTaskIdStr != null && !parentTaskIdStr.trim().isEmpty() && !parentTaskIdStr.equals("0")) {
                    task.setParentTaskId(Long.parseLong(parentTaskIdStr));
                }

                taskService.create(task);
                resp.sendRedirect(req.getContextPath() + "/tasks");
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат числовых данных");
            } catch (IllegalArgumentException | InvalidDataException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
            return;
        }



        if ("/task/update".equals(path)) {
            if (!isAdminOrManager) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Нет прав на редактирование задач");
                return;
            }

            String taskIdStr = req.getParameter("taskId");
            if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID задачи обязателен");
                return;
            }

            try {
                Long taskId = Long.parseLong(taskIdStr);
                Task existingTask = taskService.getById(taskId);
                if (existingTask == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена");
                    return;
                }

                String name = req.getParameter("name");
                String description = req.getParameter("description");
                String priorityStr = req.getParameter("priority");
                String statusStr = req.getParameter("status");
                String deadlineStr = req.getParameter("deadline");
                String sprintIdStr = req.getParameter("sprintId");
                String userIdStr = req.getParameter("userId");
                String parentTaskIdStr = req.getParameter("parentTaskId");

                if (name == null || name.trim().isEmpty() ||
                        description == null || description.trim().isEmpty() ||
                        priorityStr == null || priorityStr.trim().isEmpty() ||
                        statusStr == null || statusStr.trim().isEmpty() ||
                        sprintIdStr == null || sprintIdStr.trim().isEmpty() ||
                        userIdStr == null || userIdStr.trim().isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Заполните обязательные поля");
                    return;
                }

                existingTask.setName(name.trim());
                existingTask.setDescription(description.trim());
                existingTask.setPriority(priorityStr.trim());
                existingTask.setStatus(statusStr.trim());

                if (!deadlineStr.trim().isEmpty()) {
                    existingTask.setDeadline(Date.valueOf(deadlineStr));
                }

                existingTask.setSprintId(Long.parseLong(sprintIdStr));
                existingTask.setUserId(Long.parseLong(userIdStr));

                if (parentTaskIdStr != null && !parentTaskIdStr.trim().isEmpty() && !parentTaskIdStr.equals("0")) {
                    existingTask.setParentTaskId(Long.parseLong(parentTaskIdStr));
                } else {
                    existingTask.setParentTaskId(null);
                }

                existingTask.setUpdatedAt(new Date(System.currentTimeMillis()));

                taskService.update(existingTask);
                resp.sendRedirect(req.getContextPath() + "/tasks");
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат данных");
            } catch (IllegalArgumentException | EntityNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
            return;
        }



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

            Comment comment = new Comment();
            comment.setTaskId(taskId);
            comment.setUserId(currentUserId);
            comment.setText(text.trim());
            comment.setCreatedAt(new Date(System.currentTimeMillis()));

            commentService.create(comment);
            resp.sendRedirect(req.getContextPath() + "/task?id=" + taskId);
            return;
        }

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
