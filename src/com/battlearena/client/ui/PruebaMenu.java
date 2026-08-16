package com.battlearena.client.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Prueba de la FASE 5: muestra el menu principal
 * con acciones temporales.
 */
public class PruebaMenu {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Battle Arena 2D");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 420);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MenuPrincipalPanel panel = new MenuPrincipalPanel(
                    "juan",
                    new MenuPrincipalPanel.Acciones() {
                        @Override
                        public void alJugar() {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Aqui ira el sistema multijugador (FASE 6 en adelante).");
                        }

                        @Override
                        public void alVerEstadisticas() {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Aqui iran las estadisticas (FASE 15).");
                        }

                        @Override
                        public void alCerrarSesion() {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Aqui volveremos a la pantalla de login.");
                        }

                        @Override
                        public void alSalir() {
                            System.exit(0);
                        }
                    });

            frame.setContentPane(panel);
            frame.setVisible(true);
        });
    }
}   