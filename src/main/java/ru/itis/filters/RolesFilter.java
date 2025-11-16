package ru.itis.filters;

import ru.itis.entities.User;
import ru.itis.repositories.jdbc.RoleRepositoryJdbcImpl;
import ru.itis.repositories.jdbc.UserRoleRepositoryJdbcImpl;
import ru.itis.services.impl.UserRoleServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
    public void init(FilterConfig filterConfig) {
        UserRoleRepositoryJdbcImpl userRoleRepo = new UserRoleRepositoryJdbcImpl();
        RoleRepositoryJdbcImpl roleRepo = new RoleRepositoryJdbcImpl();
        userRoleService = new UserRoleServiceImpl(userRoleRepo, roleRepo);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        if (path.equals("/login") || path.equals("/register") ||
                path.equals("/auth/login.jsp") || path.equals("/WEB-INF/jsp/auth/login.jsp") ||
                path.equals("/auth/register.jsp") || path.equals("/WEB-INF/jsp/auth/register.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getUserId() == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long userId = user.getUserId();

        // Debug-лог
        System.out.println("Filter Debug: Path='" + path + "', UserId=" + userId + ", IsAdmin=" + userRoleService.isAdmin(userId));

        // Глобальный доступ для админа
        if (userRoleService.isAdmin(userId)) {
            System.out.println("Filter Debug: Admin access granted for path: " + path);
            chain.doFilter(request, response);
            return;
        }

        // Флаги ролей (для не-админов)
        boolean isManager = userRoleService.isManager(userId);
        boolean isDev = userRoleService.isDeveloper(userId);
        boolean isTester = userRoleService.isTester(userId);
        boolean isDevOrTester = isDev || isTester;

        // Проверка доступа по путям (только для не-админов)
        if (path.equals("/users") || path.equals("/user/users")) {
            // Только ADMIN (но уже проверено выше)
            resp.sendError(403, "Access denied for your role");
            return;
        } else if (path.equals("/projects") || path.equals("/projectEditForm") || path.equals("/projectNewForm")) {
            // MANAGER
            if (!isManager) {
                resp.sendError(403, "Access denied for your role");
                return;
            }
        } else if (path.equals("/sprints") || path.equals("/sprintNewForm") || path.equals("/sprintEditForm")) {
            // MANAGER
            if (!isManager) {
                resp.sendError(403, "Access denied for your role");
                return;
            }
        } else if (path.equals("/newSubtaskForm") || path.equals("/taskChooseProject") || path.equals("/taskEditForm") || path.equals("/taskNewForm")) {
            // MANAGER
            if (!isManager) {
                resp.sendError(403, "Access denied for your role");
                return;
            }
        } else if (path.equals("/tasks") || path.equals("/home")) {
            // DEVELOPER, TESTER, MANAGER
            if (!isDevOrTester && !isManager) {
                resp.sendError(403, "Access denied for your role");
                return;
            }
        } else if (path.equals("/workload")) {
            // Все роли – пропускаем
        } else {
            // Неизвестные пути – доступ всем авторизованным
            System.out.println("Filter Debug: Unknown path '" + path + "' allowed for userId: " + userId);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
