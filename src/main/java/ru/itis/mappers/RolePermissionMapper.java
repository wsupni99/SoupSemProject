package ru.itis.mappers;

import ru.itis.entities.RolePermission;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolePermissionMapper {
    public static RolePermission map(ResultSet rs) throws SQLException {
        return new RolePermission(
                rs.getLong("role_id"),
                rs.getLong("permission_id")
        );
    }
}
