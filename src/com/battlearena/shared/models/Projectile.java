package com.battlearena.shared.models;

import java.io.Serializable;

public class Projectile implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public int x;
    public int y;
    public int dirX;
    public int dirY;
    public int ownerId;

    public Projectile(int id, int x, int y, int dirX, int dirY, int ownerId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.ownerId = ownerId;
    }
}