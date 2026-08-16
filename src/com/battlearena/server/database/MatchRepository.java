package com.battlearena.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Guarda el resultado de una partida y actualiza estadisticas.
 * Usa una transaccion: o se guarda todo, o no se guarda nada.
 */
public class MatchRepository {

    public static void guardarPartida(int jugador1Id, int jugador2Id, int ganadorId)
            throws SQLException {

        String insertSql = "INSERT INTO partidas (jugador1_id, jugador2_id, ganador_id) "
                + "VALUES (?, ?, ?)";

        String updateSql = "UPDATE usuarios SET partidas_jugadas = partidas_jugadas + 1, "
                + "victorias = victorias + ?, derrotas = derrotas + ? WHERE id = ?";

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psInsert = con.prepareStatement(insertSql);
                 PreparedStatement psUpdate = con.prepareStatement(updateSql)) {

                psInsert.setInt(1, jugador1Id);
                psInsert.setInt(2, jugador2Id);
                psInsert.setInt(3, ganadorId);
                psInsert.executeUpdate();

                // Ganador: +1 victoria
                psUpdate.setInt(1, 1);
                psUpdate.setInt(2, 0);
                psUpdate.setInt(3, ganadorId);
                psUpdate.executeUpdate();

                // Perdedor: +1 derrota
                int perdedorId = (ganadorId == jugador1Id) ? jugador2Id : jugador1Id;
                psUpdate.setInt(1, 0);
                psUpdate.setInt(2, 1);
                psUpdate.setInt(3, perdedorId);
                psUpdate.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
}