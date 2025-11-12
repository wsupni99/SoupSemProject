package ru.itis.repositories.jdbc;

import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.ProjectMapper;
import ru.itis.entities.Project;
import ru.itis.repositories.interfaces.ProjectRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryJdbcImpl implements ProjectRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM project.projects";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM project.projects WHERE project_id = ?";
    private static final String SQL_INSERT = "INSERT INTO project.projects (name, description, start_date, end_date, status, manager_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE project.projects SET name=?, description=?, start_date=?, end_date=?, status=?, manager_id=? WHERE project_id=?";
    private static final String SQL_DELETE = "DELETE FROM project.projects WHERE project_id=?";
    private static final String SQL_FIND_BY_MANAGER = "SELECT * FROM project.projects WHERE manager_id = ?";
    private static final String SQL_FIND_BY_STATUS = "SELECT * FROM project.projects WHERE status = ?";

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(ProjectMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return projects;
    }

    @Override
    public Optional<Project> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(ProjectMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void save(Project project) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(project.getEndDate().getTime()));
            ps.setString(5, project.getStatus());
            ps.setLong(6, project.getManagerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Project project) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(project.getEndDate().getTime()));
            ps.setString(5, project.getStatus());
            ps.setLong(6, project.getManagerId());
            ps.setLong(7, project.getProjectId());
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
    public List<Project> findByManagerId(Long managerId) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_MANAGER)) {
            ps.setLong(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(ProjectMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return projects;
    }

    @Override
    public List<Project> findByStatus(String status) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STATUS)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(ProjectMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return projects;
    }
}
