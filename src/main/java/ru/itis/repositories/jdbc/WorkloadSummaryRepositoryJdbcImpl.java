package ru.itis.repositories.jdbc;

import ru.itis.entities.WorkloadSummary;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.WorkloadSummaryMapper;
import ru.itis.repositories.interfaces.WorkloadSummaryRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkloadSummaryRepositoryJdbcImpl implements WorkloadSummaryRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM analytics.workload_summary";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM analytics.workload_summary WHERE summary_id = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT * FROM analytics.workload_summary WHERE user_id = ?";
    private static final String SQL_FIND_BY_PROJECT_ID = "SELECT * FROM analytics.workload_summary WHERE project_id = ?";
    private static final String SQL_FIND_BY_SPRINT_ID = "SELECT * FROM analytics.workload_summary WHERE sprint_id = ?";
    private static final String SQL_INSERT = "INSERT INTO analytics.workload_summary (user_id, project_id, open_tasks_count, closed_tasks_count, sprint_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE analytics.workload_summary SET user_id=?, project_id=?, open_tasks_count=?, closed_tasks_count=?, sprint_id=? WHERE summary_id=?";
    private static final String SQL_DELETE = "DELETE FROM analytics.workload_summary WHERE summary_id=?";

    @Override
    public List<WorkloadSummary> findAll() {
        List<WorkloadSummary> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(WorkloadSummaryMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<WorkloadSummary> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(WorkloadSummaryMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<WorkloadSummary> findByUserId(Long userId) {
        List<WorkloadSummary> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(WorkloadSummaryMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<WorkloadSummary> findByProjectId(Long projectId) {
        List<WorkloadSummary> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PROJECT_ID)) {
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(WorkloadSummaryMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<WorkloadSummary> findBySprintId(Long sprintId) {
        List<WorkloadSummary> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_SPRINT_ID)) {
            ps.setLong(1, sprintId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(WorkloadSummaryMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(WorkloadSummary ws) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, ws.getUserId());
            ps.setLong(2, ws.getProjectId());
            ps.setInt(3, ws.getOpenTasksCount());
            ps.setInt(4, ws.getClosedTasksCount());
            ps.setLong(5, ws.getSprintId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(WorkloadSummary ws) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setLong(1, ws.getUserId());
            ps.setLong(2, ws.getProjectId());
            ps.setInt(3, ws.getOpenTasksCount());
            ps.setInt(4, ws.getClosedTasksCount());
            ps.setLong(5, ws.getSprintId());
            ps.setLong(6, ws.getSummaryId());
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
