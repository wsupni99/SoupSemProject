package ru.itis.repositories.jdbc;

import ru.itis.entities.RolePermission;
import ru.itis.entities.RolePermissionId;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.RolePermissionMapper;
import ru.itis.repositories.interfaces.RolePermissionRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolePermissionRepositoryJdbcImpl implements RolePermissionRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM core.role_permissions";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM core.role_permissions WHERE role_id = ? AND permission_id = ?";
    private static final String SQL_FIND_BY_ROLE_ID = "SELECT * FROM core.role_permissions WHERE role_id = ?";
    private static final String SQL_INSERT = "INSERT INTO core.role_permissions (role_id, permission_id) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM core.role_permissions WHERE role_id=? AND permission_id=?";

    @Override
    public List<RolePermission> findAll() {
        List<RolePermission> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(RolePermissionMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<RolePermission> findById(RolePermissionId id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id.getRoleId());
            ps.setLong(2, id.getPermissionId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(RolePermissionMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<RolePermission> findByRoleId(Long roleId) {
        List<RolePermission> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE_ID)) {
            ps.setLong(1, roleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(RolePermissionMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void deleteById(RolePermissionId id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id.getRoleId());
            ps.setLong(2, id.getPermissionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void save(RolePermission rp) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, rp.getRoleId());
            ps.setLong(2, rp.getPermissionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(RolePermission rp) {
        throw new UnsupportedOperationException("Операция update для таблиц с составным ключом не поддерживается");
    }
}
