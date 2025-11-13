package ru.itis.servlets;

import ru.itis.entities.Project;
import ru.itis.entities.Task;
import ru.itis.entities.Sprint;
import ru.itis.entities.User;
import ru.itis.exceptions.*;
import ru.itis.repositories.jdbc.*;
import ru.itis.services.impl.ProjectServiceImpl;
import ru.itis.services.impl.SprintServiceImpl;
import ru.itis.services.impl.TaskServiceImpl;
import ru.itis.services.impl.UserServiceImpl;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.SprintService;
import ru.itis.services.interfaces.TaskService;
import ru.itis.services.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(
        name = "TaskServlet",
        urlPatterns = {
                "/tasks", "/task/new", "/task/chooseProject", "/task/newWithProject",
                "/task/create", "/task/edit", "/task/update"
        }
)
public class TaskServlet extends HttpServlet {
    private final ProjectService projectService = new ProjectServiceImpl(
            new ProjectRepositoryJdbcImpl(), new SprintRepositoryJdbcImpl(), new TaskRepositoryJdbcImpl());
    private final SprintService sprintService = new SprintServiceImpl(
            new SprintRepositoryJdbcImpl(), projectService);
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryJdbcImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryJdbcImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String path = req.getServletPath();

        if ("/tasks".equals(path)) {
            List<Task> tasks = taskService.getFilteredTasks(req.getParameterMap());
            req.setAttribute("tasks", tasks);
            req.getRequestDispatcher("/WEB-INF/jsp/tasks.jsp").forward(req, resp);
            return;
        }

        if ("/task/new".equals(path)) {
            List<Project> projects = projectService.getAll();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/WEB-INF/jsp/taskChooseProject.jsp").forward(req, resp);
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
            req.getRequestDispatcher("/WEB-INF/jsp/taskNewForm.jsp").forward(req, resp);
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

            req.setAttribute("task", task);
            req.setAttribute("sprints", sprints);
            req.setAttribute("users", users);
            req.setAttribute("taskUserName", taskUserName);

            req.getRequestDispatcher("/WEB-INF/jsp/taskEditForm.jsp").forward(req, resp);
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
        }
    }
}
