package com.battlearena.server;

import com.battlearena.server.database.Database;
import com.battlearena.server.network.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor de Battle Arena 2D.
 *
 * Acepta conexiones de clientes y asigna un hilo a cada una.
 */
public class Server {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("Battle Arena 2D - Servidor");
        System.out.println("====================================");

        int puerto = puertoServidor();
        try {
            Database.inicializarEsquema();
            System.out.println("Base de datos preparada.");
        } catch (java.sql.SQLException e) {
            System.err.println("ERROR al preparar la base de datos: " + e.getMessage());
            System.exit(1);
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);
            System.out.println("Esperando conexiones... (Ctrl+C para detener)");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente conectado: "
                        + socketCliente.getRemoteSocketAddress());

                new ClientHandler(socketCliente).start();
            }

        } catch (IOException e) {
            System.out.println("ERROR del servidor: " + e.getMessage());
        }
    }

    private static int puertoServidor() {
        String puerto = System.getenv("PORT");
        if (puerto == null || puerto.isBlank()) return 5000;

        try {
            return Integer.parseInt(puerto);
        } catch (NumberFormatException e) {
            System.err.println("PORT no es valido; se usara 5000.");
            return 5000;
        }
    }
}
