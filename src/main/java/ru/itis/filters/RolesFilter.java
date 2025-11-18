package ru.itis.filters;

import ru.itis.entities.User;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.services.impl.UserRoleServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
        urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST}
)
public class RolesFilter implements Filter {

    private UserRoleServiceImpl userRoleService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        UserRoleRepositoryJdbcImpl userRoleRepo = new UserRoleRepositoryJdbcImpl();
        RoleRepositoryJdbcImpl roleRepo = new RoleRepositoryJdbcImpl();
        this.userRoleService = new UserRoleServiceImpl(userRoleRepo, roleRepo);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());

        if (path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = null;
        if (session != null) {
            Object obj = session.getAttribute("user");
            if (obj instanceof User) {
                user = (User) obj;
            }
        }

        boolean isAdmin = false;
        boolean isManager = false;
        boolean isDeveloper = false;
        boolean isTester = false;

        if (user != null) {
            Long userId = user.getUserId();
            isAdmin = userRoleService.userHasRole(userId, "ADMIN");
            isManager = userRoleService.userHasRole(userId, "MANAGER");
            isDeveloper = userRoleService.userHasRole(userId, "DEVELOPER");
            isTester = userRoleService.userHasRole(userId, "TESTER");
        }

        req.setAttribute("isAdmin", isAdmin);
        req.setAttribute("isManager", isManager);

        if (isAdmin) {
            chain.doFilter(request, response);
            return;
        }

        boolean isManagerOnly = isManager && !isAdmin;
        boolean isDevOrTester = (isDeveloper || isTester) && !isManager && !isAdmin;

        // users только админ (админ уже пропущен выше)
        if (path.startsWith("/users")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // home: доступен всем залогиненным, но содержание различается на уровне JSP/сервлетов

        // projects + формы проекта: только менеджер
        if (path.startsWith("/projects") || path.startsWith("/project")) {
            if (!isManagerOnly) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // sprints + формы спринтов: только менеджер
        if (path.startsWith("/sprints") || path.startsWith("/sprint")) {
            if (!isManagerOnly) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if (path.startsWith("/tasks") || path.startsWith("/task")) {
            // формы задач только менеджеру
            if (path.startsWith("/task/new")
                    || path.startsWith("/task/edit")
                    || path.startsWith("/task/chooseProject")
                    || path.startsWith("/task/newSubtask")) {

                if (!isManagerOnly) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                // newSubtaskForm: должна быть родительская задача
                if (path.startsWith("/task/newSubtask")) {
                    String parentId = req.getParameter("parentId");
                    if (parentId == null || parentId.isEmpty()) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }
            }

            chain.doFilter(request, response);
            return;
        }

        // home: доступ всем залогиненным (роль уже назначена), логика различий в контроллерах
        if (path.equals("/home") || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // если до сюда дошли: страница не описана выше
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}