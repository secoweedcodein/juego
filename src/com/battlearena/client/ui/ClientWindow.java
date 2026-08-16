package com.battlearena.client.ui;

import com.battlearena.client.game.GamePanel;
import com.battlearena.shared.models.GameState;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class ClientWindow extends JFrame {

    public interface AccionesCliente {
        void login(String username, String password);
        void registrar(String username, String password);
        void jugar();
        void verEstadisticas();
        void cerrarSesion();
        void salir();
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private final AccionesCliente acciones;
    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final EsperaPanel esperaPanel = new EsperaPanel();
    private GamePanel gamePanel;
    private MenuPrincipalPanel menuPanel;

    public ClientWindow(AccionesCliente acciones) {
        super("Battle Arena 2D");
        this.acciones = acciones;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        loginPanel = new LoginPanel(new LoginPanel.Acciones() {
            @Override
            public void alIniciarSesion(String u, String p) {
                acciones.login(u, p);
            }

            @Override
            public void alIrRegistro() {
                mostrarRegistro();
            }
        });

        registerPanel = new RegisterPanel(new RegisterPanel.Acciones() {
            @Override
            public void alRegistrar(String u, String p) {
                acciones.registrar(u, p);
            }

            @Override
            public void alVolver() {
                mostrarLogin();
            }
        });

        contenedor.add(loginPanel, "LOGIN");
        contenedor.add(registerPanel, "REGISTRO");
        contenedor.add(esperaPanel, "ESPERA");
        setContentPane(contenedor);
    }

    public void mostrarLogin() {
        setSize(500, 450);
        cardLayout.show(contenedor, "LOGIN");
    }

    public void mostrarRegistro() {
        cardLayout.show(contenedor, "REGISTRO");
    }

    public void mostrarEspera() {
        cardLayout.show(contenedor, "ESPERA");
    }

    public void mostrarJuego(GameState state) {
        if (gamePanel == null) {
            gamePanel = new GamePanel();
            contenedor.add(gamePanel, "JUEGO");
        }
        setSize(GameState.ANCHO + 16, GameState.ALTO + 39);
        gamePanel.setState(state);
        cardLayout.show(contenedor, "JUEGO");
    }

    public void actualizarJuego(GameState state) {
        if (gamePanel != null) {
            gamePanel.setState(state);
        }
    }

    public void mostrarMenu(String username) {
        setSize(500, 450);

        if (menuPanel != null) {
            contenedor.remove(menuPanel);
        }

        menuPanel = new MenuPrincipalPanel(username, new MenuPrincipalPanel.Acciones() {
            @Override
            public void alJugar() {
                acciones.jugar();
            }

            @Override
            public void alVerEstadisticas() {
                acciones.verEstadisticas();
            }

            @Override
            public void alCerrarSesion() {
                acciones.cerrarSesion();
            }

            @Override
            public void alSalir() {
                acciones.salir();
            }
        });

        contenedor.add(menuPanel, "MENU");
        cardLayout.show(contenedor, "MENU");
    }

    public void mensajeLogin(String texto) {
        loginPanel.mostrarMensaje(texto);
    }

    public void mensajeRegistro(String texto) {
        registerPanel.mostrarMensaje(texto);
    }
}