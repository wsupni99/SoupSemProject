package ru.itis.repositories.jdbc;

import ru.itis.entities.Sprint;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.SprintMapper;
import ru.itis.repositories.interfaces.SprintRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SprintRepositoryJdbcImpl implements SprintRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.sprints";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.sprints WHERE sprint_id = ?";
    private static final String SQL_FIND_BY_PROJECT_ID = "SELECT * FROM project.sprints WHERE project_id = ?";
    private static final String SQL_INSERT = "INSERT INTO project.sprints (project_id, name, start_date, end_date) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.sprints SET project_id=?, name=?, start_date=?, end_date=? WHERE sprint_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.sprints WHERE sprint_id=?";

    @Override
    public List<Sprint> findAll() {
        List<Sprint> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(SprintMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Sprint> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(SprintMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Sprint> findByProjectId(Long projectId) {
        List<Sprint> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PROJECT_ID)) {
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(SprintMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(Sprint sprint) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, sprint.getProjectId());
            ps.setString(2, sprint.getName());
            ps.setDate(3, new java.sql.Date(sprint.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(sprint.getEndDate().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Sprint sprint) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setLong(1, sprint.getProjectId());
            ps.setString(2, sprint.getName());
            ps.setDate(3, new java.sql.Date(sprint.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(sprint.getEndDate().getTime()));
            ps.setLong(5, sprint.getSprintId());
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
