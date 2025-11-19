package ru.itis.listener;

import ru.itis.repositories.jdbc.*;
import ru.itis.repositories.interfaces.*;
import ru.itis.services.impl.*;
import ru.itis.services.interfaces.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private Map<String, Object> services = new HashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();

            // Репозитории
            UserRepository userRepository = new UserRepositoryJdbcImpl();
            ProjectRepository projectRepository = new ProjectRepositoryJdbcImpl();
            SprintRepository sprintRepository = new SprintRepositoryJdbcImpl();
            TaskRepository taskRepository = new TaskRepositoryJdbcImpl();
            CommentRepository commentRepository = new CommentRepositoryJdbcImpl();
            UserRoleRepository userRoleRepository = new UserRoleRepositoryJdbcImpl();
            RoleRepository roleRepository = new RoleRepositoryJdbcImpl();

            // Сервисы
            UserService userService = new UserServiceImpl(userRepository);
            ProjectService projectService = new ProjectServiceImpl(projectRepository, sprintRepository, taskRepository, userService);
            SprintService sprintService = new SprintServiceImpl(sprintRepository, projectService);
            TaskService taskService = new TaskServiceImpl(taskRepository);
            CommentService commentService = new CommentServiceImpl(commentRepository);
            UserRoleService userRoleService = new UserRoleServiceImpl(userRoleRepository, roleRepository);
            RoleService roleService = new RoleServiceImpl(roleRepository);

            services.put("userService", userService);
            services.put("projectService", projectService);
            services.put("sprintService", sprintService);
            services.put("taskService", taskService);
            services.put("commentService", commentService);
            services.put("userRoleService", userRoleService);
            services.put("roleService", roleService);

            servletContext.setAttribute("services", services);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
