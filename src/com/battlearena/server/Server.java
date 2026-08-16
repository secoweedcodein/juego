package com.battlearena.server;

import com.battlearena.server.network.ClientHandler;
import com.battlearena.shared.protocol.ConfigRed;

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

        try (ServerSocket serverSocket = new ServerSocket(ConfigRed.PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + ConfigRed.PUERTO);
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
}