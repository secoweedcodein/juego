package com.battlearena.client;

import com.battlearena.client.network.ServerConnection;
import com.battlearena.client.ui.ClientWindow;
import com.battlearena.shared.models.GameState;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client {

    private ServerConnection conexion;
    private ClientWindow ventana;

    public static void main(String[] args) {
        new Client().iniciar();
    }

    private void iniciar() {
        conexion = new ServerConnection();

        try {
            conexion.conectar(new ServerConnection.Listener() {
                @Override
                public void onMessage(Message mensaje) {
                    recibir(mensaje);
                }

                @Override
                public void onConnectionLost() {
                    conexionPerdida();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar al servidor.\nAsegurate de haberlo iniciado.\n\n" + e.getMessage(),
                    "Battle Arena 2D", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            ventana = new ClientWindow(new ClientWindow.AccionesCliente() {
                @Override
                public void login(String u, String p) {
                    Message m = new Message(MessageType.LOGIN);
                    m.put("username", u);
                    m.put("password", p);
                    conexion.enviar(m);
                }

                @Override
                public void registrar(String u, String p) {
                    Message m = new Message(MessageType.REGISTER);
                    m.put("username", u);
                    m.put("password", p);
                    conexion.enviar(m);
                }

                @Override
                public void jugar() {
                    conexion.enviar(new Message(MessageType.JOIN_GAME));
                }

                @Override
                public void verEstadisticas() {
                    JOptionPane.showMessageDialog(ventana, "FASE 15: estadisticas.");
                }

                @Override
                public void cerrarSesion() {
                    ventana.mostrarLogin();
                }

                @Override
                public void salir() {
                    conexion.cerrar();
                    System.exit(0);
                }
            });

            ventana.mostrarLogin();
            ventana.setVisible(true);
        });
    }

    private void recibir(Message mensaje) {
        SwingUtilities.invokeLater(() -> {
            switch (mensaje.getType()) {
                case LOGIN_OK:
                    ventana.mostrarMenu(mensaje.get("username"));
                    break;
                case LOGIN_ERROR:
                    ventana.mensajeLogin(mensaje.get("mensaje"));
                    break;
                case REGISTER_OK:
                    ventana.mensajeLogin(mensaje.get("mensaje"));
                    ventana.mostrarLogin();
                    break;
                case REGISTER_ERROR:
                    ventana.mensajeRegistro(mensaje.get("mensaje"));
                    break;
                case WAITING:
                    ventana.mostrarEspera();
                    break;
                case GAME_FULL:
                    JOptionPane.showMessageDialog(ventana, "La partida esta llena.");
                    break;
                case GAME_START:
                    ventana.mostrarJuego((GameState) mensaje.getPayload());
                    break;
                case GAME_STATE:
                    ventana.actualizarJuego((GameState) mensaje.getPayload());
                    break;
                case GAME_OVER:
                    JOptionPane.showMessageDialog(ventana, mensaje.get("mensaje"));
                    ventana.mostrarLogin();
                    break;
                case ERROR:
                    JOptionPane.showMessageDialog(ventana, mensaje.get("mensaje"),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
        });
    }

    private void conexionPerdida() {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(ventana,
                        "Se perdio la conexion con el servidor.",
                        "Conexion", JOptionPane.WARNING_MESSAGE));
    }
}