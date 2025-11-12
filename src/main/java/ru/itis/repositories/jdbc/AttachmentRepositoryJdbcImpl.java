package ru.itis.repositories.jdbc;

import ru.itis.entities.Attachment;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.AttachmentMapper;
import ru.itis.repositories.interfaces.AttachmentRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttachmentRepositoryJdbcImpl implements AttachmentRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.attachments";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.attachments WHERE attachment_id = ?";
    private static final String SQL_FIND_BY_TASK_ID = "SELECT * FROM project.attachments WHERE task_id = ?";
    private static final String SQL_INSERT = "INSERT INTO project.attachments (task_id, filename, file_url, filetype) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.attachments SET task_id=?, filename=?, file_url=?, filetype=? WHERE attachment_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.attachments WHERE attachment_id=?";

    @Override
    public List<Attachment> findAll() {
        List<Attachment> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(AttachmentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Attachment> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(AttachmentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Attachment> findByTaskId(Long taskId) {
        List<Attachment> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TASK_ID)) {
            ps.setLong(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(AttachmentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Attachment attachment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, attachment.getTaskId());
            ps.setString(2, attachment.getFileName());
            ps.setString(3, attachment.getFileUrl());
            ps.setString(4, attachment.getFileType());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Attachment attachment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setLong(1, attachment.getTaskId());
            ps.setString(2, attachment.getFileName());
            ps.setString(3, attachment.getFileUrl());
            ps.setString(4, attachment.getFileType());
            ps.setLong(5, attachment.getAttachmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }
}
