package ru.itis.mappers;

import ru.itis.entities.UserRole;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleMapper {
    public static UserRole map(ResultSet rs) throws SQLException {
        return new UserRole(
                rs.getLong("user_id"),
                rs.getLong("role_id")
        );
    }
}
