package ru.itis.repositories.jdbc;

import ru.itis.entities.Role;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.RoleMapper;
import ru.itis.repositories.interfaces.RoleRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryJdbcImpl implements RoleRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM core.roles";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM core.roles WHERE role_id = ?";
    private static final String SQL_INSERT = "INSERT INTO core.roles (role_name, description) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE core.roles SET role_name=?, description=? WHERE role_id=?";
    private static final String SQL_DELETE = "DELETE FROM core.roles WHERE role_id=?";

    @Override
    public List<Role> findAll() {
        List<Role> result = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(RoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Role> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(RoleMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void save(Role role) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(Role role) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getDescription());
            ps.setLong(3, role.getRoleId());
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
