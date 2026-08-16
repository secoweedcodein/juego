package com.battlearena.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Clase encargada de conectar Java con MySQL.
 */
public class Database {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD
        );
    }

    /** Crea las tablas necesarias cuando la base de datos esta vacia. */
    public static void inicializarEsquema() throws SQLException {
        try (Connection conexion = getConnection(); Statement statement = conexion.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "partidas_jugadas INT NOT NULL DEFAULT 0, "
                    + "victorias INT NOT NULL DEFAULT 0, "
                    + "derrotas INT NOT NULL DEFAULT 0, "
                    + "fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS partidas ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "jugador1_id INT NOT NULL, jugador2_id INT NOT NULL, ganador_id INT NOT NULL, "
                    + "fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (jugador1_id) REFERENCES usuarios(id), "
                    + "FOREIGN KEY (jugador2_id) REFERENCES usuarios(id), "
                    + "FOREIGN KEY (ganador_id) REFERENCES usuarios(id))");
        }
    }

    public static void main(String[] args) {
        System.out.println("Probando conexion a MySQL...");

        try (Connection conexion = getConnection()) {
            System.out.println("CONEXION EXITOSA a: "
                    + conexion.getMetaData().getURL());
        } catch (SQLException e) {
            System.out.println("ERROR de conexion: " + e.getMessage());
        }
    }
}
