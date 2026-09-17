package dao;

import modelo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Devolve o usuario correspondente, ou null quando usuario/senha nao batem.
    // OBS: num sistema real, compare um HASH da senha (ex.: BCrypt),
    // nunca texto puro. Mantido simples aqui para fins didaticos.
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password FROM user WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
                return null; // nenhuma linha encontrada
            }
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT id FROM user WHERE username = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(User u) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsername().trim());
            ps.setString(2, u.getPassword());
            ps.executeUpdate();
        }
    }
}
