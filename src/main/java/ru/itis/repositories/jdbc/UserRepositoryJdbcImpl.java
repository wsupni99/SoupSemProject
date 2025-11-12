package ru.itis.repositories.jdbc;

import ru.itis.entities.User;
import ru.itis.exceptions.InvalidDataException;
import ru.itis.mappers.UserMapper;
import ru.itis.repositories.interfaces.UserRepository;
import ru.itis.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM core.users";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM core.users WHERE user_id = ?";
    private static final String SQL_INSERT = "INSERT INTO core.users (name, email, password_hash, contact_info) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE core.users SET name=?, email=?, password_hash=?, contact_info=? WHERE user_id=?";
    private static final String SQL_DELETE = "DELETE FROM core.users WHERE user_id=?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM core.users WHERE email = ?";
    private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD = "SELECT * FROM core.users WHERE email = ? AND password_hash = ?";
    private static final String SQL_FIND_BY_ROLE =
            "SELECT u.* FROM core.users u " +
                    "JOIN core.user_roles ur ON u.user_id = ur.user_id " +
                    "WHERE ur.role_id = ?";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(UserMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(UserMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public void save(User user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getContactInfo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getContactInfo());
            ps.setLong(5, user.getUserId());
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
    public Optional<User> findByEmail(String email) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(UserMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String passwordHash) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL_AND_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(UserMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByRole(Long roleId) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE)) {
            ps.setLong(1, roleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(UserMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return users;
    }
}
