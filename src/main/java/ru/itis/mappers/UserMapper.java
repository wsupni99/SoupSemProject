package ru.itis.mappers;

import ru.itis.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("contact_info")
        );
    }
}
