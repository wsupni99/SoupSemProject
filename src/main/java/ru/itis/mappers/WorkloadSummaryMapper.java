package ru.itis.mappers;

import ru.itis.entities.WorkloadSummary;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkloadSummaryMapper {
    public static WorkloadSummary map(ResultSet rs) throws SQLException {
        return new WorkloadSummary(
                rs.getLong("summary_id"),
                rs.getLong("user_id"),
                rs.getLong("project_id"),
                rs.getInt("open_tasks_count"),
                rs.getInt("closed_tasks_count"),
                rs.getLong("sprint_id")
        );
    }
}
