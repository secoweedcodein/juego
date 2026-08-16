package com.battlearena.shared.protocol;

/**
 * Configuracion de red compartida entre cliente y servidor.
 *
 * Para un cliente remoto usa las variables BATTLE_ARENA_HOST y
 * BATTLE_ARENA_PORT, sin modificar el codigo fuente.
 */
public class ConfigRed {

    public static final String HOST = valor("BATTLE_ARENA_HOST", "localhost");

    public static final int PUERTO = puerto();

    private static String valor(String nombre, String porDefecto) {
        String valor = System.getenv(nombre);
        return valor == null || valor.isBlank() ? porDefecto : valor;
    }

    private static int puerto() {
        try {
            return Integer.parseInt(valor("BATTLE_ARENA_PORT", "5000"));
        } catch (NumberFormatException e) {
            return 5000;
        }
    }
}
