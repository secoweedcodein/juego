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
 * Pantalla de inicio de sesion.
 */
public class LoginPanel extends JPanel {

    public interface Acciones {
        void alIniciarSesion(String username, String password);
        void alIrRegistro();
    }

    private final JTextField usuarioField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel mensajeLabel = new JLabel(" ");

    public LoginPanel(Acciones acciones) {
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        JLabel titulo = new JLabel("BATTLE ARENA 2D", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Inicia sesion para continuar", SwingConstants.CENTER);
        subtitulo.setForeground(Color.LIGHT_GRAY);

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Contrasena:");
        passwordLabel.setForeground(Color.WHITE);

        JButton iniciar = new JButton("INICIAR SESION");
        JButton crearCuenta = new JButton("CREAR CUENTA");

        mensajeLabel.setForeground(new Color(255, 120, 120));
        mensajeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        iniciar.addActionListener(e -> acciones.alIniciarSesion(
                usuarioField.getText(),
                new String(passwordField.getPassword())));

        crearCuenta.addActionListener(e -> acciones.alIrRegistro());

        JPanel caja = new JPanel(new GridLayout(0, 1, 6, 6));
        caja.setOpaque(false);
        caja.add(titulo);
        caja.add(subtitulo);
        caja.add(usuarioLabel);
        caja.add(usuarioField);
        caja.add(passwordLabel);
        caja.add(passwordField);
        caja.add(iniciar);
        caja.add(crearCuenta);
        caja.add(mensajeLabel);

        add(caja);
    }

    public void mostrarMensaje(String texto) {
        mensajeLabel.setText(texto);
    }
}