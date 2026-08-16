package com.battlearena.server.database;

import java.sql.SQLException;

/**
 * Prueba de la FASE 4: inicio de sesion.
 */
public class LoginTest {

    public static void main(String[] args) {
        prepararUsuarioDePrueba();

        probarLogin("juan", "1234");   // debe funcionar
        probarLogin("juan", "9999");   // contrasena incorrecta
        probarLogin("pedro", "1234");  // usuario inexistente
        probarLogin("", "");           // datos vacios
    }

    /**
     * Garantiza que exista el usuario juan para poder probar el login.
     */
    private static void prepararUsuarioDePrueba() {
        try {
            UserRepository.registerUser("juan", "1234");
            System.out.println("Usuario juan creado para la prueba.");
        } catch (IllegalArgumentException e) {
            System.out.println("Usuario juan ya existe, se usa el existente.");
        } catch (SQLException e) {
            System.out.println("ERROR SQL al preparar la prueba: " + e.getMessage());
        }
    }

    private static void probarLogin(String user, String pass) {
        System.out.println("--- Login: usuario=" + user + " ---");

        try {
            int id = UserRepository.login(user, pass);
            System.out.println("LOGIN EXITOSO, id=" + id);
        } catch (IllegalArgumentException e) {
            System.out.println("LOGIN FALLIDO: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR SQL: " + e.getMessage());
        }
    }
}