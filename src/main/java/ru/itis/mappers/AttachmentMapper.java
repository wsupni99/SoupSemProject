package ru.itis.mappers;

import ru.itis.entities.Attachment;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttachmentMapper {
    public static Attachment map(ResultSet rs) throws SQLException {
        return new Attachment(
                rs.getLong("attachment_id"),
                rs.getLong("task_id"),
                rs.getString("filename"),
                rs.getString("file_url"),
                rs.getString("filetype")
        );
    }
}
