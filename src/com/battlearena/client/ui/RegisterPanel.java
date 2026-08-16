package com.battlearena.client.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/**
 * Pantalla de registro de usuarios.
 */
public class RegisterPanel extends JPanel {

    public interface Acciones {
        void alRegistrar(String username, String password);
        void alVolver();
    }

    private final JTextField usuarioField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel mensajeLabel = new JLabel(" ");

    public RegisterPanel(Acciones acciones) {
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        JLabel titulo = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Contrasena:");
        passwordLabel.setForeground(Color.WHITE);

        JButton registrar = new JButton("REGISTRAR");
        JButton volver = new JButton("VOLVER");

        mensajeLabel.setForeground(new Color(255, 120, 120));
        mensajeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        registrar.addActionListener(e -> acciones.alRegistrar(
                usuarioField.getText(),
                new String(passwordField.getPassword())));

        volver.addActionListener(e -> acciones.alVolver());

        JPanel caja = new JPanel(new GridLayout(0, 1, 6, 6));
        caja.setOpaque(false);
        caja.add(titulo);
        caja.add(usuarioLabel);
        caja.add(usuarioField);
        caja.add(passwordLabel);
        caja.add(passwordField);
        caja.add(registrar);
        caja.add(volver);
        caja.add(mensajeLabel);

        add(caja);
    }

    public void mostrarMensaje(String texto) {
        mensajeLabel.setText(texto);
    }
}