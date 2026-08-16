package com.battlearena.server.network;

import com.battlearena.shared.protocol.ConfigRed;
import com.battlearena.shared.protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Conexion del cliente con el servidor.
 *
 * Un hilo interno lee los mensajes entrantes
 * y avisa al Listener.
 */
public class ServerConnection {

    public interface Listener {
        void onMessage(Message mensaje);
        void onConnectionLost();
    }

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Listener listener;

    public void conectar(Listener listener) throws IOException {
        this.listener = listener;

        socket = new Socket(ConfigRed.HOST, ConfigRed.PUERTO);

        // Primero el OutputStream, luego el InputStream
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        Thread lector = new Thread(() -> {
            try {
                while (true) {
                    Object objeto = in.readObject();
                    if (objeto instanceof Message) {
                        listener.onMessage((Message) objeto);
                    }
                }
            } catch (IOException e) {
                listener.onConnectionLost();
            } catch (ClassNotFoundException e) {
                System.out.println("Objeto desconocido recibido del servidor.");
            }
        }, "lector-servidor");

        lector.setDaemon(true);
        lector.start();
    }

    public synchronized void enviar(Message mensaje) {
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            listener.onConnectionLost();
        }
    }

    public void cerrar() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // No requiere accion
        }
    }
}