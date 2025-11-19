package ru.itis.repositories.jdbc;

import ru.itis.entities.UserRole;
import ru.itis.entities.UserRoleId;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.UserRoleMapper;
import ru.itis.repositories.interfaces.UserRoleRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRoleRepositoryJdbcImpl implements UserRoleRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM core.user_roles";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM core.user_roles WHERE user_id = ? AND role_id = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT * FROM core.user_roles WHERE user_id = ?";
    private static final String SQL_FIND_BY_ROLE_ID = "SELECT * FROM core.user_roles WHERE role_id = ?";
    private static final String SQL_INSERT = "INSERT INTO core.user_roles (user_id, role_id) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM core.user_roles WHERE user_id=? AND role_id=?";

    @Override
    public List<UserRole> findAll() {
        List<UserRole> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(UserRoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<UserRole> findById(UserRoleId id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id.getUserId());
            ps.setLong(2, id.getRoleId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(UserRoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        List<UserRole> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(UserRoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<UserRole> findByRoleId(Long roleId) {
        List<UserRole> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE_ID)) {
            ps.setLong(1, roleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(UserRoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(UserRole ur) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setLong(1, ur.getUserId());
            ps.setLong(2, ur.getRoleId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    // Лучше пересоздать аккаунт с новой ролью
    @Override
    public void update(UserRole ur) {
        throw new UnsupportedOperationException("Операция update для таблиц с составным ключом не поддерживается");
    }

    @Override
    public void deleteById(UserRoleId id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id.getUserId());
            ps.setLong(2, id.getRoleId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }
}
