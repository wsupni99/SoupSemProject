package ru.itis.repositories.jdbc;

import ru.itis.entities.TaskLog;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.TaskLogMapper;
import ru.itis.repositories.interfaces.TaskLogRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskLogRepositoryJdbcImpl implements TaskLogRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.task_log";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.task_log WHERE log_id = ?";
    private static final String SQL_FIND_BY_TASK_ID = "SELECT * FROM project.task_log WHERE task_id = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT * FROM project.task_log WHERE user_id = ?";
    private static final String SQL_INSERT = "INSERT INTO project.task_log (task_id, user_id, action, changed_at) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.task_log SET task_id=?, user_id=?, action=?, changed_at=? WHERE log_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.task_log WHERE log_id=?";

    @Override
    public List<TaskLog> findAll() {
        List<TaskLog> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(TaskLogMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<TaskLog> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(TaskLogMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<TaskLog> findByTaskId(Long taskId) {
        List<TaskLog> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TASK_ID)) {
            ps.setLong(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(TaskLogMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<TaskLog> findByUserId(Long userId) {
        List<TaskLog> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(TaskLogMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(TaskLog log) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, log.getTaskId());
            ps.setLong(2, log.getUserId());
            ps.setString(3, log.getAction());
            ps.setDate(4, new java.sql.Date(log.getChangedAt().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Long projectId, TaskLog log) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setLong(1, log.getTaskId());
            ps.setLong(2, log.getUserId());
            ps.setString(3, log.getAction());
            ps.setDate(4, new java.sql.Date(log.getChangedAt().getTime()));
            ps.setLong(5, log.getLogId());
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
