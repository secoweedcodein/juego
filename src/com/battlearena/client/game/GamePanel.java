package com.battlearena.client.game;

import com.battlearena.shared.models.GameState;
import com.battlearena.shared.models.PlayerState;
import com.battlearena.shared.models.Projectile;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {

    public interface AccionesJuego {
        void mover(String direccion);
        void atacar();
    }

    private GameState state;
    private int miPlayerId;
    private AccionesJuego acciones;

    public GamePanel() {
        setBackground(new Color(45, 45, 55));
        setPreferredSize(new Dimension(GameState.ANCHO, GameState.ALTO));
        setFocusable(true);
        addKeyListener(this);
    }

    public synchronized void setState(GameState state) {
        this.state = state;
        repaint();
    }

    public void iniciar(int miPlayerId, AccionesJuego acciones) {
        this.miPlayerId = miPlayerId;
        this.acciones = acciones;
        requestFocusInWindow();
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(80, 80, 90));
        g.drawRect(0, 0, GameState.ANCHO - 1, GameState.ALTO - 1);

        if (state == null) return;

        // Proyectiles
        g.setColor(Color.YELLOW);
        for (Projectile p : state.proyectiles) {
            g.fillOval(p.x - 6, p.y - 6, 12, 12);
        }

        dibujarJugador(g, state.jugador1, new Color(80, 140, 255), "J1 WASD + ESPACIO");
        dibujarJugador(g, state.jugador2, new Color(255, 90, 90), "J2 Flechas + ENTER");

        g.setColor(Color.WHITE);
        g.drawString("J1: " + state.jugador1.hp + " HP    J2: " + state.jugador2.hp + " HP", 10, 20);
    }

    private void dibujarJugador(Graphics g, PlayerState p, Color color, String etiqueta) {
        if (!p.alive) {
            g.setColor(Color.GRAY);
            g.fillRect(p.x, p.y, GameState.TAMANO_JUGADOR, GameState.TAMANO_JUGADOR);
            return;
        }

        g.setColor(color);
        g.fillRect(p.x, p.y, GameState.TAMANO_JUGADOR, GameState.TAMANO_JUGADOR);

        g.setColor(Color.BLACK);
        g.fillRect(p.x, p.y - 12, GameState.TAMANO_JUGADOR, 8);
        g.setColor(Color.GREEN);
        g.fillRect(p.x, p.y - 12, GameState.TAMANO_JUGADOR * p.hp / 100, 8);

        g.setColor(Color.WHITE);
        g.drawString(etiqueta, p.x - 10, p.y - 18);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (acciones == null) return;

        if (miPlayerId == 1) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: acciones.mover("UP"); break;
                case KeyEvent.VK_S: acciones.mover("DOWN"); break;
                case KeyEvent.VK_A: acciones.mover("LEFT"); break;
                case KeyEvent.VK_D: acciones.mover("RIGHT"); break;
                case KeyEvent.VK_SPACE: acciones.atacar(); break;
            }
        } else if (miPlayerId == 2) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: acciones.mover("UP"); break;
                case KeyEvent.VK_DOWN: acciones.mover("DOWN"); break;
                case KeyEvent.VK_LEFT: acciones.mover("LEFT"); break;
                case KeyEvent.VK_RIGHT: acciones.mover("RIGHT"); break;
                case KeyEvent.VK_ENTER: acciones.atacar(); break;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}