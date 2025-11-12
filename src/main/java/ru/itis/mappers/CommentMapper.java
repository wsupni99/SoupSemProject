package ru.itis.mappers;

import ru.itis.entities.Comment;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper {
    public static Comment map(ResultSet rs) throws SQLException {
        return new Comment(
                rs.getLong("comment_id"),
                rs.getLong("task_id"),
                rs.getLong("user_id"),
                rs.getString("text"),
                rs.getDate("created_at")
        );
    }
}
