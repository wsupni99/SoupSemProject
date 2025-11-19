package ru.itis.mappers.project;

import ru.itis.entities.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static Project map(ResultSet rs) throws SQLException {
        return new Project(
                rs.getLong("project_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getString("status"),
                rs.getLong("manager_id")
        );
    }
}
