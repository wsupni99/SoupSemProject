package ru.itis.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        Boolean isAuthenticated = false;
        Boolean sessionExists = session != null;
        Boolean isSignInOrUpPage = request.getRequestURI().equals(request.getContextPath() + "/login")
                || request.getRequestURI().equals(request.getContextPath() + "/register");
        Boolean wantsCss = request.getRequestURI().startsWith(request.getContextPath() + "/css/");

        if (sessionExists) {
            // здесь в сессии лежит объект User, а не Boolean
            isAuthenticated = session.getAttribute("user") != null;
        }

        if (!isAuthenticated && isSignInOrUpPage || wantsCss) {
            filterChain.doFilter(request, response);
        } else if (isAuthenticated && isSignInOrUpPage) {
            response.sendRedirect(request.getContextPath() + "/profile");
        } else if (!isAuthenticated && !isSignInOrUpPage) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // no-op
    }
}
