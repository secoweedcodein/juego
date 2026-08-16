package com.battlearena.server.game;

import com.battlearena.server.network.ClientHandler;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

public class GameRoom {

    private static final GameRoom instancia = new GameRoom();

    public static GameRoom get() {
        return instancia;
    }

    private ClientHandler jugador1;
    private ClientHandler jugador2;
    private Game partida;

    private GameRoom() {
    }

    public synchronized void join(ClientHandler h) {
        if (h.getUserId() == -1) {
            Message e = new Message(MessageType.ERROR);
            e.put("mensaje", "Debes iniciar sesion para jugar.");
            h.enviar(e);
            return;
        }

        if (partida != null) {
            h.enviar(new Message(MessageType.GAME_FULL));
            return;
        }

        if (jugador1 == null) {
            jugador1 = h;
            h.enviar(new Message(MessageType.WAITING));
            return;
        }

        if (jugador1 == h) {
            return;
        }

        jugador2 = h;
        iniciarPartida();
    }

    private void iniciarPartida() {
        partida = new Game(jugador1, jugador2);
        jugador1.setPartida(partida);
        jugador2.setPartida(partida);

        Message m1 = new Message(MessageType.GAME_START);
        m1.put("playerId", "1");
        m1.setPayload(partida.getState());
        jugador1.enviar(m1);

        Message m2 = new Message(MessageType.GAME_START);
        m2.put("playerId", "2");
        m2.setPayload(partida.getState());
        jugador2.enviar(m2);

        System.out.println("Partida iniciada: "
                + jugador1.getUsername() + " vs " + jugador2.getUsername());
    }

    /**
     * Se llama cuando una partida termina por KO o desconexion.
     */
    public synchronized void partidaTerminada(Game g) {
        if (partida == g) {
            partida = null;
            if (jugador1 != null) jugador1.setPartida(null);
            if (jugador2 != null) jugador2.setPartida(null);
            jugador1 = null;
            jugador2 = null;
        }
    }

    public synchronized void leave(ClientHandler h) {
        if (partida != null) {
            if (partida.contiene(h)) {
                partida.terminarPorDesconexion(h);
                partida = null;
                if (jugador1 != null) jugador1.setPartida(null);
                if (jugador2 != null) jugador2.setPartida(null);
                jugador1 = null;
                jugador2 = null;
            }
            return;
        }

        if (jugador1 == h) jugador1 = null;
        if (jugador2 == h) jugador2 = null;
    }
}