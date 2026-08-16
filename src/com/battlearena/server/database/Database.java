package com.battlearena.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
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