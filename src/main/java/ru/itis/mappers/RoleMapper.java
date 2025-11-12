package ru.itis.mappers;

import ru.itis.entities.Role;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper {
    public static Role map(ResultSet rs) throws SQLException {
        return new Role(
                rs.getLong("role_id"),
                rs.getString("role_name"),
                rs.getString("description")
        );
    }
}
