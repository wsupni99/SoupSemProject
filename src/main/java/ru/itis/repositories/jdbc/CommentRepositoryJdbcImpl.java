package ru.itis.repositories.jdbc;

import ru.itis.entities.Comment;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.CommentMapper;
import ru.itis.repositories.interfaces.CommentRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryJdbcImpl implements CommentRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.comments";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.comments WHERE comment_id = ?";
    private static final String SQL_FIND_BY_TASK_ID = "SELECT * FROM project.comments WHERE task_id = ?";
    private static final String SQL_INSERT = "INSERT INTO project.comments (task_id, user_id, text, created_at) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.comments SET task_id=?, user_id=?, text=?, created_at=? WHERE comment_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.comments WHERE comment_id=?";

    @Override
    public List<Comment> findAll() {
        List<Comment> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(CommentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(CommentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        List<Comment> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TASK_ID)) {
            ps.setLong(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(CommentMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Comment comment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, comment.getTaskId());
            ps.setLong(2, comment.getUserId());
            ps.setString(3, comment.getText());
            ps.setDate(4, new java.sql.Date(comment.getCreatedAt().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Comment comment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setLong(1, comment.getTaskId());
            ps.setLong(2, comment.getUserId());
            ps.setString(3, comment.getText());
            ps.setDate(4, new java.sql.Date(comment.getCreatedAt().getTime()));
            ps.setLong(5, comment.getCommentId());
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
