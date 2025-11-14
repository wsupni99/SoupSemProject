package ru.itis.repositories.jdbc;

import ru.itis.entities.Task;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.TaskMapper;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryJdbcImpl implements TaskRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.tasks";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.tasks WHERE task_id = ?";
    private static final String SQL_FIND_BY_PROJECT_ID = "SELECT * FROM project.tasks WHERE project_id = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT * FROM project.tasks WHERE user_id = ?";
    private static final String SQL_FIND_BY_STATUS = "SELECT * FROM project.tasks WHERE status = ?";
    private static final String SQL_INSERT = "INSERT INTO project.tasks (name, description, priority, status, created_at, updated_at, deadline, parent_task_id, project_id, sprint_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.tasks SET name=?, description=?, priority=?, status=?, created_at=?, updated_at=?, deadline=?, parent_task_id=?, project_id=?, sprint_id=?, user_id=? WHERE task_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.tasks WHERE task_id=?";
    private static final String SQL_FIND_BY_PARENT_TASK_ID = "SELECT * FROM project.tasks WHERE parent_task_id = ?";
    private static final String SQL_FIND_BY_SPRINT = "SELECT * FROM project.tasks WHERE sprint_id = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM project.sprints WHERE project_id = ?";

    @Override
    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(TaskMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Task> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(TaskMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PROJECT_ID)) {
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(TaskMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(TaskMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Task> findByStatus(String status) {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STATUS)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(TaskMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Task task) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, task.getName());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriority());
            ps.setString(4, task.getStatus());
            ps.setDate(5, new java.sql.Date(task.getCreatedAt().getTime()));
            ps.setDate(6, new java.sql.Date(task.getUpdatedAt().getTime()));
            ps.setDate(7, task.getDeadline() != null ? new java.sql.Date(task.getDeadline().getTime()) : null);
            ps.setObject(8, task.getParentTaskId());
            ps.setLong(9, task.getProjectId());
            ps.setLong(10, task.getSprintId());
            ps.setLong(11, task.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Task task) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, task.getName());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriority());
            ps.setString(4, task.getStatus());
            ps.setDate(5, new java.sql.Date(task.getCreatedAt().getTime()));
            ps.setDate(6, new java.sql.Date(task.getUpdatedAt().getTime()));
            ps.setDate(7, new java.sql.Date(task.getDeadline().getTime()));
            ps.setObject(8, task.getParentTaskId());
            ps.setLong(9, task.getProjectId());
            ps.setLong(10, task.getSprintId());
            ps.setLong(11, task.getUserId());
            ps.setLong(12, task.getTaskId());
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

    @Override
    public List<Task> findByParentTaskId(Long parentTaskId) {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PARENT_TASK_ID)) {
            ps.setLong(1, parentTaskId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(TaskMapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Task> findBySprintId(Long sprintId) {
        List<Task> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_SPRINT)) {
            ps.setLong(1, sprintId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(TaskMapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public int countByProjectId(long projectId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT)) {
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public List<Task> findWithFilters(Long projectId, Long userId, String status, Long sprintId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM project.tasks WHERE 1=1");

        if (projectId != null) {
            sql.append(" AND project_id = ?");
            params.add(projectId);
        }
        if (userId != null) {
            sql.append(" AND user_id = ?");
            params.add(userId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (sprintId != null) {
            sql.append(" AND sprint_id = ?");
            params.add(sprintId);
        }

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            List<Task> tasks = new ArrayList<>();

            while (rs.next()) {
                tasks.add(TaskMapper.map(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
