package ru.itis.mappers;

import ru.itis.entities.Permission;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper {
    public static Permission map(ResultSet rs) throws SQLException {
        return new Permission(
                rs.getLong("permission_id"),
                rs.getString("permission_name"),
                rs.getString("description")
        );
    }
}
