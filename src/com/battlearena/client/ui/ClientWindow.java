package com.battlearena.client.ui;

import com.battlearena.client.game.GamePanel;
import com.battlearena.shared.models.GameState;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;

public class ClientWindow extends JFrame {

    public interface AccionesCliente {
        void login(String username, String password);
        void registrar(String username, String password);
        void jugar();
        void verEstadisticas();
        void cerrarSesion();
        void salir();
        void mover(String direccion);
        void atacar();
        void volverAlMenu();
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private final AccionesCliente acciones;
    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final EsperaPanel esperaPanel = new EsperaPanel();
    private final ResultadoPanel resultadoPanel;
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
            @Override public void alIniciarSesion(String u, String p) { acciones.login(u, p); }
            @Override public void alIrRegistro() { mostrarRegistro(); }
        });

        registerPanel = new RegisterPanel(new RegisterPanel.Acciones() {
            @Override public void alRegistrar(String u, String p) { acciones.registrar(u, p); }
            @Override public void alVolver() { mostrarLogin(); }
        });

        resultadoPanel = new ResultadoPanel(new ResultadoPanel.Acciones() {
            @Override public void alVolverAlMenu() { acciones.volverAlMenu(); }
        });

        contenedor.add(loginPanel, "LOGIN");
        contenedor.add(registerPanel, "REGISTRO");
        contenedor.add(esperaPanel, "ESPERA");
        contenedor.add(resultadoPanel, "RESULTADO");
        setContentPane(contenedor);
    }

    public void mostrarLogin() {
        setSize(500, 450);
        setLocationRelativeTo(null);
        cardLayout.show(contenedor, "LOGIN");
    }

    public void mostrarRegistro() { cardLayout.show(contenedor, "REGISTRO"); }
    public void mostrarEspera() { cardLayout.show(contenedor, "ESPERA"); }

    public void mostrarResultado(String resultado, String detalle) {
        setSize(500, 450);
        setLocationRelativeTo(null);
        resultadoPanel.mostrar(resultado, detalle);
        cardLayout.show(contenedor, "RESULTADO");
    }

    public void mostrarJuego(GameState state, int miPlayerId) {
        if (gamePanel == null) {
            gamePanel = new GamePanel();
            contenedor.add(gamePanel, "JUEGO");
        }
        setSize(GameState.ANCHO + 16, GameState.ALTO + 39);
        setLocationRelativeTo(null);
        gamePanel.setState(state);
        gamePanel.iniciar(miPlayerId, new GamePanel.AccionesJuego() {
            @Override public void mover(String direccion) { acciones.mover(direccion); }
            @Override public void atacar() { acciones.atacar(); }
        });
        cardLayout.show(contenedor, "JUEGO");
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }

    public void actualizarJuego(GameState state) {
        if (gamePanel != null) gamePanel.setState(state);
    }

    public void mostrarMenu(String username) {
        setSize(500, 450);
        setLocationRelativeTo(null);
        if (menuPanel != null) contenedor.remove(menuPanel);

        menuPanel = new MenuPrincipalPanel(username, new MenuPrincipalPanel.Acciones() {
            @Override public void alJugar() { acciones.jugar(); }
            @Override public void alVerEstadisticas() { acciones.verEstadisticas(); }
            @Override public void alCerrarSesion() { acciones.cerrarSesion(); }
            @Override public void alSalir() { acciones.salir(); }
        });

        contenedor.add(menuPanel, "MENU");
        cardLayout.show(contenedor, "MENU");
    }

    public void mensajeLogin(String t) { loginPanel.mostrarMensaje(t); }
    public void mensajeRegistro(String t) { registerPanel.mostrarMensaje(t); }
}
