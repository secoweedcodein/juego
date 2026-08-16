package com.battlearena.server.game;

import com.battlearena.server.database.MatchRepository;
import com.battlearena.server.network.ClientHandler;
import com.battlearena.shared.models.GameState;
import com.battlearena.shared.models.PlayerState;
import com.battlearena.shared.models.Projectile;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

import java.sql.SQLException;
import java.util.Iterator;

public class Game {

    private static final int VELOCIDAD = 15;
    private static final int VEL_PROYECTIL = 12;
    private static final int DANO = 10;
    private static final int COOLDOWN_MS = 400;
    private static final int RADIO_PROYECTIL = 6;

    private final ClientHandler handler1;
    private final ClientHandler handler2;
    private final GameState state = new GameState();

    private long ultimoDisparo1 = 0;
    private long ultimoDisparo2 = 0;
    private int siguienteProyectilId = 1;
    private volatile boolean terminada = false;

    public Game(ClientHandler h1, ClientHandler h2) {
        this.handler1 = h1;
        this.handler2 = h2;
        iniciarLoop();
    }

    private void iniciarLoop() {
        Thread loop = new Thread(() -> {
            while (!terminada) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
                tick();
            }
        }, "game-loop");
        loop.setDaemon(true);
        loop.start();
    }

    public GameState getState() { return state; }

    public boolean contiene(ClientHandler h) {
        return h == handler1 || h == handler2;
    }

    public int getIdDe(ClientHandler h) {
        return (h == handler1) ? 1 : 2;
    }

    public synchronized void mover(ClientHandler h, String direccion) {
        if (terminada) return;
        PlayerState p = state.getJugador(getIdDe(h));
        if (!p.alive) return;

        switch (direccion) {
            case "UP":
                p.y = Math.max(0, p.y - VELOCIDAD);
                p.dirX = 0; p.dirY = -1;
                break;
            case "DOWN":
                p.y = Math.min(GameState.ALTO - GameState.TAMANO_JUGADOR, p.y + VELOCIDAD);
                p.dirX = 0; p.dirY = 1;
                break;
            case "LEFT":
                p.x = Math.max(0, p.x - VELOCIDAD);
                p.dirX = -1; p.dirY = 0;
                break;
            case "RIGHT":
                p.x = Math.min(GameState.ANCHO - GameState.TAMANO_JUGADOR, p.x + VELOCIDAD);
                p.dirX = 1; p.dirY = 0;
                break;
        }
        broadcastState();
    }

    /**
     * Dispara un proyectil en la direccion a la que mira el jugador.
     */
    public synchronized void atacar(ClientHandler h) {
        if (terminada) return;

        int id = getIdDe(h);
        long ahora = System.currentTimeMillis();
        if (id == 1) {
            if (ahora - ultimoDisparo1 < COOLDOWN_MS) return;
            ultimoDisparo1 = ahora;
        } else {
            if (ahora - ultimoDisparo2 < COOLDOWN_MS) return;
            ultimoDisparo2 = ahora;
        }

        PlayerState p = state.getJugador(id);
        if (!p.alive) return;

        state.proyectiles.add(new Projectile(
                siguienteProyectilId++,
                p.x + GameState.TAMANO_JUGADOR / 2,
                p.y + GameState.TAMANO_JUGADOR / 2,
                p.dirX, p.dirY, id));

        broadcastState();
    }

    /**
     * Game loop: mueve proyectiles y detecta impactos.
     */
    private synchronized void tick() {
        if (terminada) return;

        Iterator<Projectile> it = state.proyectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.x += p.dirX * VEL_PROYECTIL;
            p.y += p.dirY * VEL_PROYECTIL;

            if (p.x < 0 || p.y < 0 || p.x > GameState.ANCHO || p.y > GameState.ALTO) {
                it.remove();
                continue;
            }

            PlayerState victima = state.getJugador(p.ownerId == 1 ? 2 : 1);
            if (victima.alive
                    && p.x + RADIO_PROYECTIL > victima.x
                    && p.x - RADIO_PROYECTIL < victima.x + GameState.TAMANO_JUGADOR
                    && p.y + RADIO_PROYECTIL > victima.y
                    && p.y - RADIO_PROYECTIL < victima.y + GameState.TAMANO_JUGADOR) {

                it.remove();
                victima.hp = Math.max(0, victima.hp - DANO);

                if (victima.hp == 0) {
                    victima.alive = false;
                    broadcastState();
                    terminarPartida(p.ownerId);
                    return;
                }
            }
        }
        broadcastState();
    }

    private void terminarPartida(int idGanador) {
        terminada = true;

        ClientHandler ganador = (idGanador == 1) ? handler1 : handler2;
        ClientHandler perdedor = (idGanador == 1) ? handler2 : handler1;

        guardarResultado(ganador.getUserId());

        Message msgG = new Message(MessageType.GAME_OVER);
        msgG.put("resultado", "VICTORIA");
        msgG.put("detalle", "Derrotaste a " + nombre(perdedor));
        ganador.enviar(msgG);

        Message msgP = new Message(MessageType.GAME_OVER);
        msgP.put("resultado", "DERROTA");
        msgP.put("detalle", "Perdiste contra " + nombre(ganador));
        perdedor.enviar(msgP);

        System.out.println("Partida terminada. Gano: " + nombre(ganador));
        GameRoom.get().partidaTerminada(this);
    }

    public void terminarPorDesconexion(ClientHandler h) {
        terminada = true;
        ClientHandler otro = (h == handler1) ? handler2 : handler1;

        guardarResultado(otro.getUserId());

        Message m = new Message(MessageType.GAME_OVER);
        m.put("resultado", "VICTORIA");
        m.put("detalle", "El oponente se desconecto.");
        otro.enviar(m);

        GameRoom.get().partidaTerminada(this);
    }

    private void guardarResultado(int ganadorUserId) {
        try {
            MatchRepository.guardarPartida(
                    handler1.getUserId(),
                    handler2.getUserId(),
                    ganadorUserId);
        } catch (SQLException e) {
            System.out.println("ERROR al guardar la partida: " + e.getMessage());
        }
    }

    private String nombre(ClientHandler h) {
        return h.getUsername() == null ? "?" : h.getUsername();
    }

    public void broadcastState() {
        Message m = new Message(MessageType.GAME_STATE);
        m.setPayload(state);
        handler1.enviar(m);
        handler2.enviar(m);
    }
}