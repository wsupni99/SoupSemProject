package ru.itis.mappers;

import ru.itis.entities.Task;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper {
    public static Task map(ResultSet rs) throws SQLException {
        return new Task(
                rs.getLong("task_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("priority"),
                rs.getString("status"),
                rs.getDate("created_at"),
                rs.getDate("updated_at"),
                rs.getDate("deadline"),
                rs.getLong("parent_task_id"),
                rs.getLong("project_id"),
                rs.getLong("sprint_id"),
                rs.getLong("user_id")
        );
    }
}
