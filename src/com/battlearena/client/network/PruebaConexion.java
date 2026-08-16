package com.battlearena.client.network;

import com.battlearena.shared.protocol.ConfigRed;
import com.battlearena.shared.protocol.Message;
import com.battlearena.shared.protocol.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Cliente de consola para probar la conexion de la FASE 6.
 */
public class PruebaConexion {

    public static void main(String[] args) {
        System.out.println("Conectando a " + ConfigRed.HOST + ":" + ConfigRed.PUERTO + " ...");

        try (Socket socket = new Socket(ConfigRed.HOST, ConfigRed.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.flush();

            Object objeto = in.readObject();
            if (objeto instanceof Message) {
                System.out.println("Respuesta del servidor: " + ((Message) objeto).getType());
            }

            Message prueba = new Message(MessageType.LOGIN);
            prueba.put("username", "prueba");
            out.writeObject(prueba);
            out.flush();

            System.out.println("Mensaje de prueba enviado. Prueba terminada.");

        } catch (Exception e) {
            System.out.println("No se pudo conectar al servidor: " + e.getMessage());
            System.out.println("Asegurate de haber iniciado el Server primero.");
        }
    }
}
