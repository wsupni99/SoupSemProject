package ru.itis.mappers;

import ru.itis.entities.Sprint;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SprintMapper {
    public static Sprint map(ResultSet rs) throws SQLException {
        return new Sprint(
                rs.getLong("sprint_id"),
                rs.getLong("project_id"),
                rs.getString("name"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        );
    }
}
