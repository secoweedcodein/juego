package com.battlearena.server.database;

import java.sql.SQLException;

/**
 * Prueba de la FASE 3: registro de usuarios.
 */
public class RegistroTest {

    public static void main(String[] args) {
        probar("juan", "1234");   // debe registrarse
        probar("juan", "5678");   // debe fallar: ya existe
        probar("ab", "1234");     // debe fallar: usuario corto
        probar("carlos", "12");   // debe fallar: contrasena corta
    }

    private static void probar(String user, String pass) {
        System.out.println("--- Intento: usuario=" + user + " ---");

        try {
            UserRepository.registerUser(user, pass);
            System.out.println("REGISTRO EXITOSO: " + user);
        } catch (IllegalArgumentException e) {
            System.out.println("VALIDACION FALLIDA: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR SQL: " + e.getMessage());
        }
    }
}