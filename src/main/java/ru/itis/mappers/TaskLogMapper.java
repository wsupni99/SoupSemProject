package ru.itis.mappers;

import ru.itis.entities.TaskLog;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskLogMapper {
    public static TaskLog map(ResultSet rs) throws SQLException {
        return new TaskLog(
                rs.getLong("log_id"),
                rs.getLong("task_id"),
                rs.getLong("user_id"),
                rs.getString("action"),
                rs.getDate("changed_at")
        );
    }
}
