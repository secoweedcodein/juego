package com.battlearena.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Pantalla de menu principal del juego.
 *
 * El panel no decide que hace cada boton: solo avisa
 * a traves de la interfaz Acciones.
 */
public class MenuPrincipalPanel extends JPanel {

    /**
     * Acciones que el cliente debe implementar.
     */
    public interface Acciones {
        void alJugar();
        void alVerEstadisticas();
        void alCerrarSesion();
        void alSalir();
    }

    public MenuPrincipalPanel(String username, Acciones acciones) {
        setBackground(new Color(30, 30, 40));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titulo = new JLabel("BATTLE ARENA 2D", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);

        JLabel usuario = new JLabel("Usuario: " + username, SwingConstants.CENTER);
        usuario.setFont(new Font("Arial", Font.PLAIN, 16));
        usuario.setForeground(Color.LIGHT_GRAY);

        JPanel cabecera = new JPanel(new GridLayout(2, 1, 10, 10));
        cabecera.setOpaque(false);
        cabecera.add(titulo);
        cabecera.add(usuario);

        JButton jugar = new JButton("JUGAR");
        JButton estadisticas = new JButton("ESTADISTICAS");
        JButton cerrarSesion = new JButton("CERRAR SESION");
        JButton salir = new JButton("SALIR");

        JPanel botones = new JPanel(new GridLayout(4, 1, 0, 12));
        botones.setOpaque(false);

        for (JButton boton : new JButton[]{jugar, estadisticas, cerrarSesion, salir}) {
            boton.setFont(new Font("Arial", Font.BOLD, 16));
            boton.setFocusPainted(false);
        }

        jugar.addActionListener(e -> acciones.alJugar());
        estadisticas.addActionListener(e -> acciones.alVerEstadisticas());
        cerrarSesion.addActionListener(e -> acciones.alCerrarSesion());
        salir.addActionListener(e -> acciones.alSalir());

        botones.add(jugar);
        botones.add(estadisticas);
        botones.add(cerrarSesion);
        botones.add(salir);

        add(cabecera, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
    }
}