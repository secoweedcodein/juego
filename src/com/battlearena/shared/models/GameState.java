package com.battlearena.shared.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int ANCHO = 800;
    public static final int ALTO = 600;
    public static final int TAMANO_JUGADOR = 40;

    public PlayerState jugador1;
    public PlayerState jugador2;
    public List<Projectile> proyectiles = new ArrayList<>();

    public GameState() {
        jugador1 = new PlayerState(1, 100, 280);
        jugador2 = new PlayerState(2, 660, 280);
    }

    public PlayerState getJugador(int id) {
        return id == 1 ? jugador1 : jugador2;
    }
}