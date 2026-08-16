package com.battlearena.server.network;

import com.battlearena.server.database.UserRepository;
import com.battlearena.server.game.Game;
import com.battlearena.server.game.GameRoom;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler extends Thread {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private int userId = -1;
    private String username = null;
    private Game partidaActual;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }

    public void setPartida(Game partida) {
        this.partidaActual = partida;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            enviar(new Message(MessageType.CONNECTED));

            while (true) {
                Object objeto = in.readObject();
                if (objeto instanceof Message) {
                    procesar((Message) objeto);
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado."
                    + (username != null ? " (usuario: " + username + ")" : ""));
        } catch (ClassNotFoundException e) {
            System.out.println("Objeto desconocido recibido de un cliente.");
        } finally {
            GameRoom.get().leave(this);
            cerrar();
        }
    }

    private void procesar(Message mensaje) {
        System.out.println("Mensaje recibido: " + mensaje.getType());

        switch (mensaje.getType()) {
            case LOGIN: procesarLogin(mensaje); break;
            case REGISTER: procesarRegister(mensaje); break;
            case JOIN_GAME: GameRoom.get().join(this); break;
            case PLAYER_MOVE:
                if (partidaActual != null) partidaActual.mover(this, mensaje.get("direccion"));
                break;
            case PLAYER_ATTACK:
                if (partidaActual != null) partidaActual.atacar(this);
                break;
            case STATS_REQUEST: procesarStats(); break;
            case DISCONNECT: GameRoom.get().leave(this); break;
            default: System.out.println("Tipo sin manejo: " + mensaje.getType());
        }
    }

    private void procesarStats() {
        try {
            int[] stats = UserRepository.getStats(userId);
            Message m = new Message(MessageType.STATS_RESPONSE);
            m.put("partidas", String.valueOf(stats[0]));
            m.put("victorias", String.valueOf(stats[1]));
            m.put("derrotas", String.valueOf(stats[2]));
            enviar(m);
        } catch (SQLException e) {
            enviarErrorSql(e);
        }
    }

    private void procesarLogin(Message mensaje) {
        try {
            int id = UserRepository.login(mensaje.get("username"), mensaje.get("password"));
            userId = id;
            username = mensaje.get("username").trim();

            Message ok = new Message(MessageType.LOGIN_OK);
            ok.put("userId", String.valueOf(id));
            ok.put("username", username);
            enviar(ok);

            System.out.println("Usuario autenticado: " + username + " (id=" + id + ")");
        } catch (IllegalArgumentException e) {
            Message error = new Message(MessageType.LOGIN_ERROR);
            error.put("mensaje", e.getMessage());
            enviar(error);
        } catch (SQLException e) {
            enviarErrorSql(e);
        }
    }

    private void procesarRegister(Message mensaje) {
        try {
            UserRepository.registerUser(mensaje.get("username"), mensaje.get("password"));

            Message ok = new Message(MessageType.REGISTER_OK);
            ok.put("mensaje", "Registro exitoso. Ahora inicia sesion.");
            enviar(ok);
        } catch (IllegalArgumentException e) {
            Message error = new Message(MessageType.REGISTER_ERROR);
            error.put("mensaje", e.getMessage());
            enviar(error);
        } catch (SQLException e) {
            enviarErrorSql(e);
        }
    }

    private void enviarErrorSql(SQLException e) {
        Message error = new Message(MessageType.ERROR);
        error.put("mensaje", "Error de base de datos: " + e.getMessage());
        enviar(error);
    }

    public synchronized void enviar(Message mensaje) {
        try {
            // Evita que ObjectOutputStream reutilice estados ya enviados.
            // Así cada GAME_STATE contiene las coordenadas actuales.
            out.reset();
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            System.out.println("No se pudo enviar el mensaje al cliente.");
        }
    }

    private void cerrar() {
        try {
            socket.close();
        } catch (IOException e) {
            // No requiere accion
        }
    }
}
