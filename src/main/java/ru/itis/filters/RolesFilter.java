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

        // Статика (раньше не работали стили)
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

        if (user != null) {
            Long userId = user.getUserId();
            isAdmin = userRoleService.userHasRole(userId, "ADMIN");
            isManager = userRoleService.userHasRole(userId, "MANAGER");
        }

        req.setAttribute("isAdmin", isAdmin);
        req.setAttribute("isManager", isManager);

        // users только для админа
        if ((path.startsWith("/users"))
                && !isAdmin) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
