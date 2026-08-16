package com.battlearena.server.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Protege contrasenas con hashing SHA-256 + salt.
 *
 * Formato guardado en la base de datos:  saltHex:hashHex
 */
public class PasswordHasher {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Genera un hash nuevo con un salt aleatorio.
     */
    public static String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        String saltHex = bytesToHex(salt);
        String hashHex = sha256Hex(saltHex + password);

        return saltHex + ":" + hashHex;
    }

    /**
     * Comprueba si una contrasena coincide con el hash guardado.
     * Se usara en la FASE 4 (login).
     */
    public static boolean verify(String password, String stored) {
        String[] partes = stored.split(":");
        if (partes.length != 2) {
            return false;
        }

        String saltHex = partes[0];
        String hashHex = sha256Hex(saltHex + password);

        return hashHex.equals(partes[1]);
    }

    private static String sha256Hex(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en este JDK", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}