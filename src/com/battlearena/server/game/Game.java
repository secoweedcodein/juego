package com.battlearena.server.game;

import com.battlearena.server.network.ClientHandler;
import com.battlearena.shared.models.GameState;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

public class Game {

    private final ClientHandler handler1;
    private final ClientHandler handler2;
    private final GameState state = new GameState();

    public Game(ClientHandler h1, ClientHandler h2) {
        this.handler1 = h1;
        this.handler2 = h2;
    }

    public GameState getState() {
        return state;
    }

    public boolean contiene(ClientHandler h) {
        return h == handler1 || h == handler2;
    }

    public void broadcastState() {
        Message m = new Message(MessageType.GAME_STATE);
        m.setPayload(state);
        handler1.enviar(m);
        handler2.enviar(m);
    }

    public void terminarPorDesconexion(ClientHandler h) {
        ClientHandler otro = (h == handler1) ? handler2 : handler1;
        Message m = new Message(MessageType.GAME_OVER);
        m.put("mensaje", "El oponente se desconecto. Ganaste por desconexion.");
        otro.enviar(m);
    }
}