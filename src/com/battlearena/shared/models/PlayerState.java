package com.battlearena.shared.models;

import java.io.Serializable;

public class PlayerState implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public int x;
    public int y;
    public int hp;
    public boolean alive;

    public PlayerState(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.alive = true;
    }
}