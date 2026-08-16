package com.battlearena.client;

import com.battlearena.client.network.ServerConnection;
import com.battlearena.client.ui.ClientWindow;
import com.battlearena.shared.models.GameState;
import com.battlearena.shared.protocol.ConfigRed;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client {

    private ServerConnection conexion;
    private ClientWindow ventana;
    private int miPlayerId = -1;
    private String miUsername = null;

    public static void main(String[] args) {
        new Client().iniciar(args);
    }

    private void iniciar(String[] args) {
        String host = ConfigRed.HOST;
        int port = ConfigRed.PUERTO;

        // Ejemplo: Client 192.168.1.20  o  Client 0.tcp.ngrok.io:12345
        if (args.length > 0) {
            String arg = args[0];
            if (arg.contains(":")) {
                String[] partes = arg.split(":");
                host = partes[0];
                port = Integer.parseInt(partes[1]);
            } else {
                host = arg;
            }
        }

        conexion = new ServerConnection();

        try {
            conexion.conectar(host, port, new ServerConnection.Listener() {
                @Override public void onMessage(Message mensaje) { recibir(mensaje); }
                @Override public void onConnectionLost() { conexionPerdida(); }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar al servidor " + host + ":" + port
                            + "\nAsegurate de haberlo iniciado.\n\n" + e.getMessage(),
                    "Battle Arena 2D", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            ventana = new ClientWindow(new ClientWindow.AccionesCliente() {
                @Override
                public void login(String u, String p) {
                    Message m = new Message(MessageType.LOGIN);
                    m.put("username", u); m.put("password", p);
                    conexion.enviar(m);
                }

                @Override
                public void registrar(String u, String p) {
                    Message m = new Message(MessageType.REGISTER);
                    m.put("username", u); m.put("password", p);
                    conexion.enviar(m);
                }

                @Override
                public void jugar() { conexion.enviar(new Message(MessageType.JOIN_GAME)); }

                @Override
                public void verEstadisticas() { conexion.enviar(new Message(MessageType.STATS_REQUEST)); }

                @Override
                public void cerrarSesion() {
                    miUsername = null;
                    ventana.mostrarLogin();
                }

                @Override
                public void salir() { conexion.cerrar(); System.exit(0); }

                @Override
                public void mover(String direccion) {
                    Message m = new Message(MessageType.PLAYER_MOVE);
                    m.put("direccion", direccion);
                    conexion.enviar(m);
                }

                @Override
                public void atacar() { conexion.enviar(new Message(MessageType.PLAYER_ATTACK)); }

                @Override
                public void volverAlMenu() {
                    if (miUsername != null) ventana.mostrarMenu(miUsername);
                    else ventana.mostrarLogin();
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
                    miUsername = mensaje.get("username");
                    ventana.mostrarMenu(miUsername);
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
                    miPlayerId = Integer.parseInt(mensaje.get("playerId"));
                    ventana.mostrarJuego((GameState) mensaje.getPayload(), miPlayerId);
                    break;
                case GAME_STATE:
                    ventana.actualizarJuego((GameState) mensaje.getPayload());
                    break;
                case GAME_OVER:
                    ventana.mostrarResultado(mensaje.get("resultado"), mensaje.get("detalle"));
                    miPlayerId = -1;
                    break;
                case STATS_RESPONSE:
                    JOptionPane.showMessageDialog(ventana,
                            "Partidas jugadas: " + mensaje.get("partidas")
                                    + "\nVictorias: " + mensaje.get("victorias")
                                    + "\nDerrotas: " + mensaje.get("derrotas"),
                            "Estadisticas de " + miUsername,
                            JOptionPane.INFORMATION_MESSAGE);
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