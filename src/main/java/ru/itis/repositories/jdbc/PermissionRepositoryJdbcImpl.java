package ru.itis.repositories.jdbc;

import ru.itis.entities.Permission;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.PermissionMapper;
import ru.itis.repositories.interfaces.PermissionRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermissionRepositoryJdbcImpl implements PermissionRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM core.permissions";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM core.permissions WHERE permission_id = ?";
    private static final String SQL_INSERT = "INSERT INTO core.permissions (permission_name, description) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE core.permissions SET permission_name=?, description=? WHERE permission_id=?";
    private static final String SQL_DELETE = "DELETE FROM core.permissions WHERE permission_id=?";

    @Override
    public List<Permission> findAll() {
        List<Permission> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(PermissionMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(PermissionMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void save(Permission permission) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, permission.getPermissionName());
            ps.setString(2, permission.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Long projectId, Permission permission) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, permission.getPermissionName());
            ps.setString(2, permission.getDescription());
            ps.setLong(3, permission.getPermissionId());
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
