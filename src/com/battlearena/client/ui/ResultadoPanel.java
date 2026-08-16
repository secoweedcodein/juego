package com.battlearena.client.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class ResultadoPanel extends JPanel {

    public interface Acciones {
        void alVolverAlMenu();
    }

    private final JLabel tituloLabel;
    private final JLabel detalleLabel;

    public ResultadoPanel(Acciones acciones) {
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        tituloLabel = new JLabel("", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 40));

        detalleLabel = new JLabel("", SwingConstants.CENTER);
        detalleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        detalleLabel.setForeground(Color.LIGHT_GRAY);

        JButton volver = new JButton("VOLVER AL MENU");

        JPanel caja = new JPanel(new GridLayout(0, 1, 12, 12));
        caja.setOpaque(false);
        caja.add(tituloLabel);
        caja.add(detalleLabel);
        caja.add(volver);

        volver.addActionListener(e -> acciones.alVolverAlMenu());

        add(caja);
    }

    public void mostrar(String resultado, String detalle) {
        if ("VICTORIA".equals(resultado)) {
            tituloLabel.setText("VICTORIA");
            tituloLabel.setForeground(new Color(90, 220, 120));
        } else {
            tituloLabel.setText("DERROTA");
            tituloLabel.setForeground(new Color(255, 100, 100));
        }
        detalleLabel.setText(detalle == null ? "" : detalle);
    }
}