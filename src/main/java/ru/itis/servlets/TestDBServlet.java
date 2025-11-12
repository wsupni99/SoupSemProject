package ru.itis.servlets;

import ru.itis.util.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "SELECT project_id, name, description, start_date, end_date, manager_id, status FROM project.projects LIMIT 10");
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                result.append("id=").append(rs.getLong("project_id"))
                        .append(" | name=").append(rs.getString("name"))
                        .append(" | desc=").append(rs.getString("description"))
                        .append(" | start=").append(rs.getDate("start_date"))
                        .append(" | end=").append(rs.getDate("end_date"))
                        .append(" | manager=").append(rs.getLong("manager_id"))
                        .append(" | status=").append(rs.getString("status"))
                        .append("\n");
            }
        } catch (Exception e) {
            result.append("Ошибка: ").append(e.getMessage());
        }
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write(result.toString());
    }
}
