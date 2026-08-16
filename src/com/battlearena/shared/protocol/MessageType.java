package com.battlearena.shared.protocol;

/**
 * Tipos de mensajes entre cliente y servidor.
 */
public enum MessageType {

    // Conexion
    CONNECTED,

    // Autenticacion
    REGISTER,
    REGISTER_OK,
    REGISTER_ERROR,

    LOGIN,
    LOGIN_OK,
    LOGIN_ERROR,

    // Estadisticas
    STATS_REQUEST,
    STATS_RESPONSE,

    // Gestion de partida
    JOIN_GAME,
    WAITING,
    GAME_FULL,

    // Estado del juego
    GAME_START,
    GAME_STATE,
    GAME_OVER,

    // Acciones de jugadores
    PLAYER_MOVE,
    PLAYER_ATTACK,

    // Errores y desconexion
    DISCONNECT,
    ERROR
}