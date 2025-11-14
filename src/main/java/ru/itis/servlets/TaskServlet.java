package ru.itis.servlets;

import ru.itis.entities.*;
import ru.itis.exceptions.*;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.impl.*;
import ru.itis.services.interfaces.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

//TODO: Мб убрать айди в /tasks

@WebServlet(
        name = "TaskServlet",
        urlPatterns = {
                "/tasks", "/task/new", "/task/chooseProject", "/task/newWithProject",
                "/task/create", "/task/edit", "/task/update", "/task/delete", "/task/addComment"
        }
)
public class TaskServlet extends HttpServlet {
    private final ProjectService projectService = new ProjectServiceImpl(
            new ProjectRepositoryJdbcImpl(), new SprintRepositoryJdbcImpl(), new TaskRepositoryJdbcImpl());
    private final SprintService sprintService = new SprintServiceImpl(
            new SprintRepositoryJdbcImpl(), projectService);
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryJdbcImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());
    private final CommentService commentService = new CommentServiceImpl(new CommentRepositoryJdbcImpl());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/tasks".equals(path)) {
            List<Task> tasks = taskService.getFilteredTasks(req.getParameterMap());
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
                if (user.getUserId().equals(task.getUserId())){
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
        }

        if ("/task/addComment".equals(path)) {
            req.setCharacterEncoding("UTF-8");
            String text = req.getParameter("text");
            Long taskId = Long.parseLong(req.getParameter("taskId"));

            Comment comment = new Comment();
            comment.setTaskId(taskId);
            comment.setText(text);
            comment.setCreatedAt(new Date(System.currentTimeMillis()));

            commentService.create(comment);
            resp.sendRedirect(req.getContextPath() + "/task/edit?id=" + taskId);
            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/task/chooseProject".equals(path)) {
            String projectId = req.getParameter("projectId");
            if (projectId == null || projectId.isEmpty()) {
                throw new InvalidDataException("projectId пуст");
            }
            resp.sendRedirect(req.getContextPath() + "/task/newWithProject?id=" + projectId);
            return;
        }
        if ("/task/create".equals(path)) {
            try {
                Task task = new Task();
                task.setName(req.getParameter("name"));
                task.setDescription(req.getParameter("description"));
                task.setPriority(req.getParameter("priority"));
                task.setStatus(req.getParameter("status"));
                task.setDeadline(req.getParameter("deadline") != null && !req.getParameter("deadline").isEmpty()
                        ? Date.valueOf(req.getParameter("deadline")) : null);
                task.setParentTaskId(
                        req.getParameter("parentTaskId") != null
                                && !req.getParameter("parentTaskId").isEmpty()
                                && !req.getParameter("parentTaskId").equals("0")
                                ? Long.valueOf(req.getParameter("parentTaskId"))
                                : null
                );
                task.setProjectId(req.getParameter("projectId") != null && !req.getParameter("projectId").isEmpty()
                        ? Long.valueOf(req.getParameter("projectId")) : null);
                task.setSprintId(req.getParameter("sprintId") != null && !req.getParameter("sprintId").isEmpty()
                        ? Long.valueOf(req.getParameter("sprintId")) : null);
                String userIdStr = req.getParameter("userId");
                if (userIdStr == null || userIdStr.isEmpty())
                    throw new InvalidDataException("userId обязателен");
                task.setUserId(Long.valueOf(userIdStr));
                taskService.create(task);
                resp.sendRedirect(req.getContextPath() + "/tasks");
            } catch (DuplicateEntityException e) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
            } catch (PermissionDeniedException e) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            } catch (InvalidDataException | EntityNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
            return;
        }

        if ("/task/update".equals(path)) {
            try {
                Task task = new Task();
                task.setTaskId(Long.valueOf(req.getParameter("taskId")));
                task.setName(req.getParameter("name"));
                task.setDescription(req.getParameter("description"));
                task.setPriority(req.getParameter("priority"));
                task.setStatus(req.getParameter("status"));
                task.setCreatedAt(req.getParameter("createdAt") != null && !req.getParameter("createdAt").isEmpty()
                        ? Date.valueOf(req.getParameter("createdAt")) : new Date(System.currentTimeMillis()));
                task.setUpdatedAt(new Date(System.currentTimeMillis()));
                task.setDeadline(req.getParameter("deadline") != null && !req.getParameter("deadline").isEmpty()
                        ? Date.valueOf(req.getParameter("deadline")) : null);
                task.setParentTaskId(
                        req.getParameter("parentTaskId") != null && !req.getParameter("parentTaskId").isEmpty()
                                && !req.getParameter("parentTaskId").equals("0")
                                ? Long.valueOf(req.getParameter("parentTaskId"))
                                : null);
                task.setProjectId(req.getParameter("projectId") != null && !req.getParameter("projectId").isEmpty()
                        ? Long.valueOf(req.getParameter("projectId")) : null);
                task.setSprintId(req.getParameter("sprintId") != null && !req.getParameter("sprintId").isEmpty()
                        ? Long.valueOf(req.getParameter("sprintId")) : null);
                String userIdStr = req.getParameter("userId");
                if (userIdStr == null || userIdStr.isEmpty())
                    throw new InvalidDataException("userId обязателен");
                task.setUserId(Long.valueOf(userIdStr));

                taskService.update(task);
                resp.sendRedirect(req.getContextPath() + "/tasks");
            } catch (InvalidDataException | EntityNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (PermissionDeniedException e) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            }
            return;
        }
        if ("/task/addComment".equals(path)) {
            req.setCharacterEncoding("UTF-8");
            String text = req.getParameter("text");
            Long taskId = Long.parseLong(req.getParameter("taskId"));
            //TODO
            // HARDCODE для userId (например, самый первый пользователь)
            Long userId = 1L;

            Comment comment = new Comment();
            comment.setTaskId(taskId);
            comment.setUserId(userId);
            comment.setText(text);
            comment.setCreatedAt(new Date(System.currentTimeMillis()));

            commentService.create(comment);
            resp.sendRedirect(req.getContextPath() + "/task/edit?id=" + taskId);
            return;
        }
    }
}
