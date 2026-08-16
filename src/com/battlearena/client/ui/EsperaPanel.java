package com.battlearena.client.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

public class EsperaPanel extends JPanel {

    public EsperaPanel() {
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        JLabel label = new JLabel("Esperando otro jugador...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.WHITE);

        add(label);
    }
}