package com.battlearena.server.database;

import com.battlearena.server.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE username = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void registerUser(String username, String password) throws SQLException {
        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("El usuario debe tener al menos 3 caracteres.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 4 caracteres.");
        }

        String nombre = username.trim();

        if (usernameExists(nombre)) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }

        String hash = PasswordHasher.hash(password);

        String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, hash);
            ps.executeUpdate();
        }
    }

    public static int login(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Usuario y contrasena son obligatorios.");
        }

        String sql = "SELECT id, password FROM usuarios WHERE username = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Usuario o contrasena incorrectos.");
                }

                int id = rs.getInt("id");
                String hashGuardado = rs.getString("password");

                if (!PasswordHasher.verify(password, hashGuardado)) {
                    throw new IllegalArgumentException("Usuario o contrasena incorrectos.");
                }
                return id;
            }
        }
    }

    /**
     * Devuelve { partidas_jugadas, victorias, derrotas } del usuario.
     */
    public static int[] getStats(int userId) throws SQLException {
        String sql = "SELECT partidas_jugadas, victorias, derrotas FROM usuarios WHERE id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new int[]{rs.getInt(1), rs.getInt(2), rs.getInt(3)};
                }
                return new int[]{0, 0, 0};
            }
        }
    }
}