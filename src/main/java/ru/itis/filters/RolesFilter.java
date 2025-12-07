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

    private void forward403(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        req.getRequestDispatcher("/jsp/error/403.jsp").forward(req, resp);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());

        if (path.startsWith("/css/")) {
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

        // users только админ
        if (path.startsWith("/users")) {
            forward403(req, resp);
            return;
        }

        // projects: менеджер или админ
        if (path.startsWith("/projects") || path.startsWith("/project")) {
            if (!(isManager || isAdmin)) {
                forward403(req, resp);
                return;
            }
        }

        // sprints: менеджер или админ
        if (path.startsWith("/sprints") || path.startsWith("/sprint")) {
            if (!(isManager || isAdmin)) {
                forward403(req, resp);
                return;
            }
        }

        if (path.startsWith("/tasks") || path.startsWith("/task")) {
            // формы задач только менеджеру
            if (path.startsWith("/task/new")
                    || path.startsWith("/task/edit")
                    || path.startsWith("/task/chooseProject")
                    || path.startsWith("/task/newWithProject")) {

                if (!isManagerOnly) {
                    forward403(req, resp);
                    return;
                }
            }

            if (path.equals("/task") && req.getParameter("id") != null) {
                if (!(isAdmin || isManager || isDeveloper || isTester)) {
                    forward403(req, resp);
                    return;
                }
            }

            chain.doFilter(request, response);
            return;
        }

        if (path.equals("/home") || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
    }
}
